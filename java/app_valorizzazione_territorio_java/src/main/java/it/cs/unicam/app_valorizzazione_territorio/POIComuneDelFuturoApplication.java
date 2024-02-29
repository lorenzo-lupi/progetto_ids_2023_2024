package it.cs.unicam.app_valorizzazione_territorio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ViewFilter;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class POIComuneDelFuturoApplication {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(POIComuneDelFuturoApplication.class, args);
    }

    @Bean
    public FilterProvider jsonFilterProvider() {
        return new SimpleFilterProvider().addFilter("Synthesized", new ViewFilter(View.Synthesized.class));
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
