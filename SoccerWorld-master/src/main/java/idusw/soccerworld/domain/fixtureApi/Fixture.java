package idusw.soccerworld.domain.fixtureApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Fixture {
    private String league;
    private String leagueLogo;
    private FixtureTeam home ;
    private FixtureTeam away ;
    private String status;
    private String date;
    private String round;
}
