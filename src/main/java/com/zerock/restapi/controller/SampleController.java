package com.zerock.restapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/sample")
public class SampleController {
    /*
        Config 설정 테스트를 위해 사용
        단순하게 문자열을 반환하는 기능을 작성
        SampleController의 doA()는 '/api/sample/doA' 경로로 호출이 가능하므로
        프로젝트를 실행하고 swagger-ui/index.html 에서 동작을 확인
     */

    //  변경된 TokenCheckFilter가 정상적으로 동작하는지 확인하기 위해 @PreAuthorize 적용
    @Operation(summary = "Sample GET doA")
    @GetMapping("/doA")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<String> doA() {
        return Arrays.asList("AAA", "BBB", "CCC");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/doB")
    public List<String> doB() {
        return Arrays.asList("AdminAAA", "AdminBBB", "AdminCCC");
    }



}
