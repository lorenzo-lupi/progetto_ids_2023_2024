package it.cs.unicam.app_valorizzazione_territorio.handlers;


import it.cs.unicam.app_valorizzazione_territorio.model.RoleTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModifyAuthorizationHandlerTest {

    @BeforeAll
    void setUp() {
        SampleRepositoryProvider.clearAndSetUpRepositories();
    }

    @Test
    void modifyAuthorization_NotAdministrator_ThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            ModifyAuthorizationHandler.modifyAuthorization(SampleRepositoryProvider.TURIST_1.getID(),
                    SampleRepositoryProvider.TURIST_2.getID(),
                    SampleRepositoryProvider.CAMERINO.getID(),
                    RoleTypeEnum.CONTRIBUTOR);
        });
    }

    @Test
    void modifyAuthorization_IsAdministrator_AddsRole() {

        ModifyAuthorizationHandler.modifyAuthorization(SampleRepositoryProvider.ADMINISTRATOR_CAMERINO.getID(),
                SampleRepositoryProvider.TURIST_3.getID(),
                SampleRepositoryProvider.CAMERINO.getID(),
                RoleTypeEnum.CONTRIBUTOR);

        assertTrue(SampleRepositoryProvider.TURIST_3.getAuthorizations(SampleRepositoryProvider.CAMERINO).contains(RoleTypeEnum.CONTRIBUTOR));
    }
}