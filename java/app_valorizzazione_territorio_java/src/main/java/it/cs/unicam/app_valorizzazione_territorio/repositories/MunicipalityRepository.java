package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;

/**
 * This singleton class provides a repository for the municipalities of the system.
 * It also provides methods to manage the geo-localizable points of the municipalities and contents
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
}
