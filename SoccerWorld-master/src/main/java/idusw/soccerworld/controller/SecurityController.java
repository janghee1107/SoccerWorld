package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.MemberDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecurityController {
    @GetMapping("/security/isLoggedIn") //로그인 사용자인지, 익명사용자인지 판별
    @ResponseBody
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));
        //기본적으로 로그인안해도 익명유저가 생성되기에 설정
    }

    @GetMapping("/security/isLoggedInCheck") //로그인 사용자인지, 익명사용자인지 판별
    @ResponseBody
    public Long isLoggedIns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new MemberDto().getMemberId();

        //기본적으로 로그인안해도 익명유저가 생성되기에 설정
    }
}
