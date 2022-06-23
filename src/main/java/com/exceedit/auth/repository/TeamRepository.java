package com.exceedit.auth.repository;

import com.exceedit.auth.model.Client;
import com.exceedit.auth.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {
    Team findBy_id(String _id);
}
