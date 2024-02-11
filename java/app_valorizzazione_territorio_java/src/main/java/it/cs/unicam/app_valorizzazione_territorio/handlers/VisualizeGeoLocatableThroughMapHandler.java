package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MapDOF;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProviderBase;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProviderProxy;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.io.IOException;
import java.util.List;


/**
 * A search handler that searches for GeoLocatable objects using a map.
 */
public class VisualizeGeoLocatableThroughMapHandler {

    /**
     *
     */
    public static Identifiable visualizeGeoLocatable(long geoLocatableId){
        return MunicipalityRepository.getInstance().getGeoLocatableByID(geoLocatableId).getDetailedFormat();
    }

    /**
     * creates a visualizable map of the given municipality
     * @param municipalityId the ID of the municipality to which the GeoLocatable objects are related
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static MapDOF handleMap(long municipalityId) throws IOException {
        return new MapProviderProxy(new MapProviderBase()).getMap(MunicipalityRepository
                .getInstance()
                .getItemByID(municipalityId))
                .getDetailedFormat();
    }

    /**
     * creates a visualizable map of the given municipality
     * @param municipalityId the ID of the municipality to which the GeoLocatable objects are related
     * @param box the coordinates box
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static MapDOF handleMap(long municipalityId,
                                   CoordinatesBox box) throws IOException {

        return new MapProviderProxy(new MapProviderBase()).getMap(MunicipalityRepository
                .getInstance()
                .getItemByID(municipalityId), box)
                .getDetailedFormat();
    }

    /**
     * creates a filtered visualizable map of the given municipality
     * @param municipalityId the ID of the municipality to which the GeoLocatable objects are related
     * @param filters the filters to apply
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static MapDOF handleFilteredMap(long municipalityId,
                                   List<SearchFilter> filters) throws IOException{

        return new MapProviderProxy(new MapProviderBase())
                .getFilteredMap(MunicipalityRepository.getInstance().getItemByID(municipalityId), filters)
                .getDetailedFormat();
    }

    /**
     * creates a filtered visualizable map of the given municipality
     * @param municipalityId the ID of the municipality to which the GeoLocatable objects are related
     * @param box the coordinates box
     * @param filters the filters to apply
     * @return the map
     * @throws IOException if an I/O error occurs during the OSM data retrieval
     */
    public static MapDOF handleFilteredMap(long municipalityId,
                                   CoordinatesBox box,
                                   List<SearchFilter> filters) throws IOException{

        return new MapProviderProxy(new MapProviderBase())
                .getFilteredMap(MunicipalityRepository.getInstance().getItemByID(municipalityId), box, filters)
                .getDetailedFormat();
    }


}