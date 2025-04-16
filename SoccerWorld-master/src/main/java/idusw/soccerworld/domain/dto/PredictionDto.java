package idusw.soccerworld.domain.dto;

import lombok.Data;

@Data
public class PredictionDto {
    private long predictionId;
    private MemberDto memberDto;
    private GameDto gameDto;
    private int result;
}
