package org.consiti.prueba.weather.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.consiti.prueba.weather.security.entity.User;
import org.consiti.prueba.weather.security.enums.QueryType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationDto {

    @NotNull
    private User user;

    @NotBlank
    @NotNull
    private LocalDateTime timestamp;

    @NotBlank
    @NotNull
    private QueryType queryType;

    @NotBlank
    @NotNull
    private String requestLink;

    @NotBlank
    @NotNull
    private String responseJson;
}
