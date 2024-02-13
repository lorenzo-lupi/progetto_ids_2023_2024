package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MunicipalityAdminGenerationHandlerTest {

    @BeforeEach
    public void setUp() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    public void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    public void shouldGenerateMunicipalityAdmin() {
        long id = MunicipalityAdminGenerationHandler.generateMunicipalityAdmin(
                SampleRepositoryProvider.CAMERINO.getID(), "pippo01@yopmail.com");

        assertEquals(9, UserRepository.getInstance().getItemStream().toList().size());
        assertEquals("CamerinoAdmin", UserRepository.getInstance().getItemByID(id).getUsername());
        assertEquals(AuthorizationEnum.ADMINISTRATOR,
                UserRepository.getInstance().getItemByID(id).getRoles().get(0).authorizationEnum());
    }

    @Test
    public void shouldNotGenerateMunicipalityAdmin() {
        assertThrows(IllegalArgumentException.class, () ->
                MunicipalityAdminGenerationHandler.generateMunicipalityAdmin(
                        SampleRepositoryProvider.CAMERINO.getID(), "invalidEmail"));

        assertEquals(8, UserRepository.getInstance().getItemStream().toList().size());
    }
}
