package com.zerock.restapi.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {

    @Autowired
    private JWTUtil jwtUtil;

    /*
        JWT 테스트 하는 방법
        1. JWTUtil 을 이용하여 JWT 문자열 생성
        2. 생성된 문자열을 https://jwt.io 사이트를 통해 정상적인지 검사
        3. JWTUtil - validateToken()을 통해 jwt.io 사이트의 검사결과와 일치하는지 확인
     */

    @Test
    public void testGenerate() {

        Map<String, Object> claimMap = Map.of("mid", "ABCDE");

        String jwtStr = jwtUtil.generateToken(claimMap, 1);

        log.info(jwtStr);
        //  아직 JWT 생성 자체는 안되지만 application.properties 파일에 설정된 비밀키가
        //  정상적으로 로딩되는지는 확인 가능.

    }

    @Test
    public void testValidate() {
        //  테스트 코드는 이미 유효 기간이 지난 JWT 문자열을 이용하여 validateToken()을 실행

        //  유효시간이 지난 토큰
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJtaWQiOiJBQkNERSIsImlhdCI6MTcxNjQzMjQwOSwiZXhwIjoxNzE2NDMyNDY5fQ." +
                "dywXSPxjGKVVmlN1P7ad0z1nV03yJ6Spei-OBg35nKU";

        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);

        log.info(claim);

    }

    @Test
    public void testAll() {
        //  JWT 문자열을 생성해서 이를 검증하는 작업을 같이 수행하는 테스트 메소드를 작성

        String jwtStr = jwtUtil.generateToken(Map.of("mid", "AAAA", "email", "aaa@bbb.com"), 1);

        log.info(jwtStr);

        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);

        log.info("MID : " + claim.get("mid"));

        log.info("EMAIL : " + claim.get("email"));
        
        /*
            testAll()에서는 mid, email 을 이용해 JWT 문자열을 생성 -> validateToken()을 실행
            validateToken()의 리턴값에는 mid, email 그대로 들어있는 것을 확인 가능
         */
    }

}
