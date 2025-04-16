package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.MemberDto;
import idusw.soccerworld.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //로그인 페이지
    @GetMapping("/member/login")
    public String goLogin(@RequestParam(value = "error", required = false) String error,
                          Model model) {


        //로그인 요청 실패 시 메시지 전달
        if(error != null && error.equals("login")) {
            model.addAttribute("message", "비밀번호 또는 아이디를 확인해주세요");
        }


        return "/member/login";
    }

    //회원가입 요청
    @PostMapping("/member/register")
    public String register(@ModelAttribute MemberDto memberDto, RedirectAttributes redirectAttributes) {
        //view에서 가져온 가입 정보 전달
        int result = memberService.insertMember(memberDto);

        //view에 회원 가입 성공 여부에 따른 메세지 전달
        if(result == 1) redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        else redirectAttributes.addFlashAttribute("message", "서버 오류가 발생했습니다. 다시 시도해 주세요");
        return "redirect:/main/index";
    }
}
