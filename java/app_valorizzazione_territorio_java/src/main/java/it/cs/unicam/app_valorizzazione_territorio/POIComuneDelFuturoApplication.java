package it.cs.unicam.app_valorizzazione_territorio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContestIF;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class POIComuneDelFuturoApplication {
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(POIComuneDelFuturoApplication.class, args);
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
