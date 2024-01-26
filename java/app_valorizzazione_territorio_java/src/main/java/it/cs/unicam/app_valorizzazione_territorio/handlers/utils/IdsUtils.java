package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;

import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.Repository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;

import static it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion.EQUALS_ID;

public class IdsUtils {
    /**
     * Returns the municipality object with the given ID.
     *
     * @param municipalityID the ID of the municipality
     * @return the municipality object with the given ID
     * @throws IllegalArgumentException if the municipality with the given ID is not found
     */
    public static Municipality getMunicipalityObject(long municipalityID) {
        return getObjectFromId(municipalityID, MunicipalityRepository.getInstance());
    }

    /**
     * Returns the user object with the given ID.
     *
     * @param municipalityID the ID of the user
     * @return the user object with the given ID
     * @throws IllegalArgumentException if the user with the given ID is not found
     */
    public static User getUserObject(long municipalityID) {
        return getObjectFromId(municipalityID, UserRepository.getInstance());
    }

    public static GeoLocatable getGeoLocatableObject(long pointOfInterestID, long municipalityID) {
        return getGeoLocatableObject(pointOfInterestID, getMunicipalityObject(municipalityID));
    }

    //TODO: ATTENZIONE! E' molto più semplice di così, esiste già il metodo in MunicipalityRepository per
    //TODO: ottenere un geoLocatable da un ID, che è univoco in tutti i comuni.
    public static GeoLocatable getGeoLocatableObject(long pointOfInterestID, Municipality municipality) {
        if(municipality == null)
            throw new IllegalArgumentException("Municipality cannot be null");

        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(municipality.getGeoLocatables());
        searchEngine.addCriterion(Parameter.ID, EQUALS_ID, pointOfInterestID);
        GeoLocatable result = searchEngine.search().getResults().stream().findFirst().orElse(null);

        if (result == null)
            throw new IllegalArgumentException("Point of interest not found");

        return result;
    }

    private static <T extends Identifiable>  T getObjectFromId(long id, Repository<T> repo) {
        if(repo == null)
            throw new IllegalArgumentException("Repository cannot be null");

        T result = repo.getItemByID(id);
        if(result == null)
            throw new IllegalArgumentException("Object not found");
        return result;
    }

    public static PointOfInterest getPoiFromID(long pointOfInterestID, Municipality municipality){
        if(!(IdsUtils.getGeoLocatableObject(pointOfInterestID, municipality) instanceof PointOfInterest pointOfInterest))
            throw new IllegalArgumentException("Wrong poi id");

        return pointOfInterest;
    }
}
