package com.exceedit.auth.data.repository;

import com.exceedit.auth.data.models.entities.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokensRepository extends JpaRepository<UserTokens, Long> {
    UserTokens findByAccessToken(String accessToken);
}
