package org.consiti.prueba.weather.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.consiti.prueba.weather.security.entity.User;
import org.consiti.prueba.weather.security.enums.QueryType;

import java.sql.Timestamp;
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

    @NotNull
    @Column(nullable = false)
    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private QueryType queryType;

    @NotBlank
    @Column(nullable = false)
    private String requestLink;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String responseJson;

    public Consultation(User user,
                        Timestamp timestamp,
                        QueryType queryType,
                        String requestLink,
                        String responseJson) {
        this.user = user;
        this.timestamp = timestamp;
        this.queryType = queryType;
        this.requestLink = requestLink;
        this.responseJson = responseJson;
    }
}
