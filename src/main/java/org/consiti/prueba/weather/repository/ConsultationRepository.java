package org.consiti.prueba.weather.repository;

import org.consiti.prueba.weather.entity.Consultation;
import org.consiti.prueba.weather.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Optional<Consultation> findByUser(User user);
}
