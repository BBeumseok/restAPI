package com.zerock.restapi.apiMain;

public class RefreshTokenMain {

    /*
        Refresh Token 처리
        만료된 토큰이 전송되는 경우 사용자는 다시 API 서버에 Access Token, Refresh Token 갱신을 요구

        Client -> API Server    만료된 Access Token, Refresh Token 재발행 요청
        API Server -> Client    Generate New Token
        Client -> API Server    New Access Token, Refresh Token 전달

        '/refreshToken'에는 주어진 토큰이 다음과 같은 검증 과정으로 동작하도록 구성
        - Access Token이 존재하는지 확인
        - Refresh Token의 만료 여부 확인
        - Refresh Token의 만료 기간이 지났다면 다시 인증을 통해서 토큰들을 발급받아야 함을 전달

        Refresh Token 이용 과정 중 다음과 같은 상황들이 발생할 수 있음
        - Refresh Token의 만료 기간이 충분히 남아있으므로 Access Token만 새로 만들어지는 경우
        - Refresh Token 자체도 만료 기간이 얼마 안 남아서 Access Token과 Refresh Token 모두 새로 만들어야 하는 경우
     */

    /*
        RefreshTokenFilter를 호출하는 작업을 좀 더 간단하게 하기 위해서는 html 파일을 수정해서
        테스트가 가능하도록 수정해야 함.
     */

    /*
        Refresh Token 구현과 예외처리
        RefreshTokenFilter의 내부 구현은 다음과 같은 순서로 처리됩니다.

        - 전송된 JSON 데이터에서 accessToken과 refreshToken을 추출
        - accessToken을 검사해서 토큰이 없거나 잘못된 토큰인 경우 에러 메시지 전송
        - refreshToken을 검사해서 토큰이 없거나 잘못된 토큰 혹은 만료된 토큰인 경우 에러 메시지 전송
        - 새로운 accessToken 생성
        - 만료 기한이 얼마 남지않은 경우 새로운 refreshToken 생성
        - accessToken과 refreshToken 전송
     */

    /*
        AccessToken 검증을 TokenFilter 클래스에 구현
        checkAccessToken()이라는 별도의 메서드로 처리

        Refresh Token의 경우도 검사가 필요한데, RefreshToken이 존재하는지와 만료일이 지났는지를 확인하고
        새로운 토큰 생성을 위해서 mid(username) 값을 얻어두도록 한다.
     */

    /*
        새로운 Access Token 발행
        토큰 검증 단계가 끝나면 새로운 토큰들을 발행해주어야 한다.
        - Access Token은 무조건 새로 발행
        - Refresh Token은 만료일이 얼마 남지 않은 경우에 새로 발행

     */
}
