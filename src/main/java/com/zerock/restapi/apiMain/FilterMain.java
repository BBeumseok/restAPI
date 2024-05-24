package com.zerock.restapi.apiMain;

public class FilterMain {

    /*
        토큰 인증을 위한 시큐리티 필터
        스프링 시큐리티는 수많은 필터로 구성되어 있고, 이를 이용하여 컨트롤러에 도달하기 전에 필요한 인증 처리를
        진행할 수 있음.

        예제에서는 다음과 같은 기능 구현에 필터를 이용
        - 사용자가 자신의 아이디(mid)와 패스워드(mpw)를 이용하여 Access Token, Refresh Token을 얻으려는 단계를 구현
        - 사용자가 Access Token을 이용해서 컨트롤러를 호출하고자 할 때 인증과 권한을 체크하는 기능을 구현

        사용자의 아이디와 패스워드를 이용해 JWT 문자열을 발행하는 기능은 컨트롤러를 이용할 수도 있지만,
        스프링 시큐리티의 AbstractAuthenticationProcessingFilter 클래스를 이용하면 좀 더 완전한 분리가 가능하다

        APILoginFilter 라는 필터를 이용해 인증 단계를 처리 -> 인증에 성공 시 Access Token, Refresh Token을 전송하도록 구성

     */

}
