package it.cs.unicam.app_valorizzazione_territorio;

import it.cs.unicam.app_valorizzazione_territorio.model.*;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.ContestBase;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.GeoLocatableContestDecorator;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.PrivateContestDecorator;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.Event;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.RequestFactory;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.List;

@Component
public class SampleRepositoryConstructor {

    @Autowired
    private MunicipalityJpaRepository municipalityJpaRepository;
    @Autowired
    private RoleJpaRepository roleJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private GeoLocatableJpaRepository geoLocatableJpaRepository;
    @Autowired
    private ContestJpaRepository contestJpaRepository;
    @Autowired
    private ContentJpaRepository contentJpaRepository;
    @Autowired
    private RequestJpaRepository requestJpaRepository;
    @Autowired
    private NotificationJpaRepository notificationJpaRepository;
    @Autowired
    private MessageJpaRepository messageJpaRepository;

    @PostConstruct
    public void init() {
        setUpMunicipalities(municipalityJpaRepository, roleJpaRepository);
        setUpUsers(userJpaRepository);
        setUpGeoLocatables(geoLocatableJpaRepository);
        setUpContests(contestJpaRepository);
        setUpContents(contentJpaRepository);
        setUpRequests(requestJpaRepository);
        setUpNotifications(notificationJpaRepository);
        setUpMessages(messageJpaRepository);
    }

    @Value("${fileResources.path}")
    private String filePath;
    public boolean areMunicipalitiesSet = false;
    public boolean areUsersSet = false;
    public boolean areGeoLocatablesSet = false;
    public boolean areContestsSet = false;
    public boolean areContentsSet = false;
    public boolean areRequestsSet = false;
    public boolean areNotificationsSet = false;
    public boolean areMessagesSet = false;

    public Municipality MACERATA, CAMERINO;
    public Map<Municipality, Map<AuthorizationEnum, Role>> roles = new HashMap<>();
    public User TURIST_1, TURIST_2, TURIST_3, CURATOR_CAMERINO, ENTERTAINER_CAMERINO, ENTERTAINER_MACERATA, ENTERTAINER_TEST, ADMINISTRATOR_CAMERINO;
    public GeoLocatable UNIVERSITY_CAMERINO, VIA_MADONNA_CARCERI, PIAZZA_LIBERTA, CORSA_SPADA,
            SEPTEMBER_FEST, PIZZERIA_ENJOY, BASILICA_SAN_VENANZIO, TOUR_STUDENTE, TRADIZIONE_SAN_VENANZIO, GAS_FACILITY;
    public Contest CONCORSO_FOTO_2024, CONCORSO_FOTO_2025, CONCORSO_FOTO_PIZZA, CONCORSO_PITTURA;
    public Content<?> FOTO_SAN_VENANZIO, FOTO_PIAZZA_LIBERTA_1, FOTO_PIAZZA_LIBERTA_2, FOTO_PIZZA_MARGHERITA,
            MANIFESTO_CORSA_SPADA, FOTO_STRADE_MACERATA, FOTO_TORRE_CIVICA, FOTO_PIZZA_REGINA, FOTO_PITTURA_1,
            FOTO_PITTURA_2;

    public Request<?> RICHIESTA_PIAZZA_LIBERTA, RICHIESTA_FOTO_BASILICA, RICHIESTA_PITTURA_CAVOUR;
    public Message MESSAGGIO_1, MESSAGGIO_2;


    public void clearMunicipalities(MunicipalityJpaRepository repository, RoleJpaRepository roleRepository){
        roleRepository.deleteAll();
        roleRepository.flush();
        repository.deleteAll();
        repository.flush();
        areMunicipalitiesSet = false;
    }

    public void setUpMunicipalities(MunicipalityJpaRepository repository,
                                           RoleJpaRepository roleRepository){
        clearMunicipalities(repository, roleRepository);

        MACERATA = repository.saveAndFlush(new Municipality("Macerata", "Comune di Macerata",
                new Position(43.29812657107886, 13.451878161920886),
                new CoordinatesBox(new Position(43.317324, 13.409422),
                        new Position(43.271074, 13.499990)),
                new ArrayList<>(List.of(new File(filePath + "macerata.jpg")))));
        CAMERINO = repository.saveAndFlush(new Municipality("Camerino", "Comune di Camerino",
                new Position(43.13644468556232, 13.067156069846892),
                new CoordinatesBox(new Position(43.153712, 13.036414),
                        new Position(43.123261, 13.095768)),
                new ArrayList<>(List.of(new File(filePath + "camerino.jpg")))));

        roles = new HashMap<>();
        roles.put(MACERATA, new EnumMap<>(AuthorizationEnum.class));
        roles.put(CAMERINO, new EnumMap<>(AuthorizationEnum.class));
        Arrays.stream(AuthorizationEnum.values()).forEach(auth -> {
            roles.get(MACERATA).put(auth, roleRepository.save(new Role(MACERATA, auth)));
            roles.get(CAMERINO).put(auth, roleRepository.save(new Role(CAMERINO, auth)));
        });

        repository.flush();
        areMunicipalitiesSet = true;
    }

    public void clearUsers(UserJpaRepository repository){
        repository.deleteAll();
        repository.flush();
        areUsersSet = false;
    }

    public void setUpUsers(UserJpaRepository repository) {
        clearUsers(repository);
        if (!areMunicipalitiesSet)
            throw new IllegalStateException("Municipalities not set");

        List<User> users = new ArrayList<>(Arrays.asList(
                /* 0 */ new User("Pippo00", "pippo00@gmail.com", "Testpassword01"),               //TURIST_1
                /* 1 */ new User("Pluto01", "pluto01@gmail.com", "Testpassword02"),               //TURIST_2
                /* 2 */ new User("Paperino02", "paperino02@bitmail.it", "Testpassword02"),        //TURIST_3
                /* 3 */ new User("Pinco03", "pinco03@bitmail.it", "Testpassword03"),              //CURATOR_CAMERINO
                /* 4 */ new User("Pallo04", "pallo04@blobmail.com", "Testpassword04"),            //ENTERTAINER_MACERATA
                /* 5 */ new User("MarioRossi05", "mario.rossi06@blobmail.com", "Testpassword05"), //ENTERTAINER_CAMERINO
                /* 6 */ new User("Zeb89", "mario.rossi06@blobmail.com", "Testpassword06"),        //ENTERTAINER_TEST
                /* 7*/  new User("Admin", "ace.gamer@nonpagotasseinitalia.mt", "Testpassword07")  //ADMINISTRATOR_CAMERINO
        ));

        users.get(3).addRole(roles.get(CAMERINO).get(AuthorizationEnum.CURATOR));
        users.get(4).addRole(roles.get(MACERATA).get(AuthorizationEnum.ENTERTAINER));
        users.get(5).addRole(roles.get(CAMERINO).get(AuthorizationEnum.ENTERTAINER));
        users.get(7).addRole(roles.get(CAMERINO).get(AuthorizationEnum.ADMINISTRATOR));
        TURIST_1 = repository.save(users.get(0));
        TURIST_2 = repository.save(users.get(1));
        TURIST_3 = repository.save(users.get(2));
        CURATOR_CAMERINO = repository.save(users.get(3));
        ENTERTAINER_MACERATA = repository.save(users.get(4));
        ENTERTAINER_CAMERINO = repository.save(users.get(5));
        ENTERTAINER_TEST = repository.save(users.get(6));
        ADMINISTRATOR_CAMERINO = repository.save(users.get(7));

        repository.flush();
        areUsersSet = true;
    }

    public void clearGeoLocatables(GeoLocatableJpaRepository repository){
        repository.deleteAll();
        repository.flush();
        areGeoLocatablesSet = false;
    }
    public void setUpGeoLocatables(GeoLocatableJpaRepository repository){
        clearGeoLocatables(repository);
        if(!areUsersSet || !areMunicipalitiesSet)
            throw new IllegalStateException("User and Municipality repositories are not set");

        List<GeoLocatable> geoLocatables = new LinkedList<>();
        geoLocatables.addAll(Arrays.asList(
                //0 //UNIVERSITY_CAMERINO //Municiplaity: Camerino
                new Attraction("Università di Camerino", "Università di Camerino",
                        new Position(43.13644468556232, 13.067156069846892),
                        CAMERINO, AttractionTypeEnum.BUILDING, TURIST_3),
                //1 //VIA_MADONNA_CARCERI //Municiplaity: Camerino
                new Attraction("Via Madonna delle Carceri", "Via Madonna delle Carceri",
                        new Position(43.140, 13.069),
                        CAMERINO, AttractionTypeEnum.OTHER, TURIST_2),
                //2 //PIAZZA_LIBERTA //Municiplaity: Macerata
                new Attraction("Piazza della Libertà", "Piazza della Libertà",
                        new Position(43.29812657107886, 13.451878161920886),
                        MACERATA, AttractionTypeEnum.SQUARE, TURIST_3),
                //3 //CORSA_SPADA //Municiplaity: Camerino
                new Event("Corsa della Spada", "Celebrazione tradizionale della Corsa della Spada",
                        new Position(43.135812352706715, 13.068367879486194),
                        CAMERINO, new Date(124, 5, 17), new Date(124, 5, 24),TURIST_2),
                //4 //SEPTEMBER_FEST //Municiplaity: Macerata //Not approved
                new Event("September Fest", "Festa a tema Birra",
                        new Position(43.29812657107886, 13.451878161920886),
                        MACERATA, new Date(124, 9, 1), new Date(124, 9, 30), TURIST_3),
                //5 //PIZZERIA_ENJOY //Municiplaity: Camerino
                new Activity("Pizzeria Enjoy", "Pizzeria",
                        new Position(43.143290511951314, 13.078617450882426),
                        CAMERINO, ActivityTypeEnum.RESTAURANT, TURIST_3),
                //6 //BASILICA_SAN_VENANZIO //Municiplaity: Camerino
                new Activity("Basilica di San Venanzio", "Basilica di San Venanzio",
                        new Position(43.137753115974135, 13.073411976140818),
                        CAMERINO, ActivityTypeEnum.CHURCH, TURIST_2),
                //7 //GAS_FACILITY //Municiplaity: Camerino
                new Attraction("Gas facility", "Gas facility Camerino",
                        new Position(43.1450445, 13.0893363),
                        CAMERINO, AttractionTypeEnum.OTHER,
                        TURIST_2)
        ));
        geoLocatables.get(0).approve();
        UNIVERSITY_CAMERINO = repository.save(geoLocatables.get(0));
        geoLocatables.get(1).approve();
        VIA_MADONNA_CARCERI = repository.save(geoLocatables.get(1));

        PIAZZA_LIBERTA = repository.save(geoLocatables.get(2));
        geoLocatables.get(3).approve();
        CORSA_SPADA = repository.save(geoLocatables.get(3));
        geoLocatables.get(4).approve();
        SEPTEMBER_FEST = repository.save(geoLocatables.get(4));
        geoLocatables.get(5).approve();
        PIZZERIA_ENJOY = repository.save(geoLocatables.get(5));
        geoLocatables.get(6).approve();
        BASILICA_SAN_VENANZIO = repository.save(geoLocatables.get(6));
        geoLocatables.get(7).approve();
        GAS_FACILITY = repository.save(geoLocatables.get(7));

        CAMERINO.addGeoLocatable(UNIVERSITY_CAMERINO);
        CAMERINO.addGeoLocatable(VIA_MADONNA_CARCERI);
        MACERATA.addGeoLocatable(PIAZZA_LIBERTA);
        CAMERINO.addGeoLocatable(CORSA_SPADA);
        MACERATA.addGeoLocatable(SEPTEMBER_FEST);
        CAMERINO.addGeoLocatable(PIZZERIA_ENJOY);
        CAMERINO.addGeoLocatable(BASILICA_SAN_VENANZIO);
        CAMERINO.addGeoLocatable(GAS_FACILITY);

        geoLocatables.addAll(Arrays.asList(new CompoundPoint("Tour dello studente", "Tour dello studente",
                        CAMERINO, CompoundPointTypeEnum.ITINERARY, Arrays.asList(
                        (PointOfInterest) VIA_MADONNA_CARCERI,
                        (PointOfInterest) UNIVERSITY_CAMERINO,
                        (PointOfInterest) PIZZERIA_ENJOY),
                        new ArrayList<>(), TURIST_2),
                new CompoundPoint("Tradizione di San Venanzio", "Tradizione di San Venanzio",
                        CAMERINO, CompoundPointTypeEnum.EXPERIENCE, Arrays.asList(
                        (PointOfInterest) BASILICA_SAN_VENANZIO,
                        (PointOfInterest) CORSA_SPADA),
                        new ArrayList<>(), TURIST_2)));

        geoLocatables.get(8).approve();
        TOUR_STUDENTE = repository.save(geoLocatables.get(8));
        geoLocatables.get(9).approve();
        TRADIZIONE_SAN_VENANZIO = repository.save(geoLocatables.get(9));

        CAMERINO.addGeoLocatable(TOUR_STUDENTE);
        CAMERINO.addGeoLocatable(TRADIZIONE_SAN_VENANZIO);

        repository.flush();
        areGeoLocatablesSet = true;
    }

    public void clearContests(ContestJpaRepository repository){
        repository.deleteAll(repository.findAllByValidTrue());
        repository.flush();
        areContestsSet = false;
    }

    public void setUpContests(ContestJpaRepository repository){
        clearContests(repository);
        if(!areUsersSet || !areMunicipalitiesSet)
            throw new IllegalStateException("User and Municipality repositories are not set");

        List<Contest> contests = new ArrayList<>();
        contests.addAll(Arrays.asList(
                //0 //CONCORSO_FOTO_2024 //Municiplaity: Macerata
                new ContestBase("Concorso fotografico annuale", ENTERTAINER_MACERATA,
                        "Concorso fotografico generico edizione 2024", "Una foto per partecipante",
                        new Date(124, 0, 1), new Date(124, 9, 30),
                        new Date(124, 11, 31), MACERATA),
                //1 //CONCORSO_FOTO_2025 //Municiplaity: Macerata
                new ContestBase("Concorso fotografico annuale", ENTERTAINER_MACERATA,
                        "Concorso fotografico generico edizione 2025", "Una foto per partecipante",
                        new Date(125, 0, 1), new Date(125, 9, 30),
                        new Date(125, 11, 31), MACERATA),
                //2 //CONCORSO_FOTO_PIZZA //Municipality: Camerino //GeoLocatable: Pizzeria Enjoy
                new GeoLocatableContestDecorator(new ContestBase("Migliore foto di pizza Novembre", ENTERTAINER_CAMERINO,
                        "Concorso fotografico per la migliore foto di pizza", "Una foto per partecipante",
                        new Date(124, 0, 1), new Date(124, 9, 30),
                        new Date(124, 11, 31), CAMERINO), PIZZERIA_ENJOY),
                //3 //CONCORSO_PITTURA //Municipality: Camerino
                new PrivateContestDecorator(new ContestBase("Concorso pittura dei paesaggi", ENTERTAINER_CAMERINO,
                        "Concorso di pittura dei paesaggi",
                        "Foto di una pittura fatta a mano di un pesaggio di Camerino",
                        new Date(124, 0, 1), new Date(124, 9, 30),
                        new Date(124, 11, 31), CAMERINO), List.of(TURIST_1, TURIST_2, TURIST_3))
        ));

        CONCORSO_FOTO_2024 = repository.save(contests.get(0));
        CONCORSO_FOTO_2025 = repository.save(contests.get(1));
        CONCORSO_FOTO_PIZZA = repository.save(contests.get(2));
        CONCORSO_PITTURA = repository.save(contests.get(3));

        MACERATA.addContest(CONCORSO_FOTO_2024);
        MACERATA.addContest(CONCORSO_FOTO_2025);
        CAMERINO.addContest(CONCORSO_FOTO_PIZZA);
        CAMERINO.addContest(CONCORSO_PITTURA);

        repository.flush();
        areContestsSet = true;
    }

    public void clearContents(ContentJpaRepository repository){
        repository.deleteAll();
        repository.flush();
        areContentsSet = false;
    }

    public void setUpContents(ContentJpaRepository repository){
        clearContents(repository);
        if(!areContestsSet || !areGeoLocatablesSet)
            throw new IllegalStateException("Contests and GeoLocatable repositories are not set");

        List<Content<?>> contents = new ArrayList<>();
        contents.addAll(Arrays.asList(
                //0 //FOTO_SAN_VENANZIO //Municipality: Camerino //GeoLocatable: Basilica di San Venanzio //Not approved
                new PointOfInterestContent("Foto della basilica di San Venanzio",
                        (PointOfInterest) BASILICA_SAN_VENANZIO, new ArrayList<>(), TURIST_1),
                //1 //FOTO_PIAZZA_LIBERTA_1 //Municipality: Macerata //GeoLocatable: Piazza della Libertà
                new PointOfInterestContent("Foto della piazza della libertà",
                        (PointOfInterest) PIAZZA_LIBERTA, new ArrayList<>(), TURIST_1),
                //2 //FOTO_PIAZZA_LIBERTA_2 //Municipality: Macerata //GeoLocatable: Piazza della Libertà
                new PointOfInterestContent("Foto della piazza della libertà di notte",
                        (PointOfInterest) PIAZZA_LIBERTA, new ArrayList<>(), TURIST_2),
                //3 //FOTO_PIZZA_MARGHERITA //Municipality: Camerino //GeoLocatable: Pizzeria Enjoy
                new PointOfInterestContent("Foto delle pizze margerita",
                        (PointOfInterest) PIZZERIA_ENJOY, new ArrayList<>(), TURIST_3),
                //4 //MANIFESTO_CORSA_SPADA //Municipality: Camerino //GeoLocatable: Corsa della Spada
                new PointOfInterestContent("Manifesto della corsa della spada",
                        (PointOfInterest) CORSA_SPADA, new ArrayList<>(), CURATOR_CAMERINO),
                //5 //FOTO_STRADE_MACERATA //Municipality: Macerata //Contest: Concorso fotografico annuale 2024
                new ContestContent("Foto per le strade di Macerata",
                        CONCORSO_FOTO_2024, new ArrayList<>(), TURIST_1),
                //6 //FOTO_TORRE_CIVICA //Municipality: Macerata //Contest: Concorso fotografico annuale 2024
                new ContestContent("Foto della torre civica",
                        CONCORSO_FOTO_2024, new ArrayList<>(), TURIST_1),
                //7 //FOTO_PIZZA_REGINA //Municipality: Camerino //Contest: Migliore foto di pizza Novembre
                new ContestContent("Foto della pizza Regina Sbagliata",
                        CONCORSO_FOTO_PIZZA, new ArrayList<>(), TURIST_3),
                //8 //FOTO_PITTURA_1 //Municipality: Camerino //Contest: Concorso pittura dei paesaggi
                new ContestContent("Pittura della Basilica di San Venanzio",
                        CONCORSO_PITTURA, new ArrayList<>(), TURIST_1),
                //9 //FOTO_PITTURA_2 //Municipality: Camerino //Contest: Concorso pittura dei paesaggi //Not approved
                new ContestContent("Pittura di Piazza Cavour",
                        CONCORSO_PITTURA, new ArrayList<>(), TURIST_2))
        );

        FOTO_SAN_VENANZIO = repository.save(contents.get(0));
        contents.get(1).approve();
        FOTO_PIAZZA_LIBERTA_1 = repository.save(contents.get(1));
        contents.get(2).approve();
        FOTO_PIAZZA_LIBERTA_2 = repository.save(contents.get(2));
        contents.get(3).approve();
        FOTO_PIZZA_MARGHERITA = repository.save(contents.get(3));
        contents.get(4).approve();
        MANIFESTO_CORSA_SPADA = repository.save(contents.get(4));
        contents.get(5).approve();
        FOTO_STRADE_MACERATA = repository.save(contents.get(5));
        contents.get(6).approve();
        FOTO_TORRE_CIVICA = repository.save(contents.get(6));
        contents.get(7).approve();
        FOTO_PIZZA_REGINA = repository.save(contents.get(7));
        contents.get(8).approve();
        FOTO_PITTURA_1 = repository.save(contents.get(8));

        FOTO_PITTURA_2 = repository.save(contents.get(9));

        ((PointOfInterest) BASILICA_SAN_VENANZIO).addContent((PointOfInterestContent) FOTO_SAN_VENANZIO);
        ((PointOfInterest) PIAZZA_LIBERTA).addContent((PointOfInterestContent) FOTO_PIAZZA_LIBERTA_1);
        ((PointOfInterest) PIAZZA_LIBERTA).addContent((PointOfInterestContent) FOTO_PIAZZA_LIBERTA_2);
        ((PointOfInterest) PIZZERIA_ENJOY).addContent((PointOfInterestContent) FOTO_PIZZA_MARGHERITA);
        ((PointOfInterest) CORSA_SPADA).addContent((PointOfInterestContent) MANIFESTO_CORSA_SPADA);
        CONCORSO_FOTO_2024.getProposalRegister().proposeContent((ContestContent) FOTO_STRADE_MACERATA);
        CONCORSO_FOTO_2024.getProposalRegister().proposeContent((ContestContent) FOTO_TORRE_CIVICA);
        CONCORSO_FOTO_PIZZA.getProposalRegister().proposeContent((ContestContent) FOTO_PIZZA_REGINA);
        CONCORSO_PITTURA.getProposalRegister().proposeContent((ContestContent) FOTO_PITTURA_1);
        CONCORSO_PITTURA.getProposalRegister().proposeContent((ContestContent) FOTO_PITTURA_2);

        CONCORSO_FOTO_2024 = contestJpaRepository.saveAndFlush(CONCORSO_FOTO_2024);
        CONCORSO_FOTO_PIZZA = contestJpaRepository.saveAndFlush(CONCORSO_FOTO_PIZZA);
        CONCORSO_PITTURA = contestJpaRepository.saveAndFlush(CONCORSO_PITTURA);

        TURIST_1.addSavedContent(MANIFESTO_CORSA_SPADA);
        TURIST_2.addSavedContent(MANIFESTO_CORSA_SPADA);
        TURIST_1 = userJpaRepository.saveAndFlush(TURIST_1);
        TURIST_2 = userJpaRepository.saveAndFlush(TURIST_2);
        repository.flush();
        areContentsSet = true;
    }

    public void clearRequests(RequestJpaRepository repository){
        repository.deleteAll();
        repository.flush();
        areRequestsSet = false;
    }

    public void setUpRequests(RequestJpaRepository repository) {
        clearRequests(repository);
        if (!areGeoLocatablesSet || !areContentsSet || !areUsersSet)
            throw new IllegalStateException("GeoLocatable, Contents and Users repositories are not set");

        List<Request> requests = new ArrayList<>();
        requests.addAll(Arrays.asList(
                //0 //RICHIESTA_PIAZZA_LIBERTA //User: Pippo01 //GeoLocatable: Piazza della Libertà //Municipality: Macerata
                RequestFactory.getApprovalRequest(PIAZZA_LIBERTA),
                //1 //RICHIESTA_FOTO_BASILICA //User: Pluto02 //Content: Foto di Basilica di San Venanzio //GeoLocatable: Basilica di San Venanzio //Municipality: Camerino
                RequestFactory.getApprovalRequest(FOTO_SAN_VENANZIO),
                //2 //RICHIESTA_PITTURA_CAVOUR //User: Pluto02 //Content: Pittura piazza Cavour //Contest: Concorso pittura paessaggi //Municipality: Camerino
                RequestFactory.getApprovalRequest(FOTO_PITTURA_2)
        ));

        requests.get(0).setSender(TURIST_1);
        requests.get(1).setSender(TURIST_2);
        requests.get(2).setSender(TURIST_2);
        RICHIESTA_PIAZZA_LIBERTA = repository.save(requests.get(0));
        RICHIESTA_FOTO_BASILICA = repository.save(requests.get(1));
        RICHIESTA_PITTURA_CAVOUR = repository.save(requests.get(2));

        repository.flush();
        areRequestsSet = true;
    }

    public void clearNotifications(NotificationJpaRepository repository){
        repository.deleteAll();
        repository.flush();
        areNotificationsSet = false;
    }

    public void setUpNotifications(NotificationJpaRepository repository) {
        clearNotifications(repository);
        if (!areUsersSet || !areMunicipalitiesSet || !areContestsSet || !areGeoLocatablesSet)
            throw new IllegalStateException("Users, Municipalities, Contests and GeoLocatable repositories are not set");

        List<Notification> notifications = new ArrayList<>();
        notifications.addAll(Arrays.asList(
                //0 //Notifica invito concorso pittura per Utenti
                Notification.createNotification(CONCORSO_PITTURA, "Sei stato invitato ad un contest."),
                //1 //Notifica contenuto vincitore per Comune
                Notification.createNotification(FOTO_PIZZA_REGINA,
                        "Decretato il contenuto vincitore del contest "+CONCORSO_FOTO_PIZZA.getName()),
                //2 //Notifica inizio evento per Comune
                Notification.createNotification(SEPTEMBER_FEST, "L'evento "+SEPTEMBER_FEST.getName()+" è iniziato!")
        ));

        repository.save(notifications.get(0));
        repository.save(notifications.get(1));
        repository.save(notifications.get(2));

        CONCORSO_PITTURA.getParticipants().forEach(user -> user.addNotification(notifications.get(0)));
        CAMERINO.addNotification(notifications.get(1));
        MACERATA.addNotification(notifications.get(2));

        repository.flush();
        areNotificationsSet = true;
    }

    public void clearMessages(MessageJpaRepository repository){
        repository.deleteAll();
        repository.flush();
        areMessagesSet = false;
    }

    public void setUpMessages(MessageJpaRepository repository) {
        List<Message> messages = Arrays.asList(
                new Message("Mario Rossi", "mario.rossi@email.com",
                        "Testo del messaggio", new Date(124, 0, 1), new ArrayList<>()),
                new Message("Luigi Bianchi", "luigi.bianchi@email.it",
                        "Testo del messaggio", new Date(124, 0, 21), new ArrayList<>()));

        MESSAGGIO_1 = repository.save(messages.get(0));
        MESSAGGIO_2 = repository.save(messages.get(1));

        repository.flush();
        areMessagesSet = true;
    }
}
