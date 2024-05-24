package com.zerock.restapi.apiMain;

public class JWTAndPreAuthorize {

    /*
        JWT와 @PreAuthorize
        JWT 기반의 인증 작업은 일반적인 세션 기반의 인증과 다르기 때문에 스프링 시큐리티에서 사용하는
        @PreAuthorize를 이용할 수 없다는 단점이 있다.

        API 서버에서는 CSRF 토큰을 사용하지 않는 경우가 많고, CustomSecurityConfig 설정과 같이 세션을 생성하지 않고,
        기존에 만들어진 세션을 사용하지도 않는 SessionCreationPolicy.STATELESS 설정을 하는 경우가 대부분이다.

        JWT 인증을 이용할 때는 JWT 안에 아이디(username, 예제는 mid)를 이용하여 인증 정보를 직접 처리해서 스프링
        시큐리티에서 활용할 수 잇도록 지정하는 방법을 생각할 수 있다.

        스프링 시큐리티에서는 'SecurityContextHolder' 라는 객체로 인증과 관련된 정보를 저장 -> 컨트롤러 등에서 이를 활용할 수 있는데
        이를 이용하면 @PreAuthorize를 이용할 수 있다.
     */

    /*
        TokenCheckFilter 수정
        JWT 토큰을 이용해서 인증 정보를 처리해야 하는 부분은 TokenCheckFilter이므로, JWT에 대한 검증이 끝난 이후에
        인증 정보를 구성해서 이를 활용하도록 구성

        1. TokenCheckFilter는 APIUserDetailsService를 이용 -> JWT의 mid(username)값으로 사용자 정보를 얻어오도록 구성
        2. TokenCheckFilter를 설정하는 CustomSecurityConfig의 설정도 변경 -> filterChain()에 APIUserDetailsService 추가
        3. TokenCheckFilter를 생성하는 부분 역시 수정한다.
        4. TokenCheckFilter() 내부에는 JWT의 mid(username)값을 이용해서 UserDetails를 구하고 이를 활용하여
            UsernamePasswordAuthenticationToken 객체를 구성


        변경된 코드에서 가장 핵심적인 부분
        UsernamePasswordAuthenticationToken 객체를 구성해서 SecurityContextHolder.getContext().setAuthentication(authentication)를
        통해 스프링 시큐리티에서 사용할 수 있도록 하는 부분

     */

    /*
        @PreAuthorize 적용
        변경된 TokenCheckFilter가 정상적으로 동작하는지 확인하기 위해
        SampleController에 @PreAuthorize 적용

        JWT로 인증하는 사용자(APIUser)는 모두 'ROLE_USER' 권한만을 가지고 있으므로, 추가된 doB는 호출이 불가능

        실제로 프로그램 실행 후 AccessToken 값을 가지고 Swagger UI에서 doA, doB를 호출할 경우 doA는 정상적으로
        호출이 가능하지만 doB는 403에러(Forbidden) 발생 -> 접근권한 금지됨(권한으로 인해 발생하는 에러)

        JWT 인증과 @PreAuthorize를 이용하는 경우 매번 호출할 때마다 'APIUserDetailsService'를 이용해서
        사용자 정보를 다시 로딩해야 하는 단점이 존재한다.
        이 과정에서 데이터베이스 호출 역시 마찬가지

        JWT를 이용한다는 의미는 이미 적절한 토큰의 소유자가 인증 완료됐다고 가정해야 하므로 가능하다면
        다시 인증 정보를 구성하는 것은 성능상 좋은 방식이 아니라고 할 수 있다.

     */

}
