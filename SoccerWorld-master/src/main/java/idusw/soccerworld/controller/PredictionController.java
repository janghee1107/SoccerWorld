package idusw.soccerworld.controller;

import idusw.soccerworld.domain.dto.GameDto;
import idusw.soccerworld.domain.dto.MemberDto;
import idusw.soccerworld.domain.dto.PredictionDto;
import idusw.soccerworld.service.GameService;
import idusw.soccerworld.service.MemberService;
import idusw.soccerworld.service.PredictionService;
import idusw.soccerworld.service.ScheduleApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class PredictionController {
    final MemberService memberService;
    final GameService gameService;
    final PredictionService predictionService;
    final ScheduleApiService scheduleApiService;

//    LocalDate today = LocalDate.now();
//
//    // 이번 주의 시작일 계산 (월요일)
//    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
//
//    // 이번 주의 끝일 계산 (일요일)
//    LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
//
//    // 날짜 포맷 지정 (예: yyyy-MM-dd)
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    String startDayFormat = startOfWeek.format(formatter);
//    String endDayFormat = endOfWeek.format(formatter);

    public PredictionController(MemberService memberService,
                                GameService gameService,
                                PredictionService predictionService,
                                ScheduleApiService scheduleApiService){
        this.memberService = memberService;
        this.gameService = gameService;
        this.predictionService = predictionService;
        this.scheduleApiService = scheduleApiService;
    }
    @GetMapping("/prediction")
    public String goPrediction(@RequestParam(required = false, value = "selectedDate")String date,
                               @RequestParam(required = false, value = "round") Integer round , Model model) {
        List<GameDto> gameDtoList = null;
        if (date != null && round != null) {
//            gameDtoList = gameService.getGamesByDate(date); //예측페이지의 게임정보들 불러옴
        } else if (date == null && round != null) {
//            gameDtoList = gameService.getGamMoreByRound(round);
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            model.addAttribute("isRound","y");
        } else if (round == null && date == null) {
            round = scheduleApiService.getGameApiCurrentMatchDay();
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            gameDtoList = gameService.getGamMoreByRound(round);
//            gameDtoList = gameService.getGameByWeek(gameDtoList);
            model.addAttribute("isRound","y");
        }

        if (gameDtoList != null && !gameDtoList.isEmpty()){
            List<PredictionDto> predictionDtoList = predictionService.getPredictions(gameDtoList); //예측게임에 해당하는 예측테이블 정보들 불러옴
            Map<Long, Map<String, String>> predictionPercentages = predictionService.getPredictionPercentages(predictionDtoList);   //예측게임의 gameId 기준으로 게임의 예측값들을 100분율 퍼센트 예측률로 구하기
            model.addAttribute("predictions",predictionPercentages);
        }

        model.addAttribute("teamList", model.getAttribute("fragmentData"));
        model.addAttribute("Games",gameDtoList);
        model.addAttribute("today",date);
        model.addAttribute("currentRound", round);

        return "/fixture/prediction";
    }

    @PostMapping("/prediction")
    @ResponseBody
    public ResponseEntity inputPrediction(@RequestBody Map<String, Object> predictionData) {
        int memberId = (int) predictionData.get("memberId");
        int gameId = (int) predictionData.get("gameId");
        int result = (int) predictionData.get("result");
        PredictionDto predictionDto = new PredictionDto();
        MemberDto memberDto = memberService.getMemberByMemberId(memberId); //예측 테이블 중복값을 확인하기위한 MemberDto(DB = memeber_id) 확인
        GameDto gameDto = gameService.getGameByGameId(gameId); //예측 테이블 중복값을 확인하기 위한 GameDto(DB = game_id) 확인
        System.out.println("멤버 정보" + memberDto);
        System.out.println("게임 정보" + gameDto);
        predictionDto.setResult(result);
        predictionDto.setMemberDto(memberDto);
        predictionDto.setGameDto(gameDto);
        if (predictionService.checkPrediction(predictionDto) == "가능") { //예측 테이블 중복확인 서비스
            predictionService.insertPrediction(predictionDto);
            return new ResponseEntity<>("예측이 성공적으로 등록되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("이미 예측을 하셨습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login")
    public String login(){
        return "/main/login";
    }


}