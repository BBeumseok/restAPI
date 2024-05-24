package com.zerock.restapi.repository;

import com.zerock.restapi.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIUserRepository extends JpaRepository<APIUser, String> {
}
