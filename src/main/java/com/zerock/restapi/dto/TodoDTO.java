package com.zerock.restapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    //  TodoDTO는 LocalDate와 boolean을 이용하기 때문에 JSON 처리 시에 주의해야 한다.

    private Long tno;
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate dueDate;

    private String writer;
    private boolean complete;


}
