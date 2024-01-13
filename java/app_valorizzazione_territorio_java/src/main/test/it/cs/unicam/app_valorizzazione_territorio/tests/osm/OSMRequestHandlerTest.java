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

    @Test
    public void shouldRetrieveData2() throws IOException {
        OSMRequestHandler handler = OSMRequestHandler.getInstance();
        CoordinatesBox box = new CoordinatesBox(new Position(43.15, 13.06), new Position(43.14, 13.07));
        assertTrue(handler.retrieveOSMData(box).contains("Università di Camerino"));
    }

    @Test
    public void shouldRetrieveData3() throws IOException {
        OSMRequestHandler handler = OSMRequestHandler.getInstance();
        CoordinatesBox box = new CoordinatesBox(new Position(43.141, 13.069), new Position(43.140, 13.070));
        assertTrue(handler.retrieveOSMData(box).contains("Via Madonna delle Carceri"));
    }
}