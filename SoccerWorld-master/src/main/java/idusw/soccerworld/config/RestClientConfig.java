package idusw.soccerworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private String key = "32c1c11298b9489898887d2302b4fa2f";

    //기본 url과 key가 포함된 RestClient Bean 생성
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://api.football-data.org/v4")
                .defaultHeader("x-Auth-Token", key)
                .build();
    }
}
