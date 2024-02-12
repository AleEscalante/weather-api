package org.consiti.prueba.weather.model.input.weather.current;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WindModel (
        @JsonProperty("speed") double speed,
        @JsonProperty("deg") int direction){
}
