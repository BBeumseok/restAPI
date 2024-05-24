package com.zerock.restapi.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/*
    RefreshTokenFilter의 동작 과정 중에서 여러 종류의 예외 사항이 발생하므로 이를 별도의
    예외 클래스로 분리해주도록 한다.
    AccessTokenException과 유사하게 토큰들이 없거나 문제있는 경우와 RefreshToken이 오래된 경우를 구분해서 사용한다.
 */
public class RefreshTokenException extends RuntimeException{

    private ErrorCase errorCase;

    public enum ErrorCase {
        NO_ACCESS, BAD_ACCESS, NO_REFRESH, OLD_REFRESH, BAD_REFRESH
    }

    public RefreshTokenException(ErrorCase errorCase) {
        super(errorCase.name());
        this.errorCase = errorCase;
    }

    public void sendResponseError(HttpServletResponse response) {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", errorCase.name(), "time", new Date()));

        try {
            response.getWriter().println(responseStr);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
