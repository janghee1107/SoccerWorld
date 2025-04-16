package idusw.soccerworld.domain.dto;

import lombok.Data;

@Data
public class CommentReactionDto {

    private Long commentReactionId;

    private MemberDto memberDto;

    private CommentDto commentDto;

    private String reactionType;
}
