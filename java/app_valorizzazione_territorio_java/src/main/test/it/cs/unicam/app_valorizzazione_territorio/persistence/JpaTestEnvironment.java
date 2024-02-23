package it.cs.unicam.app_valorizzazione_territorio.persistence;

import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.*;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.GeoLocatableJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.RoleJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@ExtendWith(SpringExtension.class)
public class JpaTestEnvironment {

    public static boolean areMunicipalitiesSet = false;
    public static boolean areUsersSet = false;
    public static Municipality MACERATA, CAMERINO;
    public static Role MAC_CUR, MAC_CONTR, MAC_ADMIN, MAC_ANIM, CAM_CUR,CAM_CONTR, CAM_ADMIN, CAM_ANIM;
    public static Map<Municipality, Map<AuthorizationEnum, Role>> roles = new HashMap<>();
    public static User TURIST_1, TURIST_2, TURIST_3, CURATOR_CAMERINO, ENTERTAINER_CAMERINO, ENTERTAINER_MACERATA, ENTERTAINER_TEST, ADMINISTRATOR_CAMERINO;
    public static GeoLocatable UNIVERSITY_CAMERINO, VIA_MADONNA_CARCERI, PIAZZA_LIBERTA, CORSA_SPADA,
            SEPTEMBER_FEST, PIZZERIA_ENJOY, BASILICA_SAN_VENANZIO, TORUR_STUDENTE, TRADIZIONE_SAN_VENANZIO, GAS_FACILITY;

    public static Content FOTO_SAN_VENANZIO, FOTO_PIAZZA_LIBERTA_1, FOTO_PIAZZA_LIBERTA_2, FOTO_PIZZA_MARGHERITA,
            MANIFESTO_CORSA_SPADA, FOTO_STRADE_MACERATA, FOTO_TORRE_CIVICA, FOTO_PIZZA_REGINA, FOTO_PITTURA_1,
            FOTO_PITTURA_2;


    public static void clearMunicipalities(MunicipalityJpaRepository repository){
        repository.deleteAll();
        areMunicipalitiesSet = false;
    }
    public static void setUpMunicipalities(MunicipalityJpaRepository repository,
                                           RoleJpaRepository roleRepository){
        clearMunicipalities(repository);

        MACERATA = repository.saveAndFlush(new Municipality("Macerata", "Comune di Macerata",
                new Position(43.29812657107886, 13.451878161920886),
                new CoordinatesBox(new Position(43.317324, 13.409422),
                        new Position(43.271074, 13.499990)),
                new ArrayList<>()));
                //1 //CAMERINO
        CAMERINO = repository.saveAndFlush(new Municipality("Camerino", "Comune di Camerino",
                        new Position(43.13644468556232, 13.067156069846892),
                        new CoordinatesBox(new Position(43.153712, 13.036414),
                                new Position(43.123261, 13.095768)),
                        new ArrayList<>()));
        roles = new HashMap<>();
        roles.put(MACERATA, new EnumMap<>(AuthorizationEnum.class));
        roles.put(CAMERINO, new EnumMap<>(AuthorizationEnum.class));
        Arrays.stream(AuthorizationEnum.values()).forEach(auth -> {
            roles.get(MACERATA).put(auth, roleRepository.save(new Role(MACERATA, auth)));
            roles.get(CAMERINO).put(auth, roleRepository.save(new Role(CAMERINO, auth)));
        });
        areMunicipalitiesSet = true;
    }

    public static void clearUsers(UserJpaRepository repository){
        repository.deleteAll();
        areUsersSet = false;
    }

    public static void setUpUsers(UserJpaRepository repository) {
        clearUsers(repository);
        if (!areMunicipalitiesSet)
            throw new IllegalStateException("Municipalities not set");
        areUsersSet = true;
        List<User> users = new ArrayList<>(Arrays.asList(
                /* 0 */ new User("Pippo00", "pippo00@gmail.com", "Testpassword01"),               //TURIST_1
                /* 1 */ new User("Pluto01", "pluto01@gmail.com", "Testpassword02"),               //TURIST_2
                /* 2 */ new User("Paperino02", "paperino02@bitmail.it", "Testpassword02"),        //TURIST_3
                /* 3 */ new User("Pinco03", "pinco03@bitmail.it", "Testpassword03"),              //CURATOR_CAMERINO
                /* 4 */ new User("Pallo04", "pallo04@blobmail.com", "Testpassword04"),            //ENTERTAINER_MACERATA
                /* 5 */ new User("MarioRossi05", "mario.rossi06@blobmail.com", "Testpassword05"), //ENTERTAINER_CAMERINO
                /* 6 */ new User("Zeb89", "mario.rossi06@blobmail.com", "Testpassword06"), //ENTERTAINER_TEST
                /* 7*/  new User("Admin", "ace.gamer@nonpagotasseinitalia.mt", "Testpassword07") //ADMINISTRATOR_CAMERINO
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
    }

    public static void clearGeoLocatables(GeoLocatableJpaRepository repository){
        repository.deleteAll();
    }
    public static void setUpGeoLocatableRepository(GeoLocatableJpaRepository repository){
        clearGeoLocatables(repository);
        if(!areUsersSet || !areMunicipalitiesSet)
            throw new IllegalStateException("user and municipality repositories are not set");

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
                        CAMERINO, ActivityTypeEnum.CHURCH, TURIST_2)
        ));
        UNIVERSITY_CAMERINO = repository.save(geoLocatables.get(0));
        VIA_MADONNA_CARCERI = repository.save(geoLocatables.get(1));
        PIAZZA_LIBERTA = repository.save(geoLocatables.get(2));
        CORSA_SPADA = repository.save(geoLocatables.get(3));
        SEPTEMBER_FEST = repository.save(geoLocatables.get(4));
        PIZZERIA_ENJOY = repository.save(geoLocatables.get(5));
        BASILICA_SAN_VENANZIO = repository.save(geoLocatables.get(6));

        CAMERINO.addGeoLocatable(UNIVERSITY_CAMERINO);
        CAMERINO.addGeoLocatable(VIA_MADONNA_CARCERI);
        MACERATA.addGeoLocatable(PIAZZA_LIBERTA);
        CAMERINO.addGeoLocatable(CORSA_SPADA);
        MACERATA.addGeoLocatable(SEPTEMBER_FEST);
        CAMERINO.addGeoLocatable(PIZZERIA_ENJOY);
        CAMERINO.addGeoLocatable(BASILICA_SAN_VENANZIO);
        CAMERINO.addGeoLocatable(TORUR_STUDENTE);
        CAMERINO.addGeoLocatable(TRADIZIONE_SAN_VENANZIO);
        CAMERINO.addGeoLocatable(GAS_FACILITY);

        TORUR_STUDENTE = repository.save(new CompoundPoint("Tour dello studente", "Tour dello studente",
                CAMERINO, CompoundPointTypeEnum.ITINERARY, Arrays.asList(
                (PointOfInterest) geoLocatables.get(1),
                (PointOfInterest) geoLocatables.get(0),
                (PointOfInterest) geoLocatables.get(5)),
                new ArrayList<>(), TURIST_2));
    }

    private void setUpRoles(RoleJpaRepository roles){

    }
}
