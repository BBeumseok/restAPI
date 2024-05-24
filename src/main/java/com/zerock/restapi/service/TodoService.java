package com.zerock.restapi.service;

import com.zerock.restapi.dto.PageRequestDTO;
import com.zerock.restapi.dto.PageResponseDTO;
import com.zerock.restapi.dto.TodoDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface TodoService {

    //  등록 처리 메소드
    Long register(TodoDTO todoDTO);

    //  조회와 목록 처리
    TodoDTO read (Long tno);

    //  서비스 계층 구현
    PageResponseDTO<TodoDTO> list (PageRequestDTO pageRequestDTO);

    //  삭제
    void remove(Long tno);

    //  수정
    void modify(TodoDTO todoDTO);

}
