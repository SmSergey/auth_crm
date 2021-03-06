package com.exceedit.auth.data.repository;

import com.exceedit.auth.data.models.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {
    Team findBy_id(String _id);
}
