package idusw.soccerworld.domain.dto;

import lombok.*;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private long teamId;
    private String name;
    private String league;
    private String logo;
    private String headCoach;
    private String stadium;
    private String location;
    private String clubColor;
    private String founded;
    private String website;
}
