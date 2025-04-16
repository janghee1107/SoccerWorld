package idusw.soccerworld.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
public class LeagueService {
    private RestClient restClient;

    public LeagueService(RestClient restClient) {
        this.restClient = restClient;
    }
//
//    @Cacheable
//    //api에서 리그 하나의 순위 정보 추출
//    public List<Map<String, Object>> getLeagueStandings(int leagueId, int season) {
//        ResponseEntity<Map> data = restClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/standings")
//                        .queryParam("league", leagueId)
//                        .queryParam("season", season)
//                        .build())
//                .header("x-rapidapi-host","v3.football.api-sports.io")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .toEntity(Map.class);
//
//        List<Map<String, Object>> responseData = (List<Map<String, Object>>) data.getBody().get("response"); //맨처음 response 몸통부분 데이터가져옴
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (Map<String, Object> leagueInfo : responseData) {
//            // "league" -> "standings" -> 데이터를 가져오기
//            result = (List<Map<String, Object>>) ((Map<String, Object>) leagueInfo.get("league"))
//                    .get("standings");
//        }
//
//        System.out.println("배열확인:"+result.get(0));
//
//        return result;
//    }

    //해당 리그의 순위 정보를 api에서 가져오는 메서드
    public List<Map> getStandingsByApi(String league) {
        //generic이 지정되지 않은 map으로 받음
        Map data = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/competitions/" + league + "/standings")//파라미터로 넘어온 리그코드 사용
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {//4백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {//5백번대 예외 처리
                    throw new RestClientException("Server error: " + res.getStatusCode());
                })
                .body(Map.class);

        //table 빼내기
        List<Map> standings = (List<Map>)data.get("standings");
        List<Map> table = (List<Map>) standings.get(0).get("table");//원소 한 개의 배열로 되어 있어 0번째를 꺼낸다.

        System.out.println(table);

        return table;
    }
}
