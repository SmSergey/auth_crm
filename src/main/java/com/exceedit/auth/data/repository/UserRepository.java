package com.exceedit.auth.data.repository;

import com.exceedit.auth.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
