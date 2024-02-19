package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;

import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;

public class IdsUtils {
    /**
     * returns the point of interest from the given id.
     * wrapper method which throws an illegal argument exception if the id is not valid or the id is not a point of interest
     * @throws IllegalArgumentException if the id is not valid or the id is not a point of interest
     */
    public static PointOfInterest getPoiFromID(long pointOfInterestID, Municipality municipality){
        if(!(MunicipalityRepository.getInstance().getGeoLocatableByID(pointOfInterestID) instanceof PointOfInterest pointOfInterest))
            throw new IllegalArgumentException("Wrong poi id");
        return pointOfInterest;
    }

}
