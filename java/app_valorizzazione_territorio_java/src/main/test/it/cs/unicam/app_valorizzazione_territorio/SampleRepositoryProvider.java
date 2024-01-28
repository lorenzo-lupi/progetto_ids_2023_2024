package it.cs.unicam.app_valorizzazione_territorio;

import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.ActivityTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SampleRepositoryProvider {
    public static final List<User> users = Arrays.asList(
            new User("user1", "user1@gmail.com"),
            new User("user2", "user2@gmail.com"),
            new User("user3", "user3@bitmail.it"),
            new User("user4", "user4@bitmail.it"),
            new User("user5", "user5@blobmail.com")
    ); 

    public static final List<Municipality> municipalities = Arrays.asList(
            new Municipality("Macerata", "Comune di Macerata",
                    new Position(43.29812657107886, 13.451878161920886),
                    new CoordinatesBox(new Position(43.317324, 13.409422), new Position (43.271074, 13.499990)),
                    new ArrayList<>()),
            new Municipality("Camerino", "Comune di Camerino",
                    new Position(43.13644468556232, 13.067156069846892),
                    new CoordinatesBox(new Position(43.153712, 13.036414), new Position (43.123261, 13.095768)),
                    new ArrayList<>())
    );

    public static final List<GeoLocatable> geoLocatables = Arrays.asList(
            new Attraction("Università di Camerino", "Università di Camerino",
                    new Position(43.13644468556232, 13.067156069846892),
                    municipalities.get(1), AttractionTypeEnum.BUILDING),
            new Attraction("Via Madonna delle Carceri", "Via Madonna delle Carceri",
                    new Position(43.140, 13.069),
                    municipalities.get(1), AttractionTypeEnum.OTHER),
            new Attraction("Piazza della Libertà", "Piazza della Libertà",
                    new Position(43.29812657107886, 13.451878161920886),
                    municipalities.get(0), AttractionTypeEnum.SQUARE),
            new Event("Corsa della Spada", "Celebrazione tradizionale della Corsa della Spada",
                    new Position(43.135812352706715, 13.068367879486194),
                    municipalities.get(1), new Date(124, 5, 17), new Date(124, 5, 24)),
            new Event("September Fest", "Festa a tema Birra",
                    new Position(43.29812657107886, 13.451878161920886),
                    municipalities.get(0), new Date(124, 9, 1), new Date(124, 9, 30)),
            new Activity("Pizzeria Enjoy", "Pizzeria",
                    new Position(43.143290511951314, 13.078617450882426),
                    municipalities.get(1), ActivityTypeEnum.RESTAURANT),
            new Activity("Basilica di San Venanzio", "Basilica di San Venanzio",
                    new Position(43.137753115974135, 13.073411976140818),
                    municipalities.get(1), ActivityTypeEnum.CHURCH)
    );

    static {
        geoLocatables.addAll(Arrays.asList(
                new CompoundPoint("Tour dello studente", "Tour dello studente",
                        municipalities.get(1), CompoundPointTypeEnum.ITINERARY, Arrays.asList(
                                (PointOfInterest)geoLocatables.get(1),
                                (PointOfInterest)geoLocatables.get(0),
                                (PointOfInterest)geoLocatables.get(5)),
                        new ArrayList<>()),
                new CompoundPoint("Tradizione di San Venanzio", "Tradizione di San Venanzio",
                        municipalities.get(1), CompoundPointTypeEnum.EXPERIENCE, Arrays.asList(
                                (PointOfInterest)geoLocatables.get(6),
                                (PointOfInterest)geoLocatables.get(3)),
                        new ArrayList<>())
        ));
    }



    public static final List<Content> contents = Arrays.asList(
            new PointOfInterestContent("Foto della basilica di San Venanzio",
                    (PointOfInterest)geoLocatables.get(6), new ArrayList<>(), users.get(0)),
            new PointOfInterestContent("Foto della piazza della libertà",
                    (PointOfInterest)geoLocatables.get(2), new ArrayList<>(), users.get(0)),
            new PointOfInterestContent("Foto della piazza della libertà di notte",
                    (PointOfInterest)geoLocatables.get(2), new ArrayList<>(), users.get(1)),
            new PointOfInterestContent("Foto delle pizze margerita",
                    (PointOfInterest)geoLocatables.get(5), new ArrayList<>(), users.get(2)),
            new PointOfInterestContent("Manifesto della corsa della spada",
                    (PointOfInterest)geoLocatables.get(3), new ArrayList<>(), users.get(3))
    );

    static {
        municipalities.get(1).addGeoLocatable(geoLocatables.get(0));
        municipalities.get(1).addGeoLocatable(geoLocatables.get(1));
        municipalities.get(0).addGeoLocatable(geoLocatables.get(2));
        municipalities.get(1).addGeoLocatable(geoLocatables.get(3));
        municipalities.get(0).addGeoLocatable(geoLocatables.get(4));
        municipalities.get(1).addGeoLocatable(geoLocatables.get(5));
        municipalities.get(1).addGeoLocatable(geoLocatables.get(6));
    }

    public static void setUpMunicipalityRepository () {

    }
}
