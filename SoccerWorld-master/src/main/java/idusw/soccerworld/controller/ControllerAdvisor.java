package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.TeamDto;
import idusw.soccerworld.service.LeagueService;
import idusw.soccerworld.service.TeamService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

//공통 로직을 담당하는 클래스, 서버 시작과 함께 한 번만 동작한다.
@ControllerAdvice
public class ControllerAdvisor {
    private LeagueService leagueService;
    private TeamService teamService;

    public ControllerAdvisor(LeagueService leagueService, TeamService teamService) {
        this.leagueService = leagueService;
        this.teamService = teamService;
    }

    //모든 팀 정보를 model 객체에 저장. 해당 model에 저장된 객체는 어노테이션에 명시한 이름대로 getAttribute를 통해 접근 가능
    @ModelAttribute("fragmentData")
    public List<TeamDto> teamData() {
        return teamService.getAllTeamsByDB();
    }
}
