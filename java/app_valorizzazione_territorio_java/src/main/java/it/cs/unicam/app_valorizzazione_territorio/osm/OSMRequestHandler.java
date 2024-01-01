package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OSMRequestHandler {

    public static final String OSM_API_URL = "https://api.openstreetmap.org/api/0.6/";
    private final URL url;
    private HttpURLConnection connection;

    public OSMRequestHandler() throws MalformedURLException {
        this.url = new URL(OSM_API_URL);
    }

    public OSMRequestHandler(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public String retrieveOSMData(CoordinatesBox box) throws IOException {
        connection = (HttpURLConnection) this.url.openConnection();
        //TODO: implement
        return null;
    }
}
