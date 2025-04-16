package idusw.soccerworld.service;

import idusw.soccerworld.domain.dto.MemberDto;
import idusw.soccerworld.domain.dto.MemberDetails;
import idusw.soccerworld.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    //security를 통한 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MemberDto memberDto = memberRepository.loginCheck(id);

        if (memberDto == null) {
            throw new UsernameNotFoundException("MemberDto not found with id: " + id);
        }

        return new MemberDetails(memberDto);
    }

    //회원가입 처리
    public int insertMember(MemberDto memberDto) {
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encodedPassword);
        memberDto.setRole("Client");
        return memberRepository.insertOne(memberDto);
    }

    //아이디 중복 확인
    public String selectById(String id) {
        return memberRepository.selectOneById(id);
    }

    //닉네임 중복 확인
    public String selectByNickname(String nickname) {
        return memberRepository.selectOneByNickname(nickname);
    }

    public MemberDto getMemberByMemberId (long memberId) {
        MemberDto memberDto = memberRepository.selectByMemberId(memberId);
        return memberDto;
    }
}
