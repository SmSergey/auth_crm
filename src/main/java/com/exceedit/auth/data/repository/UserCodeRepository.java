package com.exceedit.auth.data.repository;

import com.exceedit.auth.data.models.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCodeRepository extends JpaRepository<UserCode, Long> {

}
