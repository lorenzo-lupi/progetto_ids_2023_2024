package it.cs.unicam.app_valorizzazione_territorio.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
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
                        new ViewFilter(View.Synthesized.class)));

        return objectMapper;
    }
}
