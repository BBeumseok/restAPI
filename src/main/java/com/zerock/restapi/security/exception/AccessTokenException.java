package com.zerock.restapi.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/*
    AccessTokenException은 발생하는 예외의 종류를 미리 enum으로 구분해 두고, 나중에 에러 메시지를
    전송할 수 있는 구조로 작성
 */
public class AccessTokenException extends RuntimeException{

    TOKEN_ERROR token_error;

    public enum TOKEN_ERROR {   //  발생할만한 에러를 지정
        UNACCEPT(401, "Token is null or too short"),    //  잘못된 접근
        BADTYPE(401, "Token type Bearer"),              //  잘못된 Token Type
        MALFORM(403, "Malformed Token"),                //  잘못된 형식의 Token
        BADSIGN(403, "BadSignatured Token"),            //  잘못된 비밀키
        EXPIRED(403, "Expired Token");                  //  토큰 유료기간 만료

        private int status;
        private String msg;

        TOKEN_ERROR(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public int getStatus() {return status;}

        public String getMsg() {return msg;}
    }

    public AccessTokenException(TOKEN_ERROR error) {
        super(error.name());
        this.token_error = error;
    }

    public void sendResponseError(HttpServletResponse response){

        response.setStatus(token_error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", token_error.getMsg(), "time", new Date()));

        try {
            response.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
