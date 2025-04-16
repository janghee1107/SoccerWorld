package idusw.soccerworld.controller;

import idusw.soccerworld.config.WebConfig;
import idusw.soccerworld.domain.dto.*;
import idusw.soccerworld.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class CommunityController {

    private final CategoryService categoryService;
    private final PostService postService;
    private final CommentService commentService;
    private final PostReactionService postReactionService;
    private final CommentReactionService commentReactionService;

    @RequestMapping(method = RequestMethod.GET,value = "/")
    public String goIndex(){
        return "/main/index";
    }

    //  카테고리 불러오기 및 해당 카테고리의 post 불러오기
    @GetMapping("/category/list")
    public String findAll(@RequestParam(value= "id", defaultValue = "1") Long categoryId,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size,
                          Model model){
        // 1) 전체 게시글 수 조회
        int totalPosts = postService.countByCategory(categoryId);

        // 2) offset 계산 (0-based)
        int offset = (page - 1) * size;

        // 3) 페이징된 게시글 목록 조회
        List<PostDto> postDTOList = postService.findPagedByCategory(categoryId, offset, size);

        // --- 페이징 관련 모델 추가 ---
        model.addAttribute("postList", postDTOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", (totalPosts + size - 1) / size);

        List<CategoryDto> categoryDTOList = categoryService.findAll();
        model.addAttribute("categoryList", categoryDTOList);
        model.addAttribute("currentCategory", categoryId);

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        return "/community/list";

    }

    @GetMapping("/category/{id}")
    public String selectedCategory(
            @PathVariable("id") Long categoryId,
            @RequestParam(value="q",    required=false) String q,               // 검색어
            @RequestParam(value="type", defaultValue="title") String type,     // 검색 대상: title or author
            @RequestParam(value="page", defaultValue="1")    int page,         // 현재 페이지
            @RequestParam(value="size", defaultValue="10")   int size,         // 페이지당 글 개수
            Model model
    ) {
        // 1) offset 계산 (0-based index)
        int offset = (page - 1) * size;

        List<PostDto> postList;
        int totalPosts;

        if (q != null && !q.isBlank()) {
            // 검색어가 있을 때
            if ("author".equals(type)) {
                // 작성자 검색
                postList   = postService.searchByAuthor(categoryId, q, offset, size);
                totalPosts = postService.countByAuthor(categoryId, q);
            } else {
                // 제목 검색 (기본)
                postList   = postService.searchByTitle(categoryId, q, offset, size);
                totalPosts = postService.countByTitle(categoryId, q);
            }
        } else {
            // 검색어 없을 때, 일반 페이징
            postList   = postService.findPagedByCategory(categoryId, offset, size);
            totalPosts = postService.countByCategory(categoryId);
        }

        // --- 모델에 필요한 속성들 추가 ---
        model.addAttribute("postList",       postList);
        model.addAttribute("totalPosts",     totalPosts);
        model.addAttribute("currentPage",    page);
        model.addAttribute("pageSize",       size);
        model.addAttribute("totalPages",     (totalPosts + size - 1) / size);
        model.addAttribute("q",              q);
        model.addAttribute("type",           type);
        model.addAttribute("currentCategory",categoryId);
        model.addAttribute("categoryList",   categoryService.findAll());
        model.addAttribute("teamList",       model.getAttribute("fragmentData"));

        return "/community/list";
    }

    //  post 생성
    @GetMapping("/post/create")
    public String create(Model model,
                         HttpServletRequest request){
        List<CategoryDto> categoryDTOList = categoryService.findAll();
        model.addAttribute("categoryList", categoryDTOList);

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", csrfToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || (authentication.getPrincipal() instanceof String)) {
            return "redirect:/category/list";
        }

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        return "community/create";
    }

    @PostMapping("/post/create")
    public String create(PostDto postDTO,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         HttpServletRequest request) {
        // SecurityContextHolder를 통해 로그인한 사용자의 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long loggedInMemberId = null;
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {
            // principal을 MemberDetails로 캐스팅
            MemberDetails loggedInMember = (MemberDetails) authentication.getPrincipal();
            loggedInMemberId = loggedInMember.getMemberId();
        } else {
            // 로그인하지 않은 경우에 대한 처리 (예외 발생 또는 기본값 할당)
            loggedInMemberId = 0L; // 필요에 따라 예외 처리
        }

        // 폼에서 전송된 memberDto.memberId는 무시하고, 로그인한 사용자 정보로 직접 설정
        MemberDto member = new MemberDto();
        member.setMemberId(loggedInMemberId);
        postDTO.setMemberDto(member);

        postService.create(postDTO);

        // 이미지 저장
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 저장 경로 설정
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                // 파일 이름 고유화
                String originalName = imageFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String savedName = uuid + "_" + originalName;

                // 파일 저장
                File dest = new File(uploadDir + savedName);
                imageFile.transferTo(dest);

                // 웹 접근 경로 설정 및 DB 반영
                String webPath = "/uploads/" + savedName;
                postService.updatePicturePaths(postDTO.getPostId(), webPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/category/list";
    }


    //  post detail
    @GetMapping("/post/{id}/{categoryId}")
    public String findById(@PathVariable("id") Long postId,
                           @PathVariable("categoryId") Long categoryId,
                           Model model) {
        List<CategoryDto> categoryDTOList = categoryService.findAll();
        model.addAttribute("categoryList", categoryDTOList);

        model.addAttribute("currentCategory", categoryId);

        postService.updateViewCount(postId);    //  조회수 처리

        // 게시글 내용
        PostDto postDTO = postService.findById(postId);
        model.addAttribute("post", postDTO);

        // 로그인한 사용자
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String))
                ? ((MemberDetails) authentication.getPrincipal()).getMemberId()
                : null;
        model.addAttribute("currentMemberId", memberId);

        // 현재 사용자 반응 조회
        String userReaction = null;
        if(memberId != null) {
            PostReactionDto postReactionDto = postReactionService.findByMember(postId, memberId);
            userReaction = (postReactionDto != null ? postReactionDto.getReactionType() : null);
        }
        model.addAttribute("userReaction", userReaction);

        // 댓글 내용
        List<CommentDto> commentDtoList = commentService.getCommentsByPost(postId);
        model.addAttribute("commentList", commentDtoList);

        // 각 댓글에 대한 사용자 반응 조회
        Map<Long, String> commentUserReactions;
        if (memberId != null) {
            commentUserReactions = commentReactionService.findUserReactionsForPostComments(postId, memberId);
        } else {
            commentUserReactions = Collections.emptyMap();
        }
        model.addAttribute("commentUserReactions", commentUserReactions);

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        return "/community/detail";
    }

    //  post update
    @GetMapping("post/update/{id}")
    public String updatePost(@PathVariable("id") Long postId,
                             Model model) {
        PostDto postDTO = postService.findById(postId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || (authentication.getPrincipal() instanceof String)) {
            return "redirect:/member/login?page-type=login";    // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        MemberDetails loggedInMember = (MemberDetails) authentication.getPrincipal();
        if (!postDTO.getMemberDto().getMemberId().equals(loggedInMember.getMemberId())) {
            return "redirect:/category/list";   // 포스트 작성자와 로그인 사용자가 다르면 list 페이지로 리다이렉트
        }

        model.addAttribute("post", postDTO);

        List<CategoryDto> categoryDTOList = categoryService.findAll();
        model.addAttribute("categoryList", categoryDTOList);

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        return "community/update";
    }

    @PostMapping("post/update/{id}")
    public String updatePost(@PathVariable("id") Long postId,
                             @ModelAttribute PostDto postDTO,
                             @RequestParam(value="imageFile", required=false) MultipartFile imageFile) {
        // 원래 포스트 조회 후 작성자 검증
        PostDto originalPost = postService.findById(postId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || (authentication.getPrincipal() instanceof String)) {
            return "redirect:/member/login?page-type=login";    // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        MemberDetails loggedInMember = (MemberDetails) authentication.getPrincipal();
        if (!originalPost.getMemberDto().getMemberId().equals(loggedInMember.getMemberId())) {
            return "redirect:/category/list";   // 포스트 작성자와 로그인 사용자가 다르면 list 페이지로 리다이렉트
        }

        postService.updatePost(postDTO, postId);

        // 이미지 수정
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 저장 경로 설정
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                // 파일 이름 고유화
                String originalName = imageFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String savedName = uuid + "_" + originalName;

                // 파일 저장
                File dest = new File(uploadDir + savedName);
                imageFile.transferTo(dest);

                // 웹 접근 경로 설정 및 DB 반영
                String webPath = "/uploads/" + savedName;
                postService.updatePicturePaths(postDTO.getPostId(), webPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 업데이트된 게시글을 다시 DB에서 조회하여 category 정보를 가져옴
        PostDto updatedPost = postService.findById(postId);
        Long categoryId = updatedPost.getCategoryDto() != null ? updatedPost.getCategoryDto().getCategoryId() : 1L; // 기본값 처리 가능

        String parameter = postId + "/" + categoryId;
        return "redirect:/post/" + parameter;
    }


    //  post delete
    @GetMapping("post/delete/{id}")
    public String deletePost(@PathVariable("id") Long postId,
                             Model model) {
        PostDto postDTO = postService.findById(postId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || (authentication.getPrincipal() instanceof String)) {
            return "redirect:/member/login?page-type=login";    // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        MemberDetails loggedInMember = (MemberDetails) authentication.getPrincipal();
        if (!postDTO.getMemberDto().getMemberId().equals(loggedInMember.getMemberId())) {
            return "redirect:/category/list";   // 포스트 작성자와 로그인 사용자가 다르면 list 페이지로 리다이렉트
        }

        postService.deletePost(postId);

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        return "redirect:/category/list";
    }

    //  comment add
    @PostMapping("/post/{id}/{categoryId}/comment/add")
    public String addComment(@PathVariable("id") Long postId,
                             @PathVariable("categoryId") Long categoryId,
                             @ModelAttribute CommentDto commentDto,
                             @ModelAttribute PostDto postDto, Model model)    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long loggedInMemberId = null;
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {
            // principal을 MemberDetails로 캐스팅
            MemberDetails loggedInMember = (MemberDetails) authentication.getPrincipal();
            loggedInMemberId = loggedInMember.getMemberId();
        } else {
            // 로그인하지 않은 경우에 대한 처리 (예외 발생 또는 기본값 할당)
            loggedInMemberId = 0L; // 필요에 따라 예외 처리
        }
        MemberDetails loggedInMember = (MemberDetails) authentication.getPrincipal();

        MemberDto member = new MemberDto();
        member.setMemberId(loggedInMemberId);
        commentDto.setMemberDto(member);

        postDto.setPostId(postId);

        commentService.addComment(commentDto);
        String parameter = postId + "/" + categoryId;
        return "redirect:/post/" + parameter;
    }

    //  comment update
    @PostMapping("/post/{id}/{categoryId}/comment/update")
    public String updateComment(@PathVariable("id") Long postId,
                                @PathVariable("categoryId") Long categoryId,
                                @RequestParam("commentId") Long commentId,
                                @ModelAttribute CommentDto commentDto) {
        commentService.updateComment(commentDto, commentId);

        String parameter = postId + "/" + categoryId;
        return "redirect:/post/" + parameter;
    }

    //  comment delete
    @PostMapping("/post/{id}/{categoryId}/comment/delete")
    public String deleteComment(@PathVariable("id") Long postId,
                                @PathVariable("categoryId") Long categoryId,
                                @RequestParam("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        String parameter = postId + "/" + categoryId;
        return "redirect:/post/" + parameter;
    }

}