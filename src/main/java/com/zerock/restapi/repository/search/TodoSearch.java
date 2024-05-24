package com.zerock.restapi.repository.search;

import com.zerock.restapi.domain.Todo;
import com.zerock.restapi.dto.PageRequestDTO;
import com.zerock.restapi.dto.TodoDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<TodoDTO> list(PageRequestDTO pageRequestDTO);
}
