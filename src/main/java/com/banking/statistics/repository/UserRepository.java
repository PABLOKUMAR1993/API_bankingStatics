package com.banking.statistics.repository;

import com.banking.statistics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByNombreUsuario(String nombreUsuario);
    
    boolean existsByEmail(String email);
    
    boolean existsByNombreUsuario(String nombreUsuario);
}