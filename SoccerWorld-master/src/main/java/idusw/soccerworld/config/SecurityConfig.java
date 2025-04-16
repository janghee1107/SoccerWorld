package idusw.soccerworld.config;

import idusw.soccerworld.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//security 설정 클래스, 보안 설정을 정의한다.
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MemberService memberService) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한 필요
                        .anyRequest().permitAll() //그 외 요청 모두에게 허용
                )
                .formLogin(form -> form
                        .loginPage("/member/login")//커스텀 로그인 페이지(controller를 거쳐야 함)
                        .loginProcessingUrl("/member/login")//login post 요청 url 지정
                        .usernameParameter("id")//UserDetails의 username에 해당하는 파라미터명
                        .passwordParameter("password")//UserDetails의 password에 해당하는 파라미터명
                        .defaultSuccessUrl("/main/index", true) //성공 시 이동할 페이지
                        .failureUrl("/member/login?error=login&page-type=login") //실패 시 리턴할 url
                        .permitAll()         // 로그인 페이지는 모두 접근 가능
                )
                .logout(logout -> logout
                        .logoutUrl("/member/logout")//logout post 요청 url 지정
                        .invalidateHttpSession(true)  // 세션 만료
                        .clearAuthentication(true)  // 인증 정보 제거
                        .logoutSuccessUrl("/main/index") // 로그아웃 후 갈 페이지 지정
                        .permitAll()
                )
                .userDetailsService(memberService); //담당할 UserDetailService를 상속받은 클래스 지정

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }
}
