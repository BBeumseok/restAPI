package com.zerock.restapi.repository;

import com.zerock.restapi.domain.APIUser;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class APIUserRepositoryTests {

    @Autowired
    private APIUserRepository apiUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //  테스트 코드로 여러 명의 API 사용자를 미리 생성
    @Test
    public void testInserts() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            APIUser apiUser = APIUser.builder()
                    .mid("apiUser" + i)
                    .mpw( passwordEncoder.encode("1111") )
                    .build();

            apiUserRepository.save(apiUser);
        });
    }
}
