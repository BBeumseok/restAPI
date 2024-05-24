package com.zerock.restapi.apiMain;

public class JWTUtilMain {
    /*
        JWT 는 엄밀히 말해 '인코딩된 문자열'
        크게 헤더(header), 페이로드(payload), 서명(signature) 세 부분으로 작성

        헤더 - 알고리즘과 토큰 타입
        typ : 토큰 타입
        alg : 해싱 알고리즘

        페이로드 - 클레임(claim)이라고 부르는 키(key)/값(value)으로 구성된 정보들을 저장
        iss : 토큰 발급자
        sub : 토큰 제목
        exp : 토큰 만료 시간
        iat : 토크 발급 시간
        aud : 토큰 대상자
        nbf : 토큰 활성 시간
        jti : JWT 고유 식별자

        서명 - Header + Payload 의 인코딩값을 해싱 + 비밀키
        서명 부분은 비밀키를 지정해서 인코딩

        개발자의 고민은 '어떻게 JWT 를 생성하고 넘겨받은 JWT 를 확인할 수 있는가'
        => JWT 관련 라이브러리를 이용해서 처리하는데 여러 종류의 라이브러리가 존재하므로 해당 라이브러리의 문서를
            확인하면서 개발해야 함
            (io.jsonwebtoken 공식 문서 버전은 0.11.1이지만 비밀키의 길이나 기타 메소드들이 조금 다르므로
                가장 많이 사용되는 0.9.1 버전을 이용하는 것이 좋다)

     */
}
