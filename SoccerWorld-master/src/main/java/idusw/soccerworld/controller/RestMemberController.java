package idusw.soccerworld.controller;

import idusw.soccerworld.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestMemberController {
    private MemberService memberService;

    public RestMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //아이디 검색 후 결과 반환
    @GetMapping("/member/id/{id}")
    public String idCheck(@PathVariable String id) {
        return memberService.selectById(id);
    }

    //닉네임 검색 후 결과 반환
    @GetMapping("/member/nickname/{nickname}")
    public String nicknameCheck(@PathVariable String nickname) {
        return memberService.selectByNickname(nickname);
    }
}
