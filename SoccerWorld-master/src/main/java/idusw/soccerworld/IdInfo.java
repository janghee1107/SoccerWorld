package idusw.soccerworld;

import java.util.Map;

public final class IdInfo {
    private static final Map<String, Integer> leagueId = Map.of(
            "EPL", 39,
            "LaLiga", 140,
            "SerieA", 135,
            "Bundesliga", 78,
            "Ligue1", 61,
            "UCL", 2
    );

    public static Map<String, Integer> getLeagueId() {
        return leagueId;
    }
}
