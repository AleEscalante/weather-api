package org.consiti.prueba.weather.controller;

import org.consiti.prueba.weather.filter.RequestThrottleFilter;
import org.consiti.prueba.weather.security.util.TokenUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
public class WeatherControllerTest {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private RequestThrottleFilter requestThrottleFilter;

    String bearerToken;

    @BeforeEach
    void setUp() throws Exception {
        requestThrottleFilter = new RequestThrottleFilter();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(requestThrottleFilter)
                .build();
        bearerToken = TokenUtils.createToken("prueba", "test@mail.com");
        requestThrottleFilter = null;
    }

    @DisplayName("current weather 200")
    @Test
    public void testGetCurrentWeatherOK() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/info".concat("/current-weather/").concat("atiquizaya"))
                        .header("Authorization", "Bearer " + bearerToken)
        ).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @DisplayName("current weather 400")
    @Test
    public void testGetCurrentWeatherBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/info".concat("/current-weather/").concat("pruebaprueba"))
                        .header("Authorization", "Bearer " + bearerToken)
        ).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @DisplayName("current weather 429")
    @Test
    void testGetCurrentWeatherTooMannyRequest() throws Exception {
        MvcResult result = null;
        for (int i = 0; i < 10; i++) {
            result = mockMvc.perform(
                    MockMvcRequestBuilders.get("/info".concat("/current-weather/").concat("atiquizaya"))
                            .header("Authorization", "Bearer " + bearerToken)
            ).andReturn();
        }
        assertEquals(429, result.getResponse().getStatus());
    }

    @DisplayName("forecast 200")
    @Test
    public void testGetForecastOK() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/info".concat("/forecast/").concat("atiquizaya"))
                        .header("Authorization", "Bearer " + bearerToken)
        ).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @DisplayName("forecast 400")
    @Test
    public void testGetForecastBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/info".concat("/forecast/").concat("pruebaprueba"))
                        .header("Authorization", "Bearer " + bearerToken)
        ).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @DisplayName("forecast 429")
    @Test
    void testGetForecastTooMannyRequest() throws Exception {
        MvcResult result = null;
        for (int i = 0; i < 10; i++) {
            result = mockMvc.perform(
                    MockMvcRequestBuilders.get("/info".concat("/forecast/").concat("atiquizaya"))
                            .header("Authorization", "Bearer " + bearerToken)
            ).andReturn();
        }
        assertEquals(429, result.getResponse().getStatus());
    }

    @DisplayName("pollution 200")
    @Test
    public void testGetPollutionOK() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/info".concat("/air-pollution/").concat("atiquizaya"))
                        .header("Authorization", "Bearer " + bearerToken)
        ).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @DisplayName("pollution 400")
    @Test
    public void testGetPollutionBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/info".concat("/air-pollution/").concat("pruebaprueba"))
                        .header("Authorization", "Bearer " + bearerToken)
        ).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @DisplayName("pollution 429")
    @Test
    void testGetPollutionTooMannyRequest() throws Exception {
        MvcResult result = null;
        for (int i = 0; i < 10; i++) {
            result = mockMvc.perform(
                    MockMvcRequestBuilders.get("/info".concat("/air-pollution/").concat("atiquizaya"))
                            .header("Authorization", "Bearer " + bearerToken)
            ).andReturn();
        }
        assertEquals(429, result.getResponse().getStatus());
    }

}
