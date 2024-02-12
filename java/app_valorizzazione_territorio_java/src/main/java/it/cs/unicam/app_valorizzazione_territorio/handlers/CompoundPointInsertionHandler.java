package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.CompoundPointBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.CompoundPointIF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.CompoundPoint;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.CompoundPointTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.RequestFactory;

/**
 * This class is responsible for handling the insertion of a compound point.
 */
public class CompoundPointInsertionHandler {

    /**
     * @param compoundPointIF the DTO of the compound point to be inserted
     * @throws IllegalArgumentException if the user or the municipality are not found, or if the compound
     *                                  point is not valid
     */
    public static long insertCompoundPoint(long municipalityID,
                                            long userID,
                                            CompoundPointIF compoundPointIF) {

        CompoundPoint compoundPoint = buildCompoundPoint(
                MunicipalityRepository.getInstance().getItemByID(municipalityID),
                UserRepository.getInstance().getItemByID(userID),
                compoundPointIF
        );
        return RequestRepository.getInstance()
                .add(RequestFactory.getApprovalRequest(compoundPoint)).getID();
    }

    private static CompoundPoint buildCompoundPoint(Municipality municipality,
                                                    User user,
                                                    CompoundPointIF compoundPointIF) {
        CompoundPointBuilder builder = new CompoundPointBuilder(
                CompoundPointTypeEnum.fromString(compoundPointIF.compoundPointType()),
                municipality,
                user);

        builder.setTitle(compoundPointIF.title())
                .setDescription(compoundPointIF.description())
                .addImage(compoundPointIF.images());
        compoundPointIF.pointsOfInterests().stream()
                .map(id -> MunicipalityRepository.getInstance().getGeoLocatableByID(id))
                .map(PointOfInterest.class::cast)
                .forEach(builder::addPointOfInterest);


        return builder.obtainResult();
    }

}
