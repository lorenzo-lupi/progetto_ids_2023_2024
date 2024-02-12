package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.Repository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.requests.RequestFactory;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import org.apache.commons.lang3.tuple.Pair;


import java.util.List;

/**
 * This class is responsible for handling the modification of a {@link GeoLocatable} object.
 */
public class ModifyGeoLocatableHandler {
    /**
     * This method returns the setters of a GeoLocatable object.
     *
     * @param geolocatableID the ID of the GeoLocatable object
     * @return the setters of the GeoLocatable object
     */
    public static List<String> getSetters(long geolocatableID) {
        return MunicipalityRepository.getInstance()
                .getGeoLocatableByID(geolocatableID)
                .getSettersMapping()
                .keySet()
                .stream()
                .map(Enum::toString)
                .toList();
    }

    public static long modifyGeoLocatable(long userID,
                                          long geolocatableID,
                                          List<Pair<Parameter, Object>> pairs,
                                          String message) {

        GeoLocatable geoLocatable = MunicipalityRepository.getInstance().getGeoLocatableByID(geolocatableID);
        User user = UserRepository.getInstance().getItemByID(userID);

        return modifyGeoLocatable(user, geoLocatable, pairs, message);
    }

    private static long modifyGeoLocatable(User user,
                                           GeoLocatable geoLocatable,
                                           List<Pair<Parameter, Object>> pairs,
                                           String message) {

        if (userIsAuthorized(user, geoLocatable.getMunicipality())) {
            modifyGeoLocatableValues(geoLocatable, pairs);
            return Repository.NULL_ID;
        } else {
            return addModifyRequest(pairs, geoLocatable, message);
        }

    }

    private static void modifyGeoLocatableValues(GeoLocatable geoLocatable,
                                                 List<Pair<Parameter, Object>> pairs) {
        pairs.forEach(pair -> {
                    if (!geoLocatable.getSettersMapping().containsKey(pair.getLeft()))
                        throw new IllegalArgumentException("Invalid parameter");
                    geoLocatable.getSettersMapping()
                            .get(pair.getLeft())
                            .accept(pair.getRight());
                }
        );
    }

    private static long addModifyRequest(List<Pair<Parameter, Object>> pairs,
                                         GeoLocatable geoLocatable, String message) {

        Request<?> request = RequestFactory.getModificationRequest(geoLocatable, pairs, message);
        RequestRepository.getInstance()
                .add(request);
        return request.getID();
    }

    private static boolean userIsAuthorized(User user, Municipality municipality) {
        return Role.isAtLeastContributorForMunicipality(municipality).test(user);
    }

}
