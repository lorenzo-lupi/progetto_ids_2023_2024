package it.cs.unicam.app_valorizzazione_territorio;

import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.contest.ContestBase;
import it.cs.unicam.app_valorizzazione_territorio.contest.GeoLocatableContestDecorator;
import it.cs.unicam.app_valorizzazione_territorio.contest.PrivateContestDecorator;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.model.*;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.ActivityTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.ApprovalRequest;
import it.cs.unicam.app_valorizzazione_territorio.requests.ContestApprovalRequest;
import it.cs.unicam.app_valorizzazione_territorio.requests.MunicipalityApprovalRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SampleRepositoryProvider {

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

    public static final List<User> users = Arrays.asList(
            new User("user1", "user1@gmail.com"),
            new User("user2", "user2@gmail.com"),
            new User("user3", "user3@bitmail.it"),
            new User("user4", "user4@bitmail.it"), //Curator
            new User("user5", "user5@blobmail.com"), //Entertainer
            new User("user6", "user6@blobmail.com") //Entertainer
    );

    static {
        users.get(3).addRole(municipalities.get(1), RoleTypeEnum.CURATOR);
        users.get(4).addRole(municipalities.get(0), RoleTypeEnum.ENTERTAINER);
        users.get(5).addRole(municipalities.get(1), RoleTypeEnum.ENTERTAINER);
    }

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


    public static final List<Contest> contests = Arrays.asList(
            new ContestBase("Concorso fotografico annuale", users.get(4),
                    "Concorso fotografico generico edizione 2024", "Una foto per partecipante",
                    new Date(124, 1, 1), new Date(124, 10, 30),
                    new Date(124, 12, 31)),
            new ContestBase("Concorso fotografico annuale", users.get(4),
                    "Concorso fotografico generico edizione 2025", "Una foto per partecipante",
                    new Date(125, 1, 1), new Date(125, 10, 30),
                    new Date(125, 12, 31)),
            new GeoLocatableContestDecorator(new ContestBase("Migliore foto di pizza Novembre", users.get(5),
                    "Concorso fotografico per la migliore foto di pizza", "Una foto per partecipante",
                    new Date(124, 1, 1), new Date(124, 10, 30),
                    new Date(124, 12, 31)), geoLocatables.get(5)),
            new PrivateContestDecorator(new ContestBase("Concorso pittura dei paesaggi", users.get(5),
                    "Concorso fotografico generico edizione 2026",
                    "Foto di una pittura fatta a mano di un pesaggio di Camerino",
                    new Date(126, 1, 1), new Date(126, 10, 30),
                    new Date(126, 12, 31)), List.of(users.get(0), users.get(1), users.get(2)))
    );

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
                    (PointOfInterest)geoLocatables.get(3), new ArrayList<>(), users.get(3)),
            new ContestContent("Foto per le strade di Macerata",
                    contests.get(0), new ArrayList<>(), users.get(0)),
            new ContestContent("Foto della torre civica",
                    contests.get(0), new ArrayList<>(), users.get(1)),
            new ContestContent("Foto della pizza Regina Sbagliata",
                    contests.get(2), new ArrayList<>(), users.get(2)),
            new ContestContent("Pittura della Basilica di San Venanzio",
                    contests.get(3), new ArrayList<>(), users.get(0)),
            new ContestContent("Pittura di Piazza Cavour",
                    contests.get(3), new ArrayList<>(), users.get(1))

    );

    static {
        municipalities.get(1).addGeoLocatable(geoLocatables.get(0)); geoLocatables.get(0).approve();
        municipalities.get(1).addGeoLocatable(geoLocatables.get(1)); geoLocatables.get(1).approve();
        municipalities.get(0).addGeoLocatable(geoLocatables.get(2)); geoLocatables.get(2).approve();
        municipalities.get(1).addGeoLocatable(geoLocatables.get(3)); geoLocatables.get(3).approve();
        municipalities.get(0).addGeoLocatable(geoLocatables.get(4));
        municipalities.get(1).addGeoLocatable(geoLocatables.get(5)); geoLocatables.get(5).approve();
        municipalities.get(1).addGeoLocatable(geoLocatables.get(6)); geoLocatables.get(6).approve();

        ((PointOfInterest)geoLocatables.get(6)).addContent((PointOfInterestContent)contents.get(0));
        ((PointOfInterest)geoLocatables.get(2)).addContent((PointOfInterestContent)contents.get(1)); contents.get(1).approve();
        ((PointOfInterest)geoLocatables.get(2)).addContent((PointOfInterestContent)contents.get(2)); contents.get(2).approve();
        ((PointOfInterest)geoLocatables.get(5)).addContent((PointOfInterestContent)contents.get(3)); contents.get(3).approve();
        ((PointOfInterest)geoLocatables.get(3)).addContent((PointOfInterestContent)contents.get(4)); contents.get(4).approve();
        contests.get(0).getProposalRequests().proposeContent((ContestContent)contents.get(5)); contents.get(5).approve();
        contests.get(0).getProposalRequests().proposeContent((ContestContent)contents.get(6)); contents.get(6).approve();
        contests.get(2).getProposalRequests().proposeContent((ContestContent)contents.get(7)); contents.get(7).approve();
        contests.get(3).getProposalRequests().proposeContent((ContestContent)contents.get(8)); contents.get(8).approve();
        contests.get(3).getProposalRequests().proposeContent((ContestContent)contents.get(9));
    }

    public static final List<ApprovalRequest> requests= Arrays.asList(
            new MunicipalityApprovalRequest(users.get(0), geoLocatables.get(4), municipalities.get(0)),
            new MunicipalityApprovalRequest(users.get(1), contents.get(0), municipalities.get(1)),
            new ContestApprovalRequest(users.get(1), (ContestContent) contents.get(9), contests.get(3))
    );

    public static void setUpMunicipalityRepository () {
        MunicipalityRepository.getInstance().addAll(municipalities);
    }

    public static void setUpUsers () {
        UserRepository.getInstance().addAll(users);
    }

    public static void setUpRquests () {
        ApprovalRequestRepository.getInstance().addAll(requests);
    }

    public static void setUpAllRepositories() {
        setUpMunicipalityRepository();
        setUpUsers();
        setUpRquests();
    }
}
