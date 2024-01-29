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

    public static final List<Municipality> municipalities = new ArrayList<>();
    public static final List<User> users = new ArrayList<>();
    public static final List<GeoLocatable> geoLocatables = new ArrayList<>();
    public static final List<Contest> contests = new ArrayList<>();
    public static final List<Content> contents = new ArrayList<>();
    public static final List<ApprovalRequest> requests = new ArrayList<>();


    static {

        municipalities.addAll(Arrays.asList(
                new Municipality("Macerata", "Comune di Macerata",
                        new Position(43.29812657107886, 13.451878161920886),
                        new CoordinatesBox(new Position(43.317324, 13.409422),
                                new Position (43.271074, 13.499990)),
                        new ArrayList<>()),
                new Municipality("Camerino", "Comune di Camerino",
                        new Position(43.13644468556232, 13.067156069846892),
                        new CoordinatesBox(new Position(43.153712, 13.036414),
                                new Position (43.123261, 13.095768)),
                        new ArrayList<>())
        ));

        users.addAll(Arrays.asList(
                new User("Pippo00", "pippo00@gmail.com"),
                new User("Pluto01", "pluto01@gmail.com"),
                new User("Paperino02", "paperino02@bitmail.it"),
                new User("Pinco03", "pinco03@bitmail.it"),              //Curator
                new User("Pallo04", "pallo04@blobmail.com"),            //Entertainer
                new User("MarioRossi05", "mario.rossi06@blobmail.com")  //Entertainer
        ));

        geoLocatables.addAll(Arrays.asList(
                //0 //Municiplaity: Camerino
                new Attraction("Università di Camerino", "Università di Camerino",
                        new Position(43.13644468556232, 13.067156069846892),
                        municipalities.get(1), AttractionTypeEnum.BUILDING),
                //1 //Municiplaity: Camerino
                new Attraction("Via Madonna delle Carceri", "Via Madonna delle Carceri",
                        new Position(43.140, 13.069),
                        municipalities.get(1), AttractionTypeEnum.OTHER),
                //2 //Municiplaity: Macerata
                new Attraction("Piazza della Libertà", "Piazza della Libertà",
                        new Position(43.29812657107886, 13.451878161920886),
                        municipalities.get(0), AttractionTypeEnum.SQUARE),
                //3 //Municiplaity: Camerino
                new Event("Corsa della Spada", "Celebrazione tradizionale della Corsa della Spada",
                        new Position(43.135812352706715, 13.068367879486194),
                        municipalities.get(1), new Date(124, 5, 17), new Date(124, 5, 24)),
                //4 //Municiplaity: Macerata //Not approved
                new Event("September Fest", "Festa a tema Birra",
                        new Position(43.29812657107886, 13.451878161920886),
                        municipalities.get(0), new Date(124, 9, 1), new Date(124, 9, 30)),
                //5 //Municiplaity: Camerino
                new Activity("Pizzeria Enjoy", "Pizzeria",
                        new Position(43.143290511951314, 13.078617450882426),
                        municipalities.get(1), ActivityTypeEnum.RESTAURANT),
                //6 //Municiplaity: Camerino
                new Activity("Basilica di San Venanzio", "Basilica di San Venanzio",
                        new Position(43.137753115974135, 13.073411976140818),
                        municipalities.get(1), ActivityTypeEnum.CHURCH)
        ));

        geoLocatables.addAll(Arrays.asList(
                //7 //Municiplaity: Camerino
                new CompoundPoint("Tour dello studente", "Tour dello studente",
                        municipalities.get(1), CompoundPointTypeEnum.ITINERARY, Arrays.asList(
                        (PointOfInterest)geoLocatables.get(1),
                        (PointOfInterest)geoLocatables.get(0),
                        (PointOfInterest)geoLocatables.get(5)),
                        new ArrayList<>()),
                //8 //Municiplaity: Camerino
                new CompoundPoint("Tradizione di San Venanzio", "Tradizione di San Venanzio",
                        municipalities.get(1), CompoundPointTypeEnum.EXPERIENCE, Arrays.asList(
                        (PointOfInterest)geoLocatables.get(6),
                        (PointOfInterest)geoLocatables.get(3)),
                        new ArrayList<>())
        ));

        contests.addAll(Arrays.asList(
                //0 //Municiplaity: Macerata
                new ContestBase("Concorso fotografico annuale", users.get(4),
                        "Concorso fotografico generico edizione 2024", "Una foto per partecipante",
                        new Date(124, 1, 1), new Date(124, 10, 30),
                        new Date(124, 12, 31)),
                //1 //Municiplaity: Macerata
                new ContestBase("Concorso fotografico annuale", users.get(4),
                        "Concorso fotografico generico edizione 2025", "Una foto per partecipante",
                        new Date(125, 1, 1), new Date(125, 10, 30),
                        new Date(125, 12, 31)),
                //2 //Municiplaity: Camerino //GeoLocatable: Pizzeria Enjoy
                new GeoLocatableContestDecorator(new ContestBase("Migliore foto di pizza Novembre", users.get(5),
                        "Concorso fotografico per la migliore foto di pizza", "Una foto per partecipante",
                        new Date(124, 1, 1), new Date(124, 10, 30),
                        new Date(124, 12, 31)), geoLocatables.get(5)),
                //3 //Municiplaity: Camerino
                new PrivateContestDecorator(new ContestBase("Concorso pittura dei paesaggi", users.get(5),
                        "Concorso di pittura dei paesaggi",
                        "Foto di una pittura fatta a mano di un pesaggio di Camerino",
                        new Date(126, 1, 1), new Date(126, 10, 30),
                        new Date(126, 12, 31)), List.of(users.get(0), users.get(1), users.get(2)))
        ));

        contents.addAll(Arrays.asList(
                //0 //Municiplality: Camerino //GeoLocatable: Basilica di San Venanzio //Not approved
                new PointOfInterestContent("Foto della basilica di San Venanzio",
                        (PointOfInterest)geoLocatables.get(6), new ArrayList<>(), users.get(0)),
                //1 //Municiplality: Macerata //GeoLocatable: Piazza della Libertà
                new PointOfInterestContent("Foto della piazza della libertà",
                        (PointOfInterest)geoLocatables.get(2), new ArrayList<>(), users.get(0)),
                //2 //Municiplality: Macerata //GeoLocatable: Piazza della Libertà
                new PointOfInterestContent("Foto della piazza della libertà di notte",
                        (PointOfInterest)geoLocatables.get(2), new ArrayList<>(), users.get(1)),
                //3 //Municiplality: Camerino //GeoLocatable: Pizzeria Enjoy
                new PointOfInterestContent("Foto delle pizze margerita",
                        (PointOfInterest)geoLocatables.get(5), new ArrayList<>(), users.get(2)),
                //4 //Municiplality: Camerino //GeoLocatable: Corsa della Spada
                new PointOfInterestContent("Manifesto della corsa della spada",
                        (PointOfInterest)geoLocatables.get(3), new ArrayList<>(), users.get(3)),
                //5 //Municiplality: Macerata //Contest: Concorso fotografico annuale 2024
                new ContestContent("Foto per le strade di Macerata",
                        contests.get(0), new ArrayList<>(), users.get(0)),
                //6 //Municiplality: Macerata //Contest: Concorso fotografico annuale 2024
                new ContestContent("Foto della torre civica",
                        contests.get(0), new ArrayList<>(), users.get(1)),
                //7 //Municiplality: Camerino //Contest: Migliore foto di pizza Novembre
                new ContestContent("Foto della pizza Regina Sbagliata",
                        contests.get(2), new ArrayList<>(), users.get(2)),
                //8 //Municiplality: Camerino //Contest: Concorso pittura dei paesaggi
                new ContestContent("Pittura della Basilica di San Venanzio",
                        contests.get(3), new ArrayList<>(), users.get(0)),
                //9 //Municiplality: Camerino //Contest: Concorso pittura dei paesaggi //Not approved
                new ContestContent("Pittura di Piazza Cavour",
                        contests.get(3), new ArrayList<>(), users.get(1))
        ));

        requests.addAll(Arrays.asList(
                //0 //User: Pippo01 //GeoLocatable: Piazza della Libertà //Municipality: Macerata
                new MunicipalityApprovalRequest(users.get(0), geoLocatables.get(4), municipalities.get(0)),
                //1 //User: Pluto02 //GeoLocatable: Basilica di San Venanzio //Municipality: Camerino
                new MunicipalityApprovalRequest(users.get(1), contents.get(0), municipalities.get(1)),
                //2 //User: Pluto02 //Contest: Concorso pittura paessaggi //Municipality: Camerino
                new ContestApprovalRequest(users.get(1), (ContestContent) contents.get(9), contests.get(3))
        ));

        users.get(3).addRole(municipalities.get(1), RoleTypeEnum.CURATOR);
        users.get(4).addRole(municipalities.get(0), RoleTypeEnum.ENTERTAINER);
        users.get(5).addRole(municipalities.get(1), RoleTypeEnum.ENTERTAINER);

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
