package com.exceedit.auth.data.repository;

import com.exceedit.auth.data.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByClientId(String clientId);
}
