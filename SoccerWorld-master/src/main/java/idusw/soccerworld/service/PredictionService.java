package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.GameDto;
import idusw.soccerworld.domain.dto.PredictionDto;
import idusw.soccerworld.repository.PredictionRepository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredictionService {
    PredictionRepository predictionRepository;

    public PredictionService(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    public Map<Long, Map<String, String>> getPredictionPercentages(List<PredictionDto> predictionDtoList){
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        Map<Long, Map<String, Long>> predictionsCounts = new HashMap<>();
        // {gameId : { (1홈팀승)  : gameId의 예측1 해당하는 예측값들의 개수 저장}}   //ex. (게임ID) : {홈승=2개 , 어웨이승=1개 , 무승부=0개}

        for (PredictionDto predictionDto : predictionDtoList) {
            long gameId = predictionDto.getGameDto().getGameId(); // PredictionDto 내에 GameDto 포함
            int result = predictionDto.getResult(); //예측값 (1: 홈팀 승 , 2:어웨이팀 승 , 0: 무승부)

            predictionsCounts.putIfAbsent(gameId, new HashMap<>()); // {gameId : {}} 생성
            String resultStr = String.valueOf(result);
            predictionsCounts.get(gameId).put(resultStr, predictionsCounts.get(gameId).getOrDefault(resultStr, 0L) + 1);
            // 만약 gameId에 해당하는 예측값이 1(홈승)인결과가 2개있다고 가정 (ex. 즉 홈팀 승 투표 2개)
            // 첫번째 턴 gameId에서의 resultStr값을 가져옴 -> 처음 가저올때는 resultStr에 해당하는값이 없음. 0부터시작 시작 카운트를 해야하기에 OL , OL + 1 = 1개
            // 두번째 턴 이미 첫번째 턴에서 resultStr값을 가져왔기에 이미 1(홈승)해당하는 resultStr의 1개수가 있음
            // getOrDefault(resultStr,OL) + 1 에서 resultStr에서 1(홈승)해당하는 1개가있기 때문에 resultStr값 그대로 반환 1 + 1 = 2개 , 즉 1(홈승) = 2개
        }

        Map<Long, Map<String, String>> predictionPercentages = new HashMap<>(); // {gameId : {(홈팀승) : 홈팀승의 예측투표율}}
        for (Long gameId : predictionsCounts.keySet()) { //keySet() 모든 gameId를 가져옴
            Map<String, Long> countMap = predictionsCounts.get(gameId); //gameId에해당하는 value()값 ((홈팀승)=2개) 구함
            long totalPredictions = countMap.values().stream().mapToLong(Long::longValue).sum(); // 결과값들 총개수 구하기 홈팀승 2개니 2개 get ex. 만약 홈팀승 2개 무승부 1개시 3개
            Map<String, String> percentageMap = new HashMap<>(); //{(홈팀승) : 홈팀승의 예측투표율}
            percentageMap.put("homeWin", decimalFormat.format((countMap.getOrDefault("1", 0L) / (double) totalPredictions) * 100));
            percentageMap.put("awayWin", decimalFormat.format((countMap.getOrDefault("2", 0L) / (double) totalPredictions) * 100));
            percentageMap.put("draw", decimalFormat.format((countMap.getOrDefault("0", 0L) / (double) totalPredictions) * 100));

            predictionPercentages.put(gameId, percentageMap);
        }


        return predictionPercentages;

    }

    public String checkPrediction(PredictionDto predictionDto){ //승부예측 테이블이 있는지 확인 (승부예측 중복불가)
        String result;
        PredictionDto predictionR = predictionRepository.selectByPrediction(predictionDto);
        if (predictionR == null) {
            return result = "가능";
        } else {
            return result = "중복";
        }
    }
    public void insertPrediction (PredictionDto predictionDto){ //참조키 객체지향 필요 //승부예측 입력
        predictionRepository.insert(predictionDto);
    }

    public List<PredictionDto> getPredictions (List<GameDto> gameDtoList) { // predictionDto의 gameDto 기준으로 예측 테이블 조회
        List<PredictionDto> predictionDtoList = predictionRepository.selectByGameList(gameDtoList); //여기까지 오류없음
        return predictionDtoList;
    }
}
