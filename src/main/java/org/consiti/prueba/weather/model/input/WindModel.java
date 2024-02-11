package org.consiti.prueba.weather.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WindModel (
        @JsonProperty("speed") double speed,
        @JsonProperty("deg") int direction){
}
