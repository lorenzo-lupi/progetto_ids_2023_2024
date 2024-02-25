package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.UserIF;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class,
        UserHandler.class})
@DataJpaTest
class UserHandlerTest {
    
    @Autowired
    SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    UserHandler userHandler;

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
        sampleRepositoryProvider.setUpAllRepositories();
        testMultipleAdministrator = new User("multipltore3", "test122@email.it", "asasdfddA1!");
        camerinoAdministrator = new User("vamerino2", "test222@gmail.it", "asasdfddA1!");
        testMultipleAdministrator.addRole(new Role(sampleRepositoryProvider.CAMERINO, AuthorizationEnum.ADMINISTRATOR));
        testMultipleAdministrator.addRole(new Role(sampleRepositoryProvider.MACERATA, AuthorizationEnum.ADMINISTRATOR));
        camerinoAdministrator.addRole(new Role(sampleRepositoryProvider.CAMERINO, AuthorizationEnum.ADMINISTRATOR));
        sampleRepositoryProvider.getUserJpaRepository().saveAndFlush(testMultipleAdministrator);
        sampleRepositoryProvider.getUserJpaRepository().saveAndFlush(camerinoAdministrator);
    }

    //region Registration
    @Test
    void testCreateUser1() {
        assertDoesNotThrow(() -> userHandler.registerUser(user1Pos));
    }

    @Test
    void testCreateUser2() {
        userHandler.registerUser(user2Pos);
        assertThrows(IllegalArgumentException.class, () -> userHandler.registerUser(user4Neg));
    }

    //endregion

    //region ModifyAuthorization
    User testMultipleAdministrator;
    User camerinoAdministrator;

    @Test
    void testShouldNotModifyAuthorization1() {
        assertTrue(sampleRepositoryProvider.TURIST_1.getRoles().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> userHandler.modifyUserAuthorization(testMultipleAdministrator.getID(),
                sampleRepositoryProvider.TURIST_1.getID(),
                List.of(AuthorizationEnum.CURATOR)));
        userHandler.modifyUserAuthorization(camerinoAdministrator.getID(),
                sampleRepositoryProvider.TURIST_1.getID(),
                List.of(AuthorizationEnum.CURATOR));

        assertTrue(sampleRepositoryProvider.TURIST_1.getAuthorizations(sampleRepositoryProvider.CAMERINO).contains(AuthorizationEnum.CURATOR));
    }

    @Test
    void testModifyAuthorization2() {
        assertTrue(sampleRepositoryProvider.TURIST_2.getRoles().isEmpty());
        userHandler.modifyUserAuthorization(sampleRepositoryProvider.MACERATA.getID(),
                testMultipleAdministrator.getID(),
                sampleRepositoryProvider.TURIST_2.getID(),
                List.of(AuthorizationEnum.CURATOR));
        assertTrue(sampleRepositoryProvider.TURIST_2.getAuthorizations(sampleRepositoryProvider.MACERATA).contains(AuthorizationEnum.CURATOR));
    }

    @Test
    void testModifyAuthorization3() {
        assertTrue(userHandler.isAllowedToModifyAuthorizations(testMultipleAdministrator.getID()));
    }
    //endregion

    @Test
    public void shouldGenerateMunicipalityAdmin() {
        long id = userHandler.generateMunicipalityAdministrator(
                sampleRepositoryProvider.CAMERINO.getID(), "pippo01@yopmail.com");

        assertEquals(11, sampleRepositoryProvider.getUserJpaRepository().findAll().size());
        assertEquals("CamerinoAdmin", sampleRepositoryProvider.getUserJpaRepository().getByID(id).get().getUsername());
        assertEquals(AuthorizationEnum.ADMINISTRATOR,
                sampleRepositoryProvider.getUserJpaRepository().getByID(id).get().getRoles().get(0).authorizationEnum());
    }

    @Test
    public void shouldNotGenerateMunicipalityAdmin() {
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.generateMunicipalityAdministrator(
                        sampleRepositoryProvider.CAMERINO.getID(), "invalidEmail"));

        assertEquals(10, sampleRepositoryProvider.getUserJpaRepository().findAll().size());
    }

    @AfterEach
    public void tearDown() {
        sampleRepositoryProvider.clearAllRepositories();
    }
}