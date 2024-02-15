package org.consiti.prueba.weather.service;

import jakarta.servlet.http.HttpServletRequest;
import org.consiti.prueba.weather.dto.Message;
import org.consiti.prueba.weather.entity.Consultation;
import org.consiti.prueba.weather.model.input.location.LocationModel;
import org.consiti.prueba.weather.repository.ConsultationRepository;
import org.consiti.prueba.weather.security.entity.User;
import org.consiti.prueba.weather.security.enums.QueryType;
import org.consiti.prueba.weather.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ConsultationService {

    @Autowired
    ConsultationRepository consultationRepository;

    public Optional<Consultation> findByUser(User user) {
        return consultationRepository.findByUser(user);
    }

    public void save(Consultation consultation) {
        consultationRepository.save(consultation);
    }

    public void delete(long id) {
        consultationRepository.deleteById(id);
    }

}
