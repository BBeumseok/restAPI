package com.zerock.restapi.security;

import com.zerock.restapi.domain.APIUser;
import com.zerock.restapi.dto.APIUserDTO;
import com.zerock.restapi.repository.APIUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {  //UserDetailsService 구현
    //  생성자 주입
    private final APIUserRepository apiUserRepository;

    //  loadUserByUsername() 내부에는 해당 사용자가 존재할 때 APIUserDTO를 반환하도록 코드 작성
    //  이 과정에서 모든 사용자는 ROLE_USER 권한을 가지도록 구성
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   //  UserDetailsService - loadUserByUsername 오버라이딩

        Optional<APIUser> result = apiUserRepository.findById(username);

        APIUser apiUser = result.orElseThrow( () -> new UsernameNotFoundException("Cannot find mid"));

        log.info("APIUserDetailsService apiUser ----------------------------------------");

        APIUserDTO apiUserDTO = new APIUserDTO(
                apiUser.getMid(),
                apiUser.getMpw(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        log.info(apiUserDTO);

        return apiUserDTO;
    }
}
