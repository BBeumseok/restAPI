package com.zerock.restapi.security.filter;

import com.google.gson.Gson;
import com.zerock.restapi.security.exception.RefreshTokenException;
import com.zerock.restapi.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {  //  RefreshTokenFilter 생성
    //  RefreshTokenFilter는 토큰 갱신에 필요한 경로('/refreshToken')와 JWTUtil을 주입받도록 설계
    private final String refreshPath;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if(!path.equalsIgnoreCase(refreshPath)){
            log.info("skip refresh token filter.....");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Refresh Token Filter... run...................1");

        //  토큰 검증
        //  전송된 JSON에서 accessToken과 refreshToken을 얻어온다.
        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

        //  checkAccessToken() : 예외 발생 시 메시지를 전송하고 메소드의 실행을 종료
        try {
            checkAccessToken(accessToken);

        }catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
        }

        Map<String, Object> refreshClaims = null;

        //  checkRefreshToken() 처리 부분 추가
        try {
            refreshClaims = checkRefreshToken(refreshToken);
            log.info(refreshClaims);

            //  Refresh Token의 유효 기간이 얼마 남지 않은 경우
            Long exp = (Long) refreshClaims.get("exp");

            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);

            Date current = new Date(System.currentTimeMillis());

            //  만료시간과 현재 시간의 간격 계산
            //  만일 3일 미만인 경우에는 Refresh Token도 다시 생성
            long gapTime = (expTime.getTime() - current.getTime());

            log.info("--------------------------------------------");
            log.info("current: " + current);
            log.info("expTime: " + expTime);
            log.info("gap: " + gapTime);

            String mid = (String)refreshClaims.get("mid");

            //  이 상태까지 오면 무조건 Access Token은 새로 생성
            String accessTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 1);
            String refreshTokenValue = tokens.get("refreshToken");

            //  RefreshToken이 3일도 안 남았다면
            if(gapTime < (1000 * 60 * 60 * 24 * 3) ) {
                log.info("new Refresh Token required..............");
                refreshTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 30);
            }

            log.info("Refresh Token result ------------------------");
            log.info("accessToken: " + accessTokenValue);
            log.info("refreshToken: " + refreshTokenValue);

            //  새로운 토큰들을 생성한 후 sendTokens()를 호출
            sendTokens(accessTokenValue, refreshTokenValue, response);

        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;     //  더이상 실행할 코드가 없음
        }

    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
        //  JSON 데이터를 분석해서 mid, mpw 전달 값을 Map으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);
        }catch(IOException e){
            log.error(e.getMessage());
        }
        return null;
    }
    
    //  accessToken 검증은 checkAccessToken()이라는 별도의 메소드로 처리
    //  문제가 생기면 RefreshTokenException을 전달한다.
    //  AccessToken의 만료기간이 지났으므로 log만 출력하도록 작성
    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try{
            jwtUtil.validateToken(accessToken);
            
        }catch(ExpiredJwtException expiredJwtException){
            log.info("Access Token has expired");
            
        }catch (Exception exception){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    /*
        RefreshToken의 경우도 검사가 필요함
        Refresh Token이 존재하는지와 만료일이 지났는지를 확인하고, 새로운 토큰 생성을 위해서 mid 값을 얻어두도록 한다.
        checkRefreshToken()을 생성하여 문제 발생 시 RefreshTokenException을 발생하고, 정상이라면 토큰 내용물들을
        Map으로 반환하도록 구성
     */
    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {

        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        }catch(ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        }catch(MalformedJwtException malformedJwtException){
            log.info("MalformedJwtException-------------------------");
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }catch(Exception exception) {
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return null;
    }

    //  최종적으로 만들어진 토큰들을 전송하는 sendTokens()를 작성하고 토큰들을 이용해서 메시지를 전송
    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue, "refreshToken", refreshTokenValue));

        try {
            response.getWriter().println(jsonStr);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
