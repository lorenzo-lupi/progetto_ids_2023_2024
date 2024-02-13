package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MapDOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.GeoLocatableControllerUtils;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProvider;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProviderBase;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

import java.io.IOException;

/**
 * This class handles the insertion of a point of interest.
 */
public class PointOfInterestInsertionHandler {

    private static final MapProvider mapProvider = new MapProviderBase();

    /**
     * Inserts the given point of interest in the municipality corresponding to the given ID.
     *
     * @param userId the ID of the user who is inserting the point of interest
     * @param municipalityId the ID of the municipality to which the point of interest is related
     * @param poiIF the DTO of the point of interest to be inserted
     * @throws IllegalArgumentException if the user or the municipality are not found, or if the point
     * of interest is not valid
     */
    public static long insertPointOfInterest(long userId, long municipalityId, PointOfInterestIF poiIF) {
        PointOfInterestBuilder builder =
                new PointOfInterestBuilder(MunicipalityRepository.getInstance().getItemByID(municipalityId),
                        UserRepository.getInstance().getItemByID(userId));

        builder.setTitle(poiIF.name())
                .setDescription(poiIF.description());
        poiIF.images().forEach(builder::addImage);

        MapProvider mapProvider = new MapProviderBase();

        try {
            if (!builder.getMunicipality().equals(mapProvider.getMunicipalityByPosition(poiIF.position())))
                throw new IllegalArgumentException("The position is not in the municipality");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while checking the position");
        }

        builder.setPosition(poiIF.position())
                .setClassification(PointOfInterest.stringToClass.get(poiIF.classification()));

        if (poiIF.classification().equals(Attraction.class.getSimpleName()))
                builder.setAttractionType(AttractionTypeEnum.fromString(poiIF.type()));
        else if (poiIF.classification().equals(Event.class.getSimpleName()))
                builder.setStartDate(poiIF.startDate())
                        .setEndDate(poiIF.endDate());
        else if (poiIF.classification().equals(Activity.class.getSimpleName()))
                builder.setActivityType(ActivityTypeEnum.fromString(poiIF.type()))
                        .setTimetable(poiIF.timetable());
        else throw new IllegalArgumentException("Invalid classification");

        GeoLocatable geoLocatable = builder.build().obtainResult();

        GeoLocatableControllerUtils.insertGeoLocatable(geoLocatable, UserRepository.getInstance().getItemByID(userId));

        return geoLocatable.getID();
    }

    /**
     *  this method make you visualize an EmptyMap
     * @param coordinatesBox coordinate box to visualize
     * @return the detailed format of the MAP
     */
    public static MapDOF visualizeEmptyMap(CoordinatesBox coordinatesBox) throws IOException {
        MapProvider provider = new MapProviderBase();
        return provider.getEmptyMap(coordinatesBox).getDetailedFormat();
    }

}
