package org.omnera.picbloom.repository;

import java.util.Optional;

import org.omnera.picbloom.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{

    Optional<Account> findByEmail(String email);
    
}
