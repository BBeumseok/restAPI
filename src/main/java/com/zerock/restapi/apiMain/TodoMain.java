package com.zerock.restapi.apiMain;

public class TodoMain {

    /*
        Ajax로 JSON 데이터를 교환하고, JWT로 인증 정보를 처리할 수 있게 되었다면
        이를 활용하는 Todo 서비스를 작성하도록 한다.

        Todo 서비스는 다음과 같은 경로와 메소드를 이용하도록 구성
        모든 경로의 호출에는 JWT를 이용하도록 한다.
            경로                메소드           파라미터                                                   설명
        '/api/todo/'        Method=POST     Parameter=JSON                                          신규 Todo 입력
        '/api/todo/list'    Method=GET      Parameter=size, page, from, to, keyword                 PageRequestDTO를 JSON으로 만든 결과
        '/api/todo/{tno}'   Method=GET                                                              특정 Todo 조회
        '/api/todo/{tno}'   Method=PUT      Parameter=JSON                                          특정 Todo 수정
        '/api/todo/{tno}'   Method=DELETE                                                           특정 Todo 삭제
     */

    /*
        1. Todo API 서비스는 검색 기능과 페이징 처리 등을 지원하기 위해 가장 먼저 domain 관련 처리를 작성해야 함
        2. build.gradle - Querydsl 설정 추가
        3. TodoEntity 클래스 생성 -> 작성된 TodoEntity에 대해 Querydsl이 사용하는 QTodo가 생성되는지 확인
        4. TodoRepository Test -> 테스트 코드를 통해 여러 개의 더미데이터 추가 -> 데이터베이스에 추가된 결과 확인
        5. TodoDTO 클래스 생성 -> LocalDate, boolean을 이용할 경우 JSON 처리 시에 주의해야 함
        6. TodoService/TodoServiceImpl 생성
        7. TodoController 생성 - API 서비스의 경우 @RestController를 이용하여 데이터를 처리하도록 구성
        8. TodoController에서는 JSON 문자열을 TodoDTO로 문제없이 받아들이는지를 우선적으로 확인
            -> JSON으로 전송된 데이터가 정상적으로 서버에 도달했는지 log를 통해 확인
     */

    /*
        Todo API 서비스 페이징 처리
        1. TodoRegister(등록) 기능 구현
        2. TodoRead(조회) 기능 구현
        3. 목록을 처리하기 위해서는 PageRequestDTO, PageResponseDTO 클래스를 추가해 둔 상태에서 개발을 시작
           PageRequestDTO는 Todo의 검색 조건을 고려해 항목을 추가
           - 기간별 검색 조건을 고려해서 LocalDate로 from, to
           - 완료 여부를 고려해서 complete
     */

    /*
        Querydsl을 이용한 검색 조건 처리
        PageRequestDTO의 내용을 고려해 검색 조건에 따라 동적으로 검색 조건이 만들어지므로
        Querydsl을 이용하기 위해서 추가적인 개발을 준비

        repository 패키지 - search 패키지 추가
        TodoSearch 인터페이스, TodoSearchImpl 구현체 추가 (클래스 이름 주의)
        TodoSearchImpl - QuerydslRepositorySupport 클래스를 상속하고, TodoSearch 인터페이스를 구현하도록 선언

        기존 TodoRepository 인터페이스에 TodoSearch 인터페이스 추가
        TodoSearchImpl에는 QTodo를 이용하여 from, to를 이용하는 검색조건이나 complete에 해당하는
        검색 조건을 구현   
        => 기능 구현 후 테스트를 통해 검색 조건에 대한 테스트 -> testSearch()를 실행하면 Todo의 dueDate를 기준으로 쿼리가 작성된 것을 확인 가능
     */
    
    /*
        Todo Service 구현
        TodoService, TodoServiceImpl에서는 TodoRepository의 Page<TodoDTO>를 PageRequestDTO 타입으로 변환

        Todo Controller 구현
        list() 메소드를 작성해서 검색과 페이징을 처리
     */

    /*
        Todo 수정과 삭제
        Todo 삭제와 달리 수정 가능한 부분은 '제목(title), 완료 여부(complete), 만료일(dueDate)'만 가능
        TodoService, TodoServiceImpl에서는 수정과 삭제 기능을 구현.

        Controller에서의 수정/삭제
        삭제는 Delete 방식으로 처리하고, 수정은 PUT 방식으로 처리하도록 구현
        
        수정과 삭제 테스트 시 현재 데이터베이스에 존재하는 번호를 이용하여 진행
        (Access Token을 지정한 후에 테스트를 해야 함)
        
        SwaggerUI를 통해 삭제 진행 후 제대로 처리됐는지 데이터베이스를 통해 확인
        수정 작업은 SwaggerUI PUT 탭에 tno값과 JSON 문자열을 작성해서 전송
     */
}
