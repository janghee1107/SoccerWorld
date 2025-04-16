package idusw.soccerworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SoccerWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoccerWorldApplication.class, args);
    }

}
