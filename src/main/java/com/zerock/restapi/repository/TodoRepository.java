package com.zerock.restapi.repository;

import com.zerock.restapi.domain.Todo;
import com.zerock.restapi.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {


}
