package org.consiti.prueba.weather.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.consiti.prueba.weather.security.entity.User;
import org.consiti.prueba.weather.security.enums.QueryType;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultation_records")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @NotBlank
    @Column(nullable = false)
    private QueryType queryType;

    @NotBlank
    @Column(nullable = false)
    private String requestLink;

    @NotBlank
    @Column(columnDefinition = "JSON", nullable = false)
    private String responseJson;
}
