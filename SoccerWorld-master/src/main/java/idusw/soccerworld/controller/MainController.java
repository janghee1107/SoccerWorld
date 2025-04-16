package idusw.soccerworld.controller;

import idusw.soccerworld.service.LeagueService;
import idusw.soccerworld.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final LeagueService leagueService;

    //생성자 주입
    public MainController(LeagueService leagueService, TeamService teamService) {
        this.leagueService = leagueService;
    }

    //메인 페이지 이동
    @GetMapping("/main/index")
    public String goIndex(Model model) {
        //4대 리그의 순위 정보 가져오기
        model.addAttribute("pLStand",leagueService.getStandingsByApi("PL"));
        model.addAttribute("laLigaStand",leagueService.getStandingsByApi("PD"));
        model.addAttribute("serieAStand",leagueService.getStandingsByApi("SA"));
        model.addAttribute("bundesStand",leagueService.getStandingsByApi("BL1"));

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));

        return "/main/index";
    }

    //에러 페이지 처리
    @GetMapping("/error")
    public String go404() {
        return "/error/404";
    }
}