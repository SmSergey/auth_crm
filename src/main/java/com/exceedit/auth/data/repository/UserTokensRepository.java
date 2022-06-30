package com.exceedit.auth.data.repository;

import com.exceedit.auth.data.models.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokensRepository extends JpaRepository<UserTokens, Long> {
    UserTokens findByAccessToken(String accessToken);
}
