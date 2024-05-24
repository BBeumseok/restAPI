package com.zerock.restapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APIUser {

    @Id
    private String mid;

    private String mpw;

    public void changePw(String mpw){
        this.mpw = mpw;
    }

    /*
        API 서버에 호출하는 처리에 문제가 없고 @RestController 동작을 확인했다면
        API 서버를 통해서 토큰들을 얻을 수 있는 사용자들에 대한 처리를 진행.

        APIUser - 일반 웹 서비스와 달리 Access Key를 발급받을 때 자신의 mid, mpw를 이용하므로
        다른 정보들 없이 구성 (스프링 시큐리티에서 사용하는 권한으로 예제에서는 'ROLE_USER'를 추가해서 사용할 것임)
     */
}
