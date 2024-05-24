package com.zerock.restapi.repository;

import com.zerock.restapi.domain.Todo;
import com.zerock.restapi.dto.PageRequestDTO;
import com.zerock.restapi.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    //  테스트 코드를 이용하여 100개의 더미 데이터를 추가하다록 한다.
    @Test
    public void testInsert() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Todo todo = Todo.builder()
                    .title("Todo..."+i)
                    .dueDate(LocalDate.of(2024, (i%12)+1, (i%30)+1))
                    .writer("user"+(i%10))
                    .complete(false)
                    .build();

            todoRepository.save(todo);

        });
    }   //end method

    //  검색 조건 테스트
    @Test
    public void testSearch() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .from(LocalDate.of(2024, 05, 24))
                .to(LocalDate.of(2024, 12, 31))
                .build();

        Page<TodoDTO> result = todoRepository.list(pageRequestDTO);

        result.forEach(todoDTO -> log.info(todoDTO));
    }
}
