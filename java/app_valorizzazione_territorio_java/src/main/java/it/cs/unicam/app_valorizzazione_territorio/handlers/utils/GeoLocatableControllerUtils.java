package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.MunicipalityApprovalRequest;

public class GeoLocatableControllerUtils {
    public static void insertCompoundPoint(GeoLocatable geoLocatable,
                                           User user,
                                           Municipality municipality) {
        if (geoLocatable == null)
            throw new IllegalStateException("Compound point must be created first");

        if(Role.isAtLeastContributorForMunicipality(municipality).test(user)){
            municipality.addGeoLocatable(geoLocatable);
        }
        else {
            ApprovalRequestRepository.getInstance().add(
                    new MunicipalityApprovalRequest(user, geoLocatable, municipality));
        }
    }
}
