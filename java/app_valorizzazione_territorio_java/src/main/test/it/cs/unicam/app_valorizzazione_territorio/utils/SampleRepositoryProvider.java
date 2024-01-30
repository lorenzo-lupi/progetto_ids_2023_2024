package it.cs.unicam.app_valorizzazione_territorio.utils;

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

import java.util.*;

public class SampleRepositoryProvider {

    public static Municipality MACERATA, CAMERINO;
    public static User TURIST_1, TURIST_2, TURIST_3, CURATOR_CAMERINO, ENTERTAINER_CAMERINO, ENTERTAINER_MACERATA;
    public static GeoLocatable UNIVERSITY_CAMERINO, VIA_MADONNA_CARCERI, PIAZZA_LIBERTA, CORSA_SPADA,
            SEPTEMBER_FEST, PIZZERIA_ENJOY, BASILICA_SAN_VENANZIO, TORUR_STUDENTE, TRADIZIONE_SAN_VENANZIO;
    public static Content FOTO_SAN_VENANZIO, FOTO_PIAZZA_LIBERTA_1, FOTO_PIAZZA_LIBERTA_2, FOTO_PIZZA_MARGHERITA,
            MANIFESTO_CORSA_SPADA, FOTO_STRADE_MACERATA, FOTO_TORRE_CIVICA, FOTO_PIZZA_REGINA, FOTO_PITTURA_1,
            FOTO_PITTURA_2;

    public static Contest CONCORSO_FOTO_2024, CONCORSO_FOTO_2025, CONCORSO_FOTO_PIZZA, CONCORSO_PITTURA;
    public static ApprovalRequest RICHIESTA_PIAZZA_LIBERTA, RICHIESTA_FOTO_BASILICA, RICHIESTA_PITTURA_CAVOUR;

    private static boolean municipalitiesAreSetUp = false;
    private static boolean usersAreSetUp = false;
    private static boolean requestsAreSetUp = false;

    public static final List<Municipality> municipalities = new ArrayList<>();
    public static final List<User> users = new ArrayList<>();
    public static final List<GeoLocatable> geoLocatables = new ArrayList<>();
    public static final List<Contest> contests = new ArrayList<>();
    public static final List<Content> contents = new ArrayList<>();
    public static final List<ApprovalRequest> requests = new ArrayList<>();

    static {
        createObjects();
    }

    private static void createObjects() {

        municipalities.addAll(Arrays.asList(
                //0 //MACERATA
                new Municipality("Macerata", "Comune di Macerata",
                        new Position(43.29812657107886, 13.451878161920886),
                        new CoordinatesBox(new Position(43.317324, 13.409422),
                                new Position (43.271074, 13.499990)),
                        new ArrayList<>()),
                //1 //CAMERINO
                new Municipality("Camerino", "Comune di Camerino",
                        new Position(43.13644468556232, 13.067156069846892),
                        new CoordinatesBox(new Position(43.153712, 13.036414),
                                new Position (43.123261, 13.095768)),
                        new ArrayList<>())
        ));

        users.addAll(Arrays.asList(
                new User("Pippo00", "pippo00@gmail.com"),               //TURIST_1
                new User("Pluto01", "pluto01@gmail.com"),               //TURIST_2
                new User("Paperino02", "paperino02@bitmail.it"),        //TURIST_3
                new User("Pinco03", "pinco03@bitmail.it"),              //CURATOR_CAMERINO
                new User("Pallo04", "pallo04@blobmail.com"),            //ENTERTAINER_MACERATA
                new User("MarioRossi05", "mario.rossi06@blobmail.com")  //ENTERTAINER_CAMERINO
        ));

        geoLocatables.addAll(Arrays.asList(
                //0 //UNIVERSITY_CAMERINO //Municiplaity: Camerino
                new Attraction("Università di Camerino", "Università di Camerino",
                        new Position(43.13644468556232, 13.067156069846892),
                        municipalities.get(1), AttractionTypeEnum.BUILDING, users.get(2)),
                //1 //VIA_MADONNA_CARCERI //Municiplaity: Camerino
                new Attraction("Via Madonna delle Carceri", "Via Madonna delle Carceri",
                        new Position(43.140, 13.069),
                        municipalities.get(1), AttractionTypeEnum.OTHER, users.get(1)),
                //2 //PIAZZA_LIBERTA //Municiplaity: Macerata
                new Attraction("Piazza della Libertà", "Piazza della Libertà",
                        new Position(43.29812657107886, 13.451878161920886),
                        municipalities.get(0), AttractionTypeEnum.SQUARE, users.get(2)),
                //3 //CORSA_SPADA //Municiplaity: Camerino
                new Event("Corsa della Spada", "Celebrazione tradizionale della Corsa della Spada",
                        new Position(43.135812352706715, 13.068367879486194),
                        municipalities.get(1), new Date(124, 5, 17), new Date(124, 5, 24), users.get(1)),
                //4 //SEPTEMBER_FEST //Municiplaity: Macerata //Not approved
                new Event("September Fest", "Festa a tema Birra",
                        new Position(43.29812657107886, 13.451878161920886),
                        municipalities.get(0), new Date(124, 9, 1), new Date(124, 9, 30), users.get(2)),
                //5 //PIZZERIA_ENJOY //Municiplaity: Camerino
                new Activity("Pizzeria Enjoy", "Pizzeria",
                        new Position(43.143290511951314, 13.078617450882426),
                        municipalities.get(1), ActivityTypeEnum.RESTAURANT, users.get(2)),
                //6 //BASILICA_SAN_VENANZIO //Municiplaity: Camerino
                new Activity("Basilica di San Venanzio", "Basilica di San Venanzio",
                        new Position(43.137753115974135, 13.073411976140818),
                        municipalities.get(1), ActivityTypeEnum.CHURCH, users.get(1))
        ));

        geoLocatables.addAll(Arrays.asList(
                //7 //TOUR_STUDENTE //Municiplaity: Camerino
                new CompoundPoint("Tour dello studente", "Tour dello studente",
                        municipalities.get(1), CompoundPointTypeEnum.ITINERARY, Arrays.asList(
                        (PointOfInterest)geoLocatables.get(1),
                        (PointOfInterest)geoLocatables.get(0),
                        (PointOfInterest)geoLocatables.get(5)),
                        new ArrayList<>(), users.get(2)),
                //8 //TRADIZIONE_SAN_VENANZIO //Municiplaity: Camerino
                new CompoundPoint("Tradizione di San Venanzio", "Tradizione di San Venanzio",
                        municipalities.get(1), CompoundPointTypeEnum.EXPERIENCE, Arrays.asList(
                        (PointOfInterest)geoLocatables.get(6),
                        (PointOfInterest)geoLocatables.get(3)),
                        new ArrayList<>(), users.get(1))
        ));

        contests.addAll(Arrays.asList(
                //0 //CONCORSO_FOTO_2024 //Municiplaity: Macerata
                new ContestBase("Concorso fotografico annuale", users.get(4),
                        "Concorso fotografico generico edizione 2024", "Una foto per partecipante",
                        new Date(124, 1, 1), new Date(124, 10, 30),
                        new Date(124, 12, 31)),
                //1 //CONCORSO_FOTO_2025 //Municiplaity: Macerata
                new ContestBase("Concorso fotografico annuale", users.get(4),
                        "Concorso fotografico generico edizione 2025", "Una foto per partecipante",
                        new Date(125, 1, 1), new Date(125, 10, 30),
                        new Date(125, 12, 31)),
                //2 //CONCORSO_FOTO_PIZZA //Municiplaity: Camerino //GeoLocatable: Pizzeria Enjoy
                new GeoLocatableContestDecorator(new ContestBase("Migliore foto di pizza Novembre", users.get(5),
                        "Concorso fotografico per la migliore foto di pizza", "Una foto per partecipante",
                        new Date(124, 1, 1), new Date(124, 10, 30),
                        new Date(124, 12, 31)), geoLocatables.get(5)),
                //3 //CONCORSO_PITTURA //Municiplaity: Camerino
                new PrivateContestDecorator(new ContestBase("Concorso pittura dei paesaggi", users.get(5),
                        "Concorso di pittura dei paesaggi",
                        "Foto di una pittura fatta a mano di un pesaggio di Camerino",
                        new Date(126, 1, 1), new Date(126, 10, 30),
                        new Date(126, 12, 31)), List.of(users.get(0), users.get(1), users.get(2)))
        ));

        contents.addAll(Arrays.asList(
                //0 //FOTO_SAN_VENANZIO //Municiplality: Camerino //GeoLocatable: Basilica di San Venanzio //Not approved
                new PointOfInterestContent("Foto della basilica di San Venanzio",
                        (PointOfInterest)geoLocatables.get(6), new ArrayList<>(), users.get(0)),
                //1 //FOTO_PIAZZA_LIBERTA_1 //Municiplality: Macerata //GeoLocatable: Piazza della Libertà
                new PointOfInterestContent("Foto della piazza della libertà",
                        (PointOfInterest)geoLocatables.get(2), new ArrayList<>(), users.get(0)),
                //2 //FOTO_PIAZZA_LIBERTA_2 //Municiplality: Macerata //GeoLocatable: Piazza della Libertà
                new PointOfInterestContent("Foto della piazza della libertà di notte",
                        (PointOfInterest)geoLocatables.get(2), new ArrayList<>(), users.get(1)),
                //3 //FOTO_PIZZA_MARGHERITA //Municiplality: Camerino //GeoLocatable: Pizzeria Enjoy
                new PointOfInterestContent("Foto delle pizze margerita",
                        (PointOfInterest)geoLocatables.get(5), new ArrayList<>(), users.get(2)),
                //4 //MANIFESTO_CORSA_SPADA //Municiplality: Camerino //GeoLocatable: Corsa della Spada
                new PointOfInterestContent("Manifesto della corsa della spada",
                        (PointOfInterest)geoLocatables.get(3), new ArrayList<>(), users.get(3)),
                //5 //FOTO_STRADE_MACERATA //Municiplality: Macerata //Contest: Concorso fotografico annuale 2024
                new ContestContent("Foto per le strade di Macerata",
                        contests.get(0), new ArrayList<>(), users.get(0)),
                //6 //FOTO_TORRE_CIVICA //Municiplality: Macerata //Contest: Concorso fotografico annuale 2024
                new ContestContent("Foto della torre civica",
                        contests.get(0), new ArrayList<>(), users.get(1)),
                //7 //FOTO_PIZZA_REGINA //Municiplality: Camerino //Contest: Migliore foto di pizza Novembre
                new ContestContent("Foto della pizza Regina Sbagliata",
                        contests.get(2), new ArrayList<>(), users.get(2)),
                //8 //FOTO_PITTURA_1 //Municiplality: Camerino //Contest: Concorso pittura dei paesaggi
                new ContestContent("Pittura della Basilica di San Venanzio",
                        contests.get(3), new ArrayList<>(), users.get(0)),
                //9 //FOTO_PITTURA_2 //Municiplality: Camerino //Contest: Concorso pittura dei paesaggi //Not approved
                new ContestContent("Pittura di Piazza Cavour",
                        contests.get(3), new ArrayList<>(), users.get(1))
        ));

        requests.addAll(Arrays.asList(
                //0 //RICHIESTA_PIAZZA_LIBERTA //User: Pippo01 //GeoLocatable: Piazza della Libertà //Municipality: Macerata
                new MunicipalityApprovalRequest(users.get(0), geoLocatables.get(4), municipalities.get(0)),
                //1 //RICHIESTA_FOTO_BASILICA //User: Pluto02 //Content: Foto di Basilica di San Venanzio //GeoLocatable: Basilica di San Venanzio //Municipality: Camerino
                new MunicipalityApprovalRequest(users.get(1), contents.get(0), municipalities.get(1)),
                //2 //RICHIESTA_PITTURA_CAVOUR //User: Pluto02 //Content: Pittura piazza Cavour //Contest: Concorso pittura paessaggi //Municipality: Camerino
                new ContestApprovalRequest(users.get(1), (ContestContent) contents.get(9), contests.get(3))
        ));

        MACERATA = municipalities.get(0);
        CAMERINO = municipalities.get(1);

        TURIST_1 = users.get(0);
        TURIST_2 = users.get(1);
        TURIST_3 = users.get(2);
        CURATOR_CAMERINO = users.get(3);
        ENTERTAINER_MACERATA = users.get(4);
        ENTERTAINER_CAMERINO = users.get(5);

        UNIVERSITY_CAMERINO = geoLocatables.get(0);
        VIA_MADONNA_CARCERI = geoLocatables.get(1);
        PIAZZA_LIBERTA = geoLocatables.get(2);
        CORSA_SPADA = geoLocatables.get(3);
        SEPTEMBER_FEST = geoLocatables.get(4);
        PIZZERIA_ENJOY = geoLocatables.get(5);
        BASILICA_SAN_VENANZIO = geoLocatables.get(6);
        TORUR_STUDENTE = geoLocatables.get(7);
        TRADIZIONE_SAN_VENANZIO = geoLocatables.get(8);

        FOTO_SAN_VENANZIO = contents.get(0);
        FOTO_PIAZZA_LIBERTA_1 = contents.get(1);
        FOTO_PIAZZA_LIBERTA_2 = contents.get(2);
        FOTO_PIZZA_MARGHERITA = contents.get(3);
        MANIFESTO_CORSA_SPADA = contents.get(4);
        FOTO_STRADE_MACERATA = contents.get(5);
        FOTO_TORRE_CIVICA = contents.get(6);
        FOTO_PIZZA_REGINA = contents.get(7);
        FOTO_PITTURA_1 = contents.get(8);
        FOTO_PITTURA_2 = contents.get(9);

        CONCORSO_FOTO_2024 = contests.get(0);
        CONCORSO_FOTO_2025 = contests.get(1);
        CONCORSO_FOTO_PIZZA = contests.get(2);
        CONCORSO_PITTURA = contests.get(3);

        RICHIESTA_PIAZZA_LIBERTA = requests.get(0);
        RICHIESTA_FOTO_BASILICA = requests.get(1);
        RICHIESTA_PITTURA_CAVOUR = requests.get(2);

        CURATOR_CAMERINO.addRole(CAMERINO, RoleTypeEnum.CURATOR);
        ENTERTAINER_MACERATA.addRole(MACERATA, RoleTypeEnum.ENTERTAINER);
        ENTERTAINER_CAMERINO.addRole(CAMERINO, RoleTypeEnum.ENTERTAINER);

        CAMERINO.addGeoLocatable(UNIVERSITY_CAMERINO); UNIVERSITY_CAMERINO.approve();
        CAMERINO.addGeoLocatable(VIA_MADONNA_CARCERI); VIA_MADONNA_CARCERI.approve();
        MACERATA.addGeoLocatable(PIAZZA_LIBERTA); PIAZZA_LIBERTA.approve();
        CAMERINO.addGeoLocatable(CORSA_SPADA); CORSA_SPADA.approve();
        MACERATA.addGeoLocatable(SEPTEMBER_FEST);
        CAMERINO.addGeoLocatable(PIZZERIA_ENJOY); PIZZERIA_ENJOY.approve();
        CAMERINO.addGeoLocatable(BASILICA_SAN_VENANZIO); BASILICA_SAN_VENANZIO.approve();
        CAMERINO.addGeoLocatable(TORUR_STUDENTE); TORUR_STUDENTE.approve();
        CAMERINO.addGeoLocatable(TRADIZIONE_SAN_VENANZIO); TRADIZIONE_SAN_VENANZIO.approve();

        MACERATA.addContest(CONCORSO_FOTO_2024);
        MACERATA.addContest(CONCORSO_FOTO_2025);
        CAMERINO.addContest(CONCORSO_FOTO_PIZZA);
        CAMERINO.addContest(CONCORSO_PITTURA);

        ((PointOfInterest)BASILICA_SAN_VENANZIO).addContent((PointOfInterestContent)FOTO_SAN_VENANZIO);
        ((PointOfInterest)PIAZZA_LIBERTA).addContent((PointOfInterestContent)FOTO_PIAZZA_LIBERTA_1); FOTO_PIAZZA_LIBERTA_1.approve();
        ((PointOfInterest)PIAZZA_LIBERTA).addContent((PointOfInterestContent)FOTO_PIAZZA_LIBERTA_2); FOTO_PIAZZA_LIBERTA_2.approve();
        ((PointOfInterest)PIZZERIA_ENJOY).addContent((PointOfInterestContent)FOTO_PIZZA_MARGHERITA); FOTO_PIZZA_MARGHERITA.approve();
        ((PointOfInterest)CORSA_SPADA).addContent((PointOfInterestContent)MANIFESTO_CORSA_SPADA); MANIFESTO_CORSA_SPADA.approve();

        CONCORSO_FOTO_2024.getProposalRequests().proposeContent((ContestContent)FOTO_STRADE_MACERATA); FOTO_STRADE_MACERATA.approve();
        CONCORSO_FOTO_2024.getProposalRequests().proposeContent((ContestContent)FOTO_TORRE_CIVICA); FOTO_TORRE_CIVICA.approve();
        CONCORSO_FOTO_PIZZA.getProposalRequests().proposeContent((ContestContent)FOTO_PIZZA_REGINA); FOTO_PIZZA_REGINA.approve();
        CONCORSO_PITTURA.getProposalRequests().proposeContent((ContestContent)FOTO_PITTURA_1); FOTO_PITTURA_1.approve();
        CONCORSO_PITTURA.getProposalRequests().proposeContent((ContestContent)FOTO_PITTURA_2);
    }

    private static void clearObjects() {
        municipalities.clear();
        users.clear();
        geoLocatables.clear();
        contests.clear();
        contents.clear();
        requests.clear();
    }

    public static void setUpMunicipalitiesRepository() {
        if (!municipalitiesAreSetUp) {
            MunicipalityRepository.getInstance().addAll(municipalities);
            municipalitiesAreSetUp = true;
        }
    }

    public static void setUpUsersRepository() {
        if (!usersAreSetUp) {
            UserRepository.getInstance().addAll(users);
            usersAreSetUp = true;
        }
    }

    public static void setUpRequest5esRepositories() {
        if (!requestsAreSetUp) {
            ApprovalRequestRepository.getInstance().addAll(requests);
            requestsAreSetUp = true;
        }
    }

    public static void setUpAllRepositories() {
        setUpMunicipalitiesRepository();
        setUpUsersRepository();
        setUpRequest5esRepositories();
    }

    public static void clearMunicipalitesRepository() {
        MunicipalityRepository.getInstance().clear();
        municipalitiesAreSetUp = false;
    }

    public static void clearUsersRepository() {
        UserRepository.getInstance().clear();
        usersAreSetUp = false;
    }

    public static void clearRequestsRepositories() {
        ApprovalRequestRepository.getInstance().clear();
        requestsAreSetUp = false;
    }

    public static void clearAllRepositories() {
        clearMunicipalitesRepository();
        clearUsersRepository();
        clearRequestsRepositories();
        clearObjects();
        createObjects();
    }
}
