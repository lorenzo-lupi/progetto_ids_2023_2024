package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is used to retrieve OSM data from the OSM API.
 */
public class OSMRequestHandler {

    public static final String OSM_API_URL = "https://api.openstreetmap.org/api/0.6/map";
    public static final String OSM_NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/reverse";
    private final URL urlOsmApi;
    private final URL urlNominatimApi;
    Map<String, String> parameters;
    private static OSMRequestHandler instance;

    private OSMRequestHandler() throws MalformedURLException {
        this.urlOsmApi = new URL(OSM_API_URL);
        this.urlNominatimApi = new URL(OSM_NOMINATIM_API_URL);
        this.parameters = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the class.
     *
     * @return the singleton instance of the class
     * @throws MalformedURLException if the internal OSM API URL is malformed
     */
    public static OSMRequestHandler getInstance() throws MalformedURLException {
        if (instance == null) instance = new OSMRequestHandler();
        return instance;
    }

    /**
     * Retrieves OSM data from the given box.
     * The OSM data contains all the geographical objects included in the given box.
     *
     * @param box the coordinates box
     * @return the OSM data in JSON format
     * @throws IOException if an I/O error occurs
     */
    public String retrieveOSMData(CoordinatesBox box) throws IOException {
        return retrieveOSMData(box.getWest(), box.getSouth(), box.getEast(), box.getNorth());
    }

    /**
     * Retrieves OSM data from the given box, represented by the values of minimum and maximum
     * latitude and longitude.
     * The OSM data contains all the geographical objects included in the given box.
     *
     * @param left minimum longitude
     * @param bottom minimum latitude
     * @param right maximum longitude
     * @param top maximum latitude
     * @return the OSM data in JSON format
     * @throws IOException if an I/O error occurs
     */
    public String retrieveOSMData(double left, double bottom, double right, double top) throws IOException {
        parameters.clear();
        parameters.put("bbox", left + "," + bottom + "," + right + "," + top);

        HttpURLConnection connection = (HttpURLConnection) getGETRequestURLFromParameters(urlOsmApi).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "application/json");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) return getResponse(connection);
        else throw new IOException("HTTP error code: " + connection.getResponseCode());
    }

    public String getMunicipalityOfPosition(Position position) throws IOException {
        parameters.clear();
        parameters.put("format", "json");
        parameters.put("lat", String.valueOf(position.getLatitude()));
        parameters.put("lon", String.valueOf(position.getLongitude()));

        HttpURLConnection connection =
                (HttpURLConnection) getGETRequestURLFromParameters(urlNominatimApi).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "application/json");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            return parseMunicipalityFromNominatimResponse(getResponse(connection));
        else throw new IOException("HTTP error code: " + connection.getResponseCode());
    }

    /**
     * Returns the URL of the GET request data obtained from the URL and the parameters,
     * that is ready to use in a GET request.
     *
     * @return the URL of the GET request obtained from the parameters
     */
    private URL getGETRequestURLFromParameters(URL baseUrl) throws UnsupportedEncodingException, MalformedURLException {
        StringBuilder requestURL = new StringBuilder();
        requestURL.append(baseUrl +(parameters.isEmpty() ? "" : "?"));
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            requestURL.append(entry.getKey());
            requestURL.append("=");
            requestURL.append(entry.getValue());
            requestURL.append("&");
        }

        return new URL(requestURL.toString());
    }

    /**
     *Returns the response of the given connection.
     *
     * @param connection the connection
     * @return the response of the given connection
     * @throws IOException if an I/O error occurs
     */
    private String getResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                response.append(line);
                response.append(System.lineSeparator());
            }
        } finally {
            connection.disconnect();
        }
        return response.toString();
    }

    private String parseMunicipalityFromNominatimResponse(String response) {
        return response.indexOf("\"town\":") != -1 ?
                response.substring(response.indexOf("\"town\":") + 8, response.indexOf("\",\"county\"")) :
                "";
    }
}
