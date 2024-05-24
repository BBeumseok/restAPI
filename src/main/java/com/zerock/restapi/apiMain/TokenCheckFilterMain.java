package com.zerock.restapi.apiMain;

public class TokenCheckFilterMain {
    /*
        TokenCheckFilter는 '/api/'로 시작하는 모든 경로의 호출에 사용되고,
        사용자는 해당 경로에 다음과 같이 접근하게 된다.
        
        - Access Token이 없는 경우 - 토큰이 없다는 메시지 전달 필요(로그인창 또는 토큰을 발행하게 끔)
        - Access Token이 잘못된 경우(서명 혹은 구성, 기타 에러) - 잘못된 토큰이라는 메시지 전달 필요
        - Access Token이 존재하지만 오래된(expired) 값인 경우 - 토큰을 갱신하라는 메시지 전달 필요(재발행 요구 등)   
        + Access Token이 정상적인 경우
    
        위와 같이 다양한 상황을 처리하기 위해 TokenCheckFilter는 JWTUtil에서 발생하는 예외에 따른 처리를
        세밀하게 처리해야 한다.
        
        1. Access Token의 추출과 검증
        토큰 검증 단계에서 가장 먼저 할 일은 브라우저가 전송하는 Access Token을 추출하는 것
        일반적으로 Access Token의 값은 HTTP Header 중 'Authorization'을 이용해서 전달된다.
        Authorization Header는 'type + 인증값' 으로 작성
        type 값 - 'Basic ,Bearer, Digest, HOBA, Mutual' 등을 이용. 이 중에서 OAuth / JWT는 'Bearer' 타입을 이용
        
        TokenCheckFilter에서는 별도의 메소드를 이용해 Authorization Header 추출 -> Access Token 검사
        
        Access Token에 문제 발생을 대비해 별도의 exception 패키지를 구성하여 예외 클래스를 미리 정의
        
     */
}
