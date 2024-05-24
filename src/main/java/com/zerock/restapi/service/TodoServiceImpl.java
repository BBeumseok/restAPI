package com.zerock.restapi.service;

import com.zerock.restapi.domain.Todo;
import com.zerock.restapi.dto.PageRequestDTO;
import com.zerock.restapi.dto.PageResponseDTO;
import com.zerock.restapi.dto.TodoDTO;
import com.zerock.restapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(TodoDTO todoDTO) {     //  등록 기능 구현

        Todo todo = modelMapper.map(todoDTO, Todo.class);
        Long tno = todoRepository.save(todo).getTno();

        return tno;
    }

    @Override
    public TodoDTO read(Long tno) {     //  조회와 목록 처리 기능 구현
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        return modelMapper.map(todo, TodoDTO.class);
    }

    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {   //  페이징 처리
        //  PageResponseDTO를 Page<TodoDTO> 타입으로 변환
        Page<TodoDTO> result = todoRepository.list(pageRequestDTO);

        return PageResponseDTO.<TodoDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.toList())
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public void modify(TodoDTO todoDTO) {   //  수정 기능
        Optional<Todo> result = todoRepository.findById(todoDTO.getTno());

        Todo todo = result.orElseThrow();

        todo.changeTitle(todoDTO.getTitle());
        todo.changeDueDate(todoDTO.getDueDate());
        todo.changeComplete(todoDTO.isComplete());

        todoRepository.save(todo);
    }

    @Override
    public void remove(Long tno) {      //  삭제 기능
        todoRepository.deleteById(tno);
    }
}
