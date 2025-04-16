package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.CommentReactionDto;
import idusw.soccerworld.domain.dto.PostReactionDto;
import idusw.soccerworld.service.CommentReactionService;
import idusw.soccerworld.service.PostReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reactions")
public class ReactionController {

    private final PostReactionService postReactionService;
    private final CommentReactionService commentReactionService;

    // 게시글에 대한 좋아요/싫어요 토글 처리
    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> reactToPost(@RequestBody PostReactionDto dto) {
        postReactionService.toggleReaction(dto);

        Long postId = dto.getPostDto().getPostId();
        Long memberId = dto.getMemberDto().getMemberId();

        // Reaction 테이블에서 해당 게시글의 UP, DOWN 개수를 조회
        Map<String, Integer> counts = postReactionService.getReactionCounts(postId);

        PostReactionDto postReactionDto = postReactionService.findByMember(postId, memberId);
        String userReaction = postReactionDto != null ? postReactionDto.getReactionType() : null;

        Map<String, Object> result = new HashMap<>();
        result.put("UP",   counts.getOrDefault("UP",   0));
        result.put("DOWN", counts.getOrDefault("DOWN", 0));
        result.put("userReaction", userReaction);

        return ResponseEntity.ok(result);
    }

    // 댓글에 대한 좋아요/싫어요 토글 처리
    @PostMapping("/comment")
    public ResponseEntity<Map<String, Object>> reactToComment(@RequestBody CommentReactionDto dto) {
        commentReactionService.toggleReaction(dto);

        Long commentId = dto.getCommentDto().getCommentId();
        Long memberId = dto.getMemberDto().getMemberId();

        // Reaction 테이블에서 해당 댓글의 UP, DOWN 개수를 조회
        Map<String, Integer> counts = commentReactionService.getReactionCounts(commentId);

        CommentReactionDto commentReactionDto = commentReactionService.findByMember(commentId, memberId);
        String userReaction = commentReactionDto != null ? commentReactionDto.getReactionType() : null;

        Map<String, Object> result = new HashMap<>();
        result.put("UP",   counts.getOrDefault("UP",   0));
        result.put("DOWN", counts.getOrDefault("DOWN", 0));
        result.put("userReaction", userReaction);

        return ResponseEntity.ok(result);
    }
}
