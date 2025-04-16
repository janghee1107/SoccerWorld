package idusw.soccerworld.domain.dto;

import lombok.*;

import java.util.Date;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private long playerId;
    private TeamDto teamDto;
    private String name;
    private String nation;
    private String position;
    private Date birthDay;
}
