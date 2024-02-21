package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.UserIF;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.CredentialsUtils;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class UserHandlerTest {

    private static final UserIF user1Pos = new UserIF(
            "username1",
            "email1@gmail.com",
            "Password1!"
    );

    private static final UserIF user2Pos = new UserIF(
            "username2",
            "email2@gmail.com",
            "Password2!"
    );


    private static final UserIF user4Neg = new UserIF(
            "username2",
            "email4@gmail.com",
            "Password4!"
    );

    @BeforeEach
    public void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
        testMultipleAdministrator = new User("multipltore3", "test122@email.it", "asasdfddA1!");
        camerinoAdministrator = new User("vamerino2", "test222@gmail.it", "asasdfddA1!");
        testMultipleAdministrator.addRole(new Role(SampleRepositoryProvider.CAMERINO, AuthorizationEnum.ADMINISTRATOR));
        testMultipleAdministrator.addRole(new Role(SampleRepositoryProvider.MACERATA, AuthorizationEnum.ADMINISTRATOR));
        camerinoAdministrator.addRole(new Role(SampleRepositoryProvider.CAMERINO, AuthorizationEnum.ADMINISTRATOR));
        UserRepository.getInstance().add(testMultipleAdministrator);
        UserRepository.getInstance().add(camerinoAdministrator);
    }

    //region Registration
    @Test
    void testCreateUser1() {
        assertDoesNotThrow(() -> UserHandler.registerUser(user1Pos));
    }

    @Test
    void testCreateUser2() {
        UserHandler.registerUser(user2Pos);
        assertThrows(IllegalArgumentException.class, () -> UserHandler.registerUser(user4Neg));
    }

    //endregion

    //region ModifyAuthorization
    User testMultipleAdministrator;
    User camerinoAdministrator;

    @Test
    void testShouldNotModifyAuthorization1() {
        assertTrue(SampleRepositoryProvider.TURIST_1.getRoles().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> UserHandler.modifyUserAuthorization(testMultipleAdministrator.getID(),
                SampleRepositoryProvider.TURIST_1.getID(),
                List.of(AuthorizationEnum.CURATOR)));
        UserHandler.modifyUserAuthorization(camerinoAdministrator.getID(),
                SampleRepositoryProvider.TURIST_1.getID(),
                List.of(AuthorizationEnum.CURATOR));

        assertTrue(SampleRepositoryProvider.TURIST_1.getAuthorizations(SampleRepositoryProvider.CAMERINO).contains(AuthorizationEnum.CURATOR));
    }

    @Test
    void testModifyAuthorization2() {
        assertTrue(SampleRepositoryProvider.TURIST_2.getRoles().isEmpty());
        UserHandler.modifyUserAuthorization(SampleRepositoryProvider.MACERATA.getID(),
                testMultipleAdministrator.getID(),
                SampleRepositoryProvider.TURIST_2.getID(),
                List.of(AuthorizationEnum.CURATOR));
        assertTrue(SampleRepositoryProvider.TURIST_2.getAuthorizations(SampleRepositoryProvider.MACERATA).contains(AuthorizationEnum.CURATOR));
    }

    @Test
    void testModifyAuthorization3() {
        assertTrue(UserHandler.isAllowedToModifyAuthorizations(testMultipleAdministrator.getID()));
    }
    //endregion

    @Test
    public void shouldGenerateMunicipalityAdmin() {
        long id = UserHandler.generateMunicipalityAdministrator(
                SampleRepositoryProvider.CAMERINO.getID(), "pippo01@yopmail.com");

        assertEquals(11, UserRepository.getInstance().getItemStream().toList().size());
        assertEquals("CamerinoAdmin", UserRepository.getInstance().getItemByID(id).getUsername());
        assertEquals(AuthorizationEnum.ADMINISTRATOR,
                UserRepository.getInstance().getItemByID(id).getRoles().get(0).authorizationEnum());
    }

    @Test
    public void shouldNotGenerateMunicipalityAdmin() {
        assertThrows(IllegalArgumentException.class, () ->
                UserHandler.generateMunicipalityAdministrator(
                        SampleRepositoryProvider.CAMERINO.getID(), "invalidEmail"));

        assertEquals(10, UserRepository.getInstance().getItemStream().toList().size());
    }

    @AfterEach
    public void tearDown() {
        SampleRepositoryProvider.clearAllRepositories();
    }
}