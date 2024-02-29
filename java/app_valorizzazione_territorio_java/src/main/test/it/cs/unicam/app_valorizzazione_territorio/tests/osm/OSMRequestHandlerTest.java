package it.cs.unicam.app_valorizzazione_territorio.tests.osm;

import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.osm.OSMRequestHandler;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class, OSMRequestHandler.class})
@DataJpaTest
public class OSMRequestHandlerTest {
    @Autowired
    private SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    private OSMRequestHandler osmRequestHandler;

    @BeforeEach
    public void setUp() {
        sampleRepositoryProvider.setUpMunicipalities();
    }

    @AfterEach
    public void clear() {
        sampleRepositoryProvider.clearMunicipalities();
    }
    
    @Test
    public void shouldRetrieveMapData1() throws IOException {
        CoordinatesBox box = new CoordinatesBox(new Position(43.2, 13.2), new Position(43.1, 13.3));
        assertTrue(osmRequestHandler.retrieveOSMData(box).contains("Macerata"));
    }

    @Test
    public void shouldRetrieveMapData2() throws IOException {
        CoordinatesBox box = new CoordinatesBox(new Position(43.15, 13.06), new Position(43.14, 13.07));
        assertTrue(osmRequestHandler.retrieveOSMData(box).contains("Università di Camerino"));
    }

    @Test
    public void shouldRetrieveMapData3() throws IOException {
        CoordinatesBox box = new CoordinatesBox(new Position(43.141, 13.069), new Position(43.140, 13.070));
        assertTrue(osmRequestHandler.retrieveOSMData(box).contains("Via Madonna delle Carceri"));
    }

    @Test
    public void shouldRetrievePositionData1() throws IOException {
        Position position = sampleRepositoryProvider.CAMERINO.getPosition();
        assertTrue(osmRequestHandler.getMunicipalityOfPosition(position).equals("Camerino"));
    }

    @Test
    public void shouldRetrievePositionData2() throws IOException {
        Position position = sampleRepositoryProvider.MACERATA.getPosition();
        assertTrue(osmRequestHandler.getMunicipalityOfPosition(position).equals("Macerata"));
    }
}
