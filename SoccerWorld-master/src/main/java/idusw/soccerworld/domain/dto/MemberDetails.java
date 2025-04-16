package idusw.soccerworld.domain.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;

@Getter
//security를 위한 인증용 유저 정보 객체
public class MemberDetails implements UserDetails {
    private final String username;
    private final String password;
    private final String role;
    private final Long memberId;
    private final String name;
    private final int gender;
    private final Date birthday;
    private final String nickname;
    private final Long teamId;
    private final int point;

    //member에서 데이터 가져옴
    public MemberDetails(MemberDto memberDto) {
        this.username = memberDto.getId();
        this.password = memberDto.getPassword();
        this.role = memberDto.getRole();
        this.memberId = memberDto.getMemberId();
        this.name = memberDto.getName();
        this.gender = memberDto.getGender();
        this.birthday = memberDto.getBirthday();
        this.nickname = memberDto.getNickname();
        this.teamId = memberDto.getTeamId();
        this.point = memberDto.getPoint();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
