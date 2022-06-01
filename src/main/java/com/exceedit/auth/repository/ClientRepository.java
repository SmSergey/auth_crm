package com.exceedit.auth.repository;

import com.exceedit.auth.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByClientId(String clientId);
}
