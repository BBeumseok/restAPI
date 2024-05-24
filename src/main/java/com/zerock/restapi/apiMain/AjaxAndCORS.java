package com.zerock.restapi.apiMain;

public class AjaxAndCORS {
    /*
        Ajax와 CORS
        API 서버에서는 JSON 데이터만 주고받는 방식이기 때문에 실제로 화면이 존재하지 않음.
        실제 화면은 별도의 서버를 이용해서 처리하거나 리액트, Vue.js 등을 이용하는
        SPA(Single Page Application) 방식으로 구현해서 물리적으로 분리되어 있는 서버나 프로그램에서 Ajax로 호출

        다른 서버에서 Ajax를 호출하면 '동일 출처 정책(same-origin policy)'을 위반하게 되며, Ajax 호출은
        정상적으로 이루어지지 않는다.

        '동일 출처 정책(Same-origin policy)'은 웹 브라우저 보안을 위해 프로토콜(Protocol), 호스트(Host), 포트(Port)가 같은
        서버로만 Ajax 요청을 주고받을 수 있도록 한 정책으로 Ajax를 이용해서 다른 서버의 자원들을 마음대로 사용하는 것을
        막기 위한 보안 조치이다.

        Ajax 호출이 '동일 출처 정책'으로 인해 제한받기 때문에 해결하기 위해서는 'CORS(Cross Origin Resource Sharing)'
        처리가 필요하다.
        CORS 처리를 하게 되면 Ajax 호출 서버와 API 서버가 다른 경우에도 접근과 처리를 허용할 수 있다.
     */

    /*
        CORS 처리가 필요한 상태 확인
        실제로 어떤 문제가 있는지 확인하기 위해서는 현재의 프로젝트가 실행되는 환경(API 서버)에서 다른 포트로
        별도의 서버(웹 서버)를 구성하고, 어떤 문제가 생기는지 확인해야 함.

        작성하는 새로운 프로젝트는 API 서버가 실행되는 상황에서 추가로 프로젝트를 구성하는 방식
     */

    /*
        Nginx 웹 서버
        별도의 서버를 구성하는 방법 중 Nginx 서버를 세팅해서 html 파일들을 서비스하고, Ajax를 이용해 JWT를 사용
        해보도록 한다. (https://nginx.org/en/download.html 에서 오픈소스로 제공하는 웹 서버 다운가능)

        Nginx 시작과 종료
        다운 -> C: 드라이브 아래 압축풀기 -> 생성된 폴더와 파일 확인
        html 폴더 내부에는 index.html과 50x.html이 존재하는 것을 확인할 수 있다.
        Nginx가 시작되면 80 포트를 기본으로 동작하는데 이때 가장 먼저 사용할 수 있는 파일이 index.html
        Nginx의 시작과 종료는 해당 폴더에서 'nginx.exe' 파일을 이용하여 처리

        Nginx Command
        1. nginx -s stop : 서버 즉시 종료 (강제 종료에 가까움, 비추천)
        2. nginx -s quit : 서버 종료 (완만한 종료)
        3. nginx -s reload : 설정 변경과 같은 작업 후 재시작
        4. nginx -s reopen : 로그 파일 재오픈

        Nginx 실행 방법
        1. 명령 프롬프트를 이용하여 해당 폴더로 이동
        nginx 폴더 안에서 마우스 우클릭하여 명령창 키는 것도 가능 - Shift+우클릭 (PowerShell 불가, 반드시 명령프롬프트(cmd)로 실행
        만약 폴더 내에서 우클릭으로 명령창을 켜는 기능이 없다면 폴더 위쪽 경로창에 cmd를 치면 명령 프롬프트가 실행 된다.

        2. cmd 창에서 start nginx 커맨드를 입력하여 서버를 실행
        3. 서버 실행 후에는 브라우저로 'http://localhost'를 호출해서 index.html이 서비스 되는 것을 확인

        주의점 : nginx 실행 전 현재 프로젝트 서버가 반드시 켜져 있어야 '동일 정책 위반' 문제점을 확인할 수 있음
     */

    /*
        html 폴더 편집
        실제 HTML 파일들의 내용은 html 폴더에 위치하므로 예제에서 작성했던 파일들을 html 폴더에 넣고 편집할 필요가 있다.
        ( 프로젝트 내 resources - static 폴더에 있는 html 파일 복사하여 nginx - html 폴더 안에 붙여넣기 )
     */

    /*
        Nginx 서버를 실행 후 'http://localhost/apiLogin.html' 을 실행해서 [generate Token] 버튼을 누르면 문제 발생
        발생하는 문제는 Ajax 호출에 사용하는 CORS 문제와 GET 방식이 아닌 POST 방식을 이용할 때 발생하는 Preflight 문제이다.

        Preflight 요청(사전 요청)
        Ajax는 GET/POST/HEAD 방식의 요청을 'Simple Request' 라고 하고, 여기에 서버로 전송하는 Conten-Type이
        'application/x-www-form-urlencoded, multipart/form-data, text/plan'인 경우에는 Ajax의 호출을 허용

        하지만 예제와 같이 'Custom Header'를 이용하거나 Content-Type이 다른 경우에는 'Preflight Request' 라는 것을 실행한다.
     */

    /*
        CORS 문제 해결
        Ajax의 '동일 출처 정책'을 해결하는 방법에는 여러 가지가 존재하는데 예를 들어 브라우저에서 직접 서버를 호출하는 대신에
        현재 서버 내 다른 프로그램을 이용해서 API 서버를 호출하는 프록시(proxy - 대리자) 패턴을 이용하거나, JSONP와 같이
        JSON이 아니라 순수한 JS 파일을 요청하는 방식 등이 있다.

        가장 권장되는 해결책은 서버에서 CORS 관련 설정으로 해결하는 것.
        서버에서 CORS 설정은 주로 필터(Filter)를 이용하여 브라우저의 응답 메시지에 해당 호출이 문제 없었다는 헤더 정보들을
        같이 전송하는 방식입니다.
        
        스프링 부트에서는 이런 상황들을 해결하기 위해 웹 관련 설정을 조정하는 방식을 이용하거나, @CrossOrigin 어노테이션을
        이용해서 처리할 수 있다. (@CrossOrigin의 경우 3버전에서는 사용 불가)
        스프링 시큐리티 필터들의 설정은 config - CustomSecurityConfig에 설정을 추가하는 방식으로 작성
     */

}
