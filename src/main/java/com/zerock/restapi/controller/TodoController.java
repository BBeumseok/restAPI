package com.zerock.restapi.controller;

import com.zerock.restapi.dto.PageRequestDTO;
import com.zerock.restapi.dto.PageResponseDTO;
import com.zerock.restapi.dto.TodoDTO;
import com.zerock.restapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
@Log4j2
public class TodoController {

    private final TodoService todoService;

    //  TodoController는 우선 JSON 문자열을 TodoDTO로 문제없이 받아들이는지 확인하도록 한다.
    //  AccessToken을 발급 후 SwaggerUI를 통해 Authorization - AccessToken값 저장 -> todo-controller로 확인
    //  JSON으로 전송된 데이터가 정상적으로 서버에 도달했는지 확인
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO) {
        log.info(todoDTO);
        Long tno = todoService.register(todoDTO);
        return Map.of("tno", tno);
    }

    //  @PathVariable을 이용해 '/api/todo/111'과 같은 경로를 처리
    @GetMapping("/{tno}")
    public TodoDTO read(@PathVariable("tno") Long tno) {
        log.info("read tno: " + tno);
        return todoService.read(tno);
    }

    //  검색과 페이징 처리
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        return todoService.list(pageRequestDTO);
    }

    //  삭제
    @DeleteMapping(value = "/{tno}")
    public Map<String, String> delete(@PathVariable("tno") Long tno) {
        todoService.remove(tno);
        return Map.of("result", "success");
    }

    //  수정
    @PutMapping(value = "/{tno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> modify(@PathVariable("tno") Long tno,
                                      @RequestBody TodoDTO todoDTO){

        //  잘못된 tno가 발생하지 못하도록
        todoDTO.setTno(tno);

        todoService.modify(todoDTO);

        return Map.of("result", "success");
    }
}
