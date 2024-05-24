package com.zerock.restapi.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
    //  부모 클래스인 AbstractAuthenticationProcessingFilter 의 영향으로 생성자와 추상메서드를 오버라이드 해야 함
    //  설정을 위해 생성자는 public 으로 변경
    //  AbstractAuthenticationProcessingFilter 는 로그인 처리를 담당하기 때문에 다른 필터들과 달리
    //  로그인을 처리하는 경로에 대한 설정과 실제 인증 처리를 담당하는 AuthenticationManager 객체의 설정이 필수로 필요
    //  AuthenticationManager 는 CustomSecurityConfig 를 이용하여 처리
    public APILoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("APILoginFilter---------------------------------");

        //  POST 방식으로 요청이 들어올 떄 JSON 문자열을 처리하는 parseRequestJSON() 메소드를 구성 (mid, mpw 확인할 수 있도록)
        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("GET METHOD NOT SUPPORT");
            return null;
        }

        Map<String, String> jsonData = parseRequestJSON(request);

        log.info("jsonData : "+jsonData);

        /*
            Map 으로 처리된 mid, mpw 를 이용하여 로그인을 처리하는 부분은 UserNamePasswordAuthenticationToken
            인증 정보를 만들어서 다음 필터(UserNamePasswordAuthenticationFilter)에서 하도록 구성
         */
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jsonData.get("mid"),
                        jsonData.get("mpw"));

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
        //  JSON 데이터를 분석해서 mid, mpw 전달 값을 Map 으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

        //  변경된 APILoginFilter 는 POST 방식으로 동작해야 전송된 JSON 데이터를 처리하므로 static 폴더에
        //  apiLogin.html 파일을 추가
    }

}
