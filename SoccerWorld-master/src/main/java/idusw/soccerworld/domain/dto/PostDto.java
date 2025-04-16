package idusw.soccerworld.domain.dto;

import lombok.Data;

@Data
public class PostDto {

    private CategoryDto categoryDto;

    private Long postId;

    private MemberDto memberDto;

    private String  title;

    private String  content;

    private int upCount = 0;

    private int downCount = 0;

    private int viewCount = 0;

    private String  regDate;

    private String  picture;

}