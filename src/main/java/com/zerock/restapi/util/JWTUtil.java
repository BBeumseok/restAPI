package com.zerock.restapi.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {
    /*
        JWTUtil 에서 필요한 기능은 크게 JWT 문자열을 생성하는 기느인 generateToken()과
        토큰을 검증하는 validateToken() 기능
     */


    //  토큰 생성
    @Value("hello1234567890abcdefghijklmnopq")  //application properties 에서 추가하여 사용
    private String key;

    public String generateToken(Map<String, Object> valueMap, int days) {

        log.info("generateKey..." + key);

        //  Header - alg, tokenType
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //  Payload - key/value
        Map<String, Object> payload = new HashMap<>();
        payload.putAll(valueMap);

        //  테스트 시에는 짧은 유효기간으로 설정
        //  int time = (1) * days;      //  테스트는 분단위로 나중에 60*24 (일)단위변경
        int time = (60*24) * days;      //  정상인지 확인을 위해 유효기간을 일(day) 단위로 변경

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payload)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;
    }

    //  토큰 검증
    public Map<String, Object> validateToken(String token) throws JwtException {
        /*
            JWTUtil 을 이용하여 JWT 문자열 검증 시 가장 중요한 부분은 여러 종류의 예외가 발생하고,
            발생한 예외를 JwtException 이라는 상위 타입의 예외로 던지도록 구성하는 것

            검증은 JWT 문자열 자체의 구성이 잘못되었거나, JWT 문자열의 유효 시간이 지났거나, 서명에 문제가 있는 등 여러 문제가
            발생할 수 있음    => 추가된 라이브러리의 Jwts.parser()를 이용해서 처리
         */

        Map<String, Object> claim = null;

        claim = Jwts.parser()
                .setSigningKey(key.getBytes()).build()  //  Set key
                .parseSignedClaims(token)               //  파싱 및 검증, 실패 시 에러
                .getBody();


        return claim;
    }

}
