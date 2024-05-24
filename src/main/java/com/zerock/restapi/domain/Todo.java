package com.zerock.restapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tbl_todo_api")
public class Todo {
    /*
        TodoEntity를 작성하고, Todo에 대해 Querydsl이 사용하는 QTodo가 생성되는지 확인하도록 한다.
        오른쪽에 Gradle 탭 - Tasks - other - compileJava 우클릭하여 Run 실행 후
        프로젝트 하위 폴더에 build 폴더 - classes - domain에 QTodo가 생성된지 확인
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private String title;

    private LocalDate dueDate;

    private String writer;

    private boolean complete;

    public void changeComplete(boolean complete) {
        this.complete = complete;
    }

    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void changeTitle(String title) {
        this.title = title;
    }


}
