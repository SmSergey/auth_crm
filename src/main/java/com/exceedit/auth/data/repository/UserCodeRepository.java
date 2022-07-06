package com.exceedit.auth.data.repository;

import com.exceedit.auth.data.models.entities.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCodeRepository extends JpaRepository<UserCode, Long> {
    UserCode findByCode(String code);
}
