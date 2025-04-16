package idusw.soccerworld.domain.fixtureApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FixtureTeam {
    private int id;
    private String name;
    private String logo;
    private Integer goals;
    private String winner;
}
