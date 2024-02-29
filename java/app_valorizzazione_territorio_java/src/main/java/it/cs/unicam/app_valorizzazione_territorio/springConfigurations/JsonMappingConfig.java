package it.cs.unicam.app_valorizzazione_territorio.springConfigurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ViewFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class JsonMappingConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule())
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .setFilterProvider(new SimpleFilterProvider().addFilter(
                        "Synthesized",
                        new ViewFilter(View.Synthesized.class)))
                .registerModule(new JavaTimeModule());

        return objectMapper;
    }
}
