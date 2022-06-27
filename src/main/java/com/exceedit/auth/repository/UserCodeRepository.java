package com.exceedit.auth.repository;

import com.exceedit.auth.model.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCodeRepository extends JpaRepository<UserCode, Long> {

}
