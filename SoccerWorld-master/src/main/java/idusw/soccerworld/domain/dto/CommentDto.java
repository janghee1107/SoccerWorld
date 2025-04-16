package idusw.soccerworld.domain.dto;

import lombok.Data;

@Data
public class CommentDto {

    private Long commentId;

    private TeamDto teamDto;

    private PlayerDto playerDto;

    private MemberDto memberDto;

    private PostDto postDto;

    private int replyId;

    private String content;

    private int upCount = 0;

    private int downCount = 0;

    private String regDate;


}
