package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.model.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterest;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * This singleton class provides a repository for the municipalities of the system.
 * It also provides methods to manage the geo-locatable points of the municipalities and contents
 * of their POIs.
 */
public class MunicipalityRepository extends Repository<Municipality> {
    private static MunicipalityRepository instance;
    private long nextGeoLocalizableID = 0L;
    private long nextContentID = 0L;

    private MunicipalityRepository() {
        super();
    }

    public static MunicipalityRepository getInstance() {
        if (instance == null) instance = new MunicipalityRepository();
        return instance;
    }

    /**
     * Returns and updates the next available ID for the geo-localizables of
     * the municipalities in the repository.
     *
     * @return the next available ID for the geo-localizables of the repository
     */
    public long getNextGeoLocalizableID() {
        return nextGeoLocalizableID++;
    }

    /**
     * Returns and updates the next available ID for the content of the
     * contents of the POIs of the municipalities in the repository.
     *
     * @return the next available ID for the contents of the POIs of the municipalities in the repository
     */
    public long getNextContentID() {
        return nextContentID++;
    }

    /**
     * Returns a map of all the geo-locatable points of the municipalities in the repository mapped with their IDs.
     *
     * @return a map of all the geo-locatable points of the municipalities
     */
    private Map<Long, GeoLocatable> getAllGeoLocatablesMap() {
        return this.getItemStream().parallel()
                .flatMap(municipality -> municipality.getGeoLocatables().stream())
                .collect(toMap(GeoLocatable::getID, Function.identity()));
    }

    /**
     * Returns a map of all the content of the POIs of the municipalities in the repository mapped with their IDs.
     *
     * @return a map of all the content of the POIs of the municipalities
     */
    private Map<Long, Content> getAllContentMap() {
        return this.getItemStream().parallel()
                .flatMap(municipality -> municipality.getGeoLocatables().stream())
                .filter(geoLocatable -> geoLocatable instanceof PointOfInterest)
                .flatMap(pointOfInterest -> pointOfInterest.getContents().stream())
                .collect(toMap(Content::getID, Function.identity()));
    }

    /**
     * Returns the geo-locatable point of the municipalities in the repository with the given ID.
     *
     * @param ID the ID of the geo-locatable point to be returned
     * @return the geo-locatable point of the municipalities in the repository with the given ID
     */
    public GeoLocatable getGeoLocatableByID(long ID) {
        return getAllGeoLocatablesMap().get(ID);
    }

    /**
     * Returns the content of the POIs of the municipalities in the repository with the given ID.
     *
     * @param ID the ID of the content to be returned
     * @return the content of the POIs of the municipalities in the repository with the given ID
     */
    public Content getContentByID(long ID) {
        return getAllContentMap().get(ID);
    }

    /**
     * Returns a stream of all the geo-locatable points of the municipalities in the repository.
     *
     * @return a stream of all the geo-locatable points of the municipalities in the repository
     */
    public Stream<GeoLocatable> getAllGeoLocatables() {
        return getAllGeoLocatablesMap().values().stream();
    }

    /**
     * Returns a stream of all the content of the POIs of the municipalities in the repository.
     *
     * @return a stream of all the content of the POIs of the municipalities in the repository
     */
    public Stream<Content> getAllContents() {
        return getAllContentMap().values().stream();
    }
}
