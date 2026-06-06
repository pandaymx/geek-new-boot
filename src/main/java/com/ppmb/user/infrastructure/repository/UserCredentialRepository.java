package com.ppmb.user.infrastructure.repository;

import com.ppmb.user.domain.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {}
