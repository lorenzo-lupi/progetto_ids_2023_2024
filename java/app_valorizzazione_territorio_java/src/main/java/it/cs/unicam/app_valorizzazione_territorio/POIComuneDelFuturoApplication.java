package it.cs.unicam.app_valorizzazione_territorio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class POIComuneDelFuturoApplication {
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(POIComuneDelFuturoApplication.class, args);

        ObjectMapper objectMapper = new ObjectMapper();
    }


    /*
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public OSMRequestHandler osmRequestHandler() throws MalformedURLException {
        return new OSMRequestHandler();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MapProvider mapProviderBase() throws MalformedURLException {
        return new MapProviderBase(municipalityJpaRepository(), osmRequestHandler());
    }*/
}
