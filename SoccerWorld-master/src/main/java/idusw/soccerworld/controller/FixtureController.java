package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.GameDto;
import idusw.soccerworld.domain.dto.PredictionDto;
import idusw.soccerworld.service.GameService;
import idusw.soccerworld.service.PredictionService;
import idusw.soccerworld.service.ScheduleApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class FixtureController {
    final ScheduleApiService scheduleService;
    final GameService gameService;
    final PredictionService predictionService;
    final ScheduleApiService scheduleApiService;

    public FixtureController(ScheduleApiService scheduleService,
                             GameService gameService,
                             PredictionService predictionService,
                             ScheduleApiService scheduleApiService) {
        this.scheduleService = scheduleService;
        this.gameService = gameService;
        this.predictionService = predictionService;
        this.scheduleApiService = scheduleApiService;
    }


    @GetMapping("fixture/schedule")
    public String goPrediction(@RequestParam(required = false, value = "league") String league,
                               @RequestParam(required = false, value = "selectedDate")String date,
                               @RequestParam(required = false, value = "round") Integer round,
                               Model model) {
        List<GameDto> gameDtoList;
        LocalDateTime dateTime;

        if (date != null) {
            dateTime = LocalDateTime.parse(date + "T00:00:00");
        } else {
            dateTime = LocalDateTime.now();
        }

        GameDto gameDto = GameDto.builder()
                .gameId(0)
                .dateTime(dateTime)
                .league(league)
                .round(round)
                .build();

        if(date != null){
            gameDtoList = gameService.getGamesByDate(gameDto);
        } else {
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            model.addAttribute("isRound", "y");
            if (round != null) {
                gameDtoList = gameService.getGamMoreByRound(gameDto);
            } else {
                gameDtoList = gameService.getGameByWeek(gameDto);
                round = gameDtoList.get(0).getRound();
            }
        }

        if (!gameDtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(gameDtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("predictions",predictionPercentages);
        }

        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        model.addAttribute("Games",gameDtoList);
        model.addAttribute("today",date);
        model.addAttribute("currentRound", round);

        if(league != null) {
            model.addAttribute("leagueName",league);
            return "/fixture/prediction";
        } else {
            return "/error/404";
        }

    }

    @GetMapping("/admin/schedule")
    @ResponseBody
    public Object getFixture(@RequestParam(required = false, value = "selectedDate")String paramDate,
                             @RequestParam(required = false, value = "leagueNum")int leagueNum) {
        ResponseEntity<Map> response;
        if("0".equals(paramDate)) {
            response =  scheduleService.getGameApiByYearSeason(leagueNum);;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(paramDate,formatter);
            LocalDate previousDate = date.minusDays(1);
            String fromDate = previousDate.format(formatter);
            String toDate = paramDate;
            response = scheduleService.getGameApiByLeagueAndDate(leagueNum,fromDate,toDate);
        }
        return response;

    }

    @PostMapping("/admin/insertGame")
    @ResponseBody
    public ResponseEntity insertGame(@RequestBody Map<String, Object> gameData){

            int result = gameService.insertGames(gameData);
            if(result > 0) {
                return new ResponseEntity<>("성공적으로 등록되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("게임 등록 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @GetMapping("/posts")
    public String testListCode(@RequestParam(required = false,value = "lastRound") Integer lastRound,
                               @RequestParam(required = false,value = "leagueParam") String league,Model model){
        GameDto gameDto = GameDto.builder()
                .gameId(0)
                .round(lastRound)
                .league(league)
                .build();
        List<GameDto> gameDtoList = gameService.getGamMoreByRound(gameDto);
        model.addAttribute("isRound","y");

        System.out.println("라운드:"+lastRound);
        System.out.println("포스트:"+gameDtoList);
        model.addAttribute("Games", gameDtoList);
        if (!gameDtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(gameDtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("predictions",predictionPercentages);
        }
        return "fixture/prediction :: matchList";
    }
}
