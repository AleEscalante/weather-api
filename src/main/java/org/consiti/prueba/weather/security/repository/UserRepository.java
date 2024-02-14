package org.consiti.prueba.weather.security.repository;

import org.consiti.prueba.weather.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
