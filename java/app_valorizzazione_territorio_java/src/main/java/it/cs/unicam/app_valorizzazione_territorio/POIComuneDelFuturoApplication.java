package it.cs.unicam.app_valorizzazione_territorio;

import it.cs.unicam.app_valorizzazione_territorio.osm.MapProvider;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProviderBase;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProviderProxy;
import it.cs.unicam.app_valorizzazione_territorio.osm.OSMRequestHandler;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class POIComuneDelFuturoApplication {
    public static void main(String[] args) {
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
