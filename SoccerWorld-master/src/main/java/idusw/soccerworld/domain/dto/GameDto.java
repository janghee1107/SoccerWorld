package idusw.soccerworld.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDto {
    private long gameId;
    private TeamDto homeTeamDto;
    private TeamDto awayTeamDto;
    private LocalDateTime dateTime;
    private String league;
    private Integer homeScore;
    private Integer awayScore;
    private Integer result;
    private Integer round;

}
