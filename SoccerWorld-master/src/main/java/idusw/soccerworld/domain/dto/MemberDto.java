package idusw.soccerworld.domain.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class MemberDto {
    private Long memberId;
    private String id;
    private String password;
    private String name;
    private int gender;
    private Date birthday;
    private String nickname;
    private Long teamId;
    private int point;
    private String role;
}
