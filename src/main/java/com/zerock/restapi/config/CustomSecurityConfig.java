package com.zerock.restapi.config;

import com.zerock.restapi.security.APIUserDetailsService;
import com.zerock.restapi.security.filter.APILoginFilter;
import com.zerock.restapi.security.filter.RefreshTokenFilter;
import com.zerock.restapi.security.filter.TokenCheckFilter;
import com.zerock.restapi.security.handler.APILoginSuccessHandler;
import com.zerock.restapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Log4j2
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {
    /*
        이전 Spring Boot 패키지에 있는 Config 설정들(CustomSecurityConfig, RootConfig, SwaggerConfig)을 불러오는데
        CustomSecurityConfig 클래스는 에러가 발생하게 된다.
        토큰 비활성화, 세션을 사용하지 않을 것을 지정
     */

    //  주입 (AuthenticationManager 객체를 생성하기 위해)
    private final APIUserDetailsService apiUserDetailsService;
    //  JWTUtil 주입
    private final JWTUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("---------------web configure-----------------");

        //  정적 리소스 필터링 제외
        return (web -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                ));
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        //  AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(apiUserDetailsService)
                        .passwordEncoder(passwordEncoder());

        //  Get AuthenticationManager
        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();

        //  반드시 필요
        http.authenticationManager(authenticationManager);

        //  APILoginFilter
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");   //APILoginFilter 경로 지정
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        //  APILoginSuccessHandler
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil);    //  jwtUtil 주입
        //  SuccessHandler 세팅
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        //  APILoginFilter 위치 조정 (username, password 처리를 하는 UsernamePasswordAuthenticationFilter 의 앞쪽으로 동작하도록 설정)
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        //  api로 시작하는 모든 경로는 TokenCheckFilter 동작
        //  tokenCheckFilter() 부분에 apiUserDetailsService 추가
        http.addFilterBefore(
                tokenCheckFilter(jwtUtil, apiUserDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );

        //  refreshToken 호출 처리
        //  RefreshTokenFilter는 다른 JWT 관련 필터들의 동작 이전에 할 수 있도록 TokenCheckFilter 앞으로 배치
        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil),
                TokenCheckFilter.class);


        //  1. CSRF 토큰의 비활성화
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        //  2. 세션을 사용하지 않음
        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ));

        //  CORS 관련 설정  - HttpSecurity 객체에 반영
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        return http.build();
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil, APIUserDetailsService apiUserDetailsService) {
        return new TokenCheckFilter(jwtUtil, apiUserDetailsService);    //  apiUserDetailsService 추가
    }


    //  cors() 관련 설정을 객체로 생성
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //  접근할 URL을 지정하여 처리  "*"는 모든 주소로의 접근 허용. 대상 지정하면 지정 대상만 접근 가능
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:9010", "http://localhost"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //  "/**" : 하위 모든 폴더 포함
        return source;
    }

    /*
        CORS 관련 설정이 끝나면 프로젝트 API서버와 NginX 서버를 둘 다 실행시킨 상태에서 브라우저 열기
        localhost/apiLogin.html -> API 서버의 '/apiLogin.html이 정상적으로 호출되는지 확인 (관리자 도구 - Network 탭 확인)

        정상적인 토큰이 발행되었다면 'sendJWT.html'을 이용하여 정상으로 호출이 가능하지 살펴봐야 함
        만일 Refresh Token이 만료되었다면 '/refreshToken'도 호출되는 것을 확인할 수 있음
     */

}
