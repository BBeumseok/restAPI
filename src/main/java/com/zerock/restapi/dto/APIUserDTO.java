package com.zerock.restapi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class APIUserDTO extends User {  //  스프링 시큐리티 - User 클래스를 상속

    private String mid;
    private String mpw;

    //  User 클래스에 username, password, authorities 세 개를 가지고 사용자를 확인
    public APIUserDTO(String username, String password, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mid = username;
        this.mpw = password;
    }

}
