package com.zerock.restapi.security.filter;

import com.zerock.restapi.security.APIUserDetailsService;
import com.zerock.restapi.security.exception.AccessTokenException;
import com.zerock.restapi.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    /*
        TokenCheckFilter 는 현재 사용자가 로그인한 사용자인지 체크하는 로그인 체크용 필터와 유사하게
        JWT 토큰을 검사하는 역할을 위해서 사용

        org.springframework.web.filter.OncePerRequestFilter 를 상속해서 구성
        OncePerRequestFilter - 하나의 요청에 대해 한 번씩 동작하는 필터로 서블릿 API 필터와 유사하다.

        TokenCheckFilter 는 JWTUtil의 validateToken() 기능을 활용해야 함
     */

    private final JWTUtil jwtUtil;

    //  APIUserDetailsService를 이용해서 JWT의 mid 값으로 사용자 정보를 불러옴
    private final APIUserDetailsService apiUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if(!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Token Check Filter ---------------------------------");
        log.info("JWTUtil: " + jwtUtil);

        //  doFilterInternal()의 내용을 수정
        //  AccessToken에 문제가 있을 때는 자동으로 브라우저에 에러 메시지를 상태 코드와 함께 전송하도록 처리
        //  JWT의 mid 값을 이용 -> UserDetails를 구하고 이를 활용 -> UsernamePasswordAuthenticationToken 객체를 구성
        try {
            Map<String, Object> payload = validateAccessToken(request);

            //  mid
            String mid = (String)payload.get("mid");

            log.info("mid: " + mid);

            UserDetails userDetails = apiUserDetailsService.loadUserByUsername(mid);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        }catch (AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }

    }

    //  AccessToken을 검증하는 validateAccessToken() 메소드를 추가하고 예외 종류에 따라서 AccessTokenException으로 처리
    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {

        String headerStr = request.getHeader("Authorization");

        if(headerStr == null || headerStr.length() < 8){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        //  Bearer 생략
        String tokenType = headerStr.substring(0, 6);
        String tokenStr = headerStr.substring(7);

        if(tokenType.equalsIgnoreCase("Bearer") == false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);

            return values;
        }catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException-------------------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch (SignatureException signatureException) {
            log.error("SignatureException---------------------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch (ExpiredJwtException expiredJwtException) {
            log.error("ExpiredJwtException----------------------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }

        /*
            validateAccessToken()에는 JWTUtil의 validateToken()을 실행해서 문제가 생기면 발생하는
            JwtException을 이용하여 예외 내용을 출력하고, AccessTokenException을 던지도록 설계
         */
    }
}
