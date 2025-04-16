package idusw.soccerworld.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleApiService {

    private RestClient restClient;
    public ScheduleApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<Map> getGameApiByLeagueAndDate(int leagueNum, String fromDate , String toDate){ // 축구경기 JSON 구하기

        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + leagueNum + "/matches")
                        .queryParam("dateFrom",fromDate)
                        .queryParam("dateTo",toDate)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError , (request, response2) -> {
                    throw new RestClientException("Client Error:" + response2.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError , (request, response2) -> {
                    throw new RestClientException("Server Error:" + response2.getStatusCode());
                })
                .toEntity(Map.class);
        System.out.println("경기 서비스 응답:" + response);
        return response;
    }

    public ResponseEntity<Map> getGameApiByYearSeason(int leagueNum) {
        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/" + leagueNum + "/matches")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);
        System.out.println("경기 서비스 응답:" + response);
        return response;
    }

    public Integer getGameApiCurrentMatchDay(){
        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/competitions/PL")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);
        Map responseBody = response.getBody();
        Map currentSeason = (Map) responseBody.get("currentSeason");
        return (Integer) currentSeason.get("currentMatchday");
    }

    public List<Map<String,Object>> getStandingsApi(){ //순위 api JSON 데이터 구하기
        ResponseEntity<Map> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/standings")
                        .queryParam("league", "39")
                        .queryParam("season", "2024")
                        .build())
                .header("x-rapidapi-host","v3.football.api-sports.io")
                .header("x-rapidapi-key", "73b2b917e94580c8bd9bb06ab1b77f14")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Map.class);

        List<Map<String, Object>> responseData = (List<Map<String, Object>>) response.getBody().get("response"); //맨처음 response 몸통부분 데이터가져옴
        List<Map<String, Object>> standings = new ArrayList<>();
        for (Map<String, Object> leagueInfo : responseData) {
            // "league" -> "standings" -> 데이터를 가져오기
            standings = (List<Map<String, Object>>) ((Map<String, Object>) leagueInfo.get("league"))
                    .get("standings");
        }
        System.out.println("서비스:" + standings.get(0));
        //stadings 랭킹데이터가 response안에 league안에 standings 이중배열로 되어있음

        return (List<Map<String, Object>>) standings.get(0);
    }

}
