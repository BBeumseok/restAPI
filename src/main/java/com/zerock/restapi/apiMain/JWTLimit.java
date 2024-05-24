package com.zerock.restapi.apiMain;

public class JWTLimit {
    /*
        JWT의 한계
        JWT를 이용해서 자원을 보호하는 방식은 태생적으로 '문자열' 이라는 한계가 존재
        예를 들어 외부의 공격자가 RefreshToken을 탈취한 상황이라면 얼마든지 새로운 AccessToken을 생성할 수 있기 때문에
        안전하지 않음.

        보완하기 위해서는 Access Token과 Refresh Token을 데이터베이스에 보관하고, 토큰을 갱신할 때
        데이터베이스의 값과 비교하는 방법을 이용할 수 있다.

        JWT를 안전하게 하기 위한 대부분의 방법들이 근본적으로 공격자가 Access Token과 Refresh Token을 탈취한 경우
        최소한 1회 이상은 작업이 가능하다는 점에서 모든 보완책이 완벽할 수는 없다.
        (인터넷에서 공격자가 사용자의 아이디, 비밀번호를 탈취하는 상황과 큰 차이가 없음)
     */

    /*
        브라우저에서 JWT 확인
        API 서버를 이용하는 구조는 브라우저에서 HTTP로 JWT 토큰을 전송하고, 필요한 자원에 접근하는 방식을 이용
        예제의 경우 Access Token과 Refresh Token 등의 활용을 우선적으로 같은 서버 환경에서 먼저
        체크하고, 이후에 별도의 서버를 구축해서 확인하도록 한다.
     */

    /*
        JWT를 이용하는 시나리오
        브라우저에서 JWT를 이용하는 시나리오는 다음과 같다.

        1단계 - 토큰 생성과 저장
        (예제의 경우) 당연하게 '/generateToken' 을 호출해서 서버에서 발행한 Access Token과 Refresh Token을 받는 단계이다.
        브라우저는 받은 토큰들을 저장해 두고 필요할 때마다 토큰들을 찾아서 사용하도록 구성해야 한다.

        2단계 - Access Token을 이용한 접근
        브라우저에서 '/api/sample/doA' 를 호출할 때 가지고 있는 Access Token을 같이 전달했을 경우 정상적인
        결과가 나오는지를 확인한다.

        3단계 - Refresh 처리
        Access Token의 유효 기간이 만료되는 상황에 대한 처리
        Access Token의 유효 기간이 만료되면 서버에서는 에러 메시지 전송을 하는데 이 메시지를 판단해서 브라우저는
        Refresh Token으로 다시 새로운 Access Token을 받고, 원래 의도했던 작업을 수행해야 한다.
        (이 과정은 사용자가 참여하지 않고, 자동으로 처리되어야 하기 때문에 'Silent Refreshing' 이라고 하기도 함)

        4단계 - 만료된 Refresh Token
        Refresh Token 마저도 만료된 상황에 대한 처리
        Refresh Token이 만료되면 새로운 Access Token을 발행할 수 없기 때문에 사용자에게 1단계부터 다시
        시작해야 함을 알려주어야 한다.

     */

}
