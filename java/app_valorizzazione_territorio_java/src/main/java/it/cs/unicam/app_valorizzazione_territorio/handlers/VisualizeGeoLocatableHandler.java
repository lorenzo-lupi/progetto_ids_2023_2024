package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.CompoundPointDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.GeoLocatableSOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.PointOfInterestDOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.CompoundPoint;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

/**
 * This class provides the methods to visualize a GeoLocatable object.
 */
public class VisualizeGeoLocatableHandler {

    /**
     * This method visualizes a GeoLocatable object.
     *
     * @param idComune       the id of the comune
     * @param idMunicipality the id of the municipality
     */

    @SuppressWarnings("unchecked")
    public static List<GeoLocatableSOF> searchGeoLocatables(long idComune, long idMunicipality, List<SearchFilter> filters) {
        return (List<GeoLocatableSOF>) SearchHandler.getFilteredItems(
                MunicipalityRepository.getInstance().getItemByID(idMunicipality).getGeoLocatables(),
                filters
        );
    }

    /**
     *
     */
    public static Identifiable visualizeDetailedGeoLocatable(long idGeoLocatable) {
        return MunicipalityRepository
                .getInstance()
                .getGeoLocatableByID(idGeoLocatable)
                .getDetailedFormat();
    }

    /**
     * This method visualizes a PointOfInterest object.
     * @param idPointOfInterest the id of the point of interest
     * @return the detailed format of the point of interest
     */
    public static PointOfInterestDOF visualizeDetailedPointOfInterest(long idPointOfInterest){
        GeoLocatable geoLocatable = MunicipalityRepository.getInstance().getGeoLocatableByID(idPointOfInterest);

        if(geoLocatable instanceof PointOfInterest pointOfInterest){
            return pointOfInterest.getDetailedFormat();
        }
        else{
            throw new IllegalArgumentException("The given ID does not correspond to a point of interest");
        }
    }

    /**
     * This method visualizes a CompoundPoint object.
     * @param idCompoundPoint the id of the compound point
     * @return the detailed format of the compound point
     */
    public static CompoundPointDOF visualizeDetailedCompoundPoint(long idCompoundPoint){
        GeoLocatable geoLocatable = MunicipalityRepository.getInstance().getGeoLocatableByID(idCompoundPoint);

        if(geoLocatable instanceof CompoundPoint compoundPoint){
            return compoundPoint.getDetailedFormat();
        }
        else{
            throw new IllegalArgumentException("The given ID does not correspond to a compound point");
        }
    }

}
