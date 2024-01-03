package it.cs.unicam.app_valorizzazione_territorio.tests.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.osm.OSMRequestHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class OSMRequestHandlerTest {

    @Test
    public void shouldRetrieveData1() throws IOException {
        OSMRequestHandler handler = OSMRequestHandler.getInstance();
        CoordinatesBox box = new CoordinatesBox(new Position(43.2, 13.2), new Position(43.1, 13.3));
        assertTrue(handler.retrieveOSMData(box).contains("Macerata"));
    }
}
