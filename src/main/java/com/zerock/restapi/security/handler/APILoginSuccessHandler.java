package com.zerock.restapi.security.handler;

import com.google.gson.Gson;
import com.zerock.restapi.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler{

    //  JWTUtil 주입 -> 필요한 토큰들을 생성하도록 수정
    private final JWTUtil jwtUtil;

    /*
        인증 처리가 되었지만 기존의 스프링 시큐리티처럼 로그인 후 '/'와 같이 화면을 이동하는 방식으로 동작함
        원하는 작업은 JWT 문자열을 생성하는 것이므로 이에 대한 처리를 위해 인증 성공 후 처리 작업을 담당하는 
        AuthenticationSuccessHandler 를 이용하여 후처리를 진행
        
        APILoginSuccessHandler 의 동작은 APILoginFilter 와 연동되야 하므로 CustomSecurityConfig 내부에서 이를 설정
     */

    /*
        JWTUtil 을 이용하여 JWT 관련 문자열(토큰)을 만들거나 검증할 수 있다는 사실을 알았다면 이제는
        이를 언제 어떻게 활용해야 하는지 다시 저검하고 구현해야 함

        사용자가 '/generateToken' 을 POST 방식으로 필요한 정보(mid,mpw)를 전달 -> APILoginFilter 동작
        -> 인증 처리가 된 후 -> APILoginSuccessHandler 동작

     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        log.info("Login Success Handler---------------------------------------");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(authentication);
        log.info(authentication.getName());     //username

        Map<String, Object> claim = Map.of("mid", authentication.getName());

        //  Access Token 유효 기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1);
        //  Refresh Token 유효 기간 30일
        String refreshToken = jwtUtil.generateToken(claim, 30);

        Gson gson = new Gson();

        Map<String, String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken);

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().println(jsonStr);

    }
}
