package idusw.soccerworld.controller;

import idusw.soccerworld.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class TeamController {
    final TeamService teamService;
    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    @GetMapping("/admin/premier-league")
    public String getPremierLeague(){
        return "/admin/premier-league";
    }

    @GetMapping("/admin/laliga")
    public String getLagliga(){
        return "/admin/laliga";
    }

    @GetMapping("/admin/bundesliga")
    public String goBundesliga(){
        return "/admin/bundesliga";
    }

    @GetMapping("/admin/serie-a")
    public String goSerieaA(){
        return "/admin/serie-a";
    }

    //팀 정보 페이지
    @GetMapping("/team/info")
    public String goInfo(@RequestParam(value = "team-id") String teamId, Model model) {
        model.addAttribute("info", teamService.getTeamDetails(teamId));

        //DB에서 모든 팀 정보 가져오기(fragment를 위한)
        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        return "/team/info";
    }

    @GetMapping("/teamInfo")
    @ResponseBody
    public Object getTeamInfo(@RequestParam(required = false,value="leagueNum")int leagueNum){
        ResponseEntity<Map> response = teamService.getTeamInfo(leagueNum);
        return response;
    }

    @PostMapping("/admin/insertTeams")
    @ResponseBody
    public ResponseEntity insertTeams(@RequestBody Map<String, Object> teamsData){
        int result = teamService.insertTeamInfo(teamsData);
        if(result > 0) {
            return new ResponseEntity<>("성공적으로 등록되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게임 등록 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
