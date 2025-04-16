package idusw.soccerworld.domain.dto;

import lombok.Data;

@Data
public class PostReactionDto {

    private Long postReactionId;

    private MemberDto memberDto;

    private PostDto postDto;

    private String reactionType;
}
