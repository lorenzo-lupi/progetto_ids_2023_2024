package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityIF;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MunicipalityCreationHandlerTest {

    MunicipalityIF municipalitySample1 = new MunicipalityIF("Matelica",
            "Decrizione di Matelica",
            new Position(43.251227, 13.009103),
            new CoordinatesBox(new Position(43.277326, 12.973482),
                    new Position(43.239917, 13.047051) ),
            new ArrayList<>());

    MunicipalityIF municipalitySample2 = new MunicipalityIF("Tolentino",
            "Descrizione di Tolentino", null, null, null);

    @BeforeEach
    public void setUp() {
        SampleRepositoryProvider.setUpMunicipalitiesRepository();
    }

    @AfterEach
    public void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    public void shouldCreateMunicipality() {
        long id = MunicipalityCreationHandler.createMunicipality(municipalitySample1);

        assertEquals(4, MunicipalityRepository.getInstance().getItemStream().toList().size());
        assertEquals("Matelica", MunicipalityRepository.getInstance().getItemByID(id).getName());
    }

    @Test
    public void shouldNotCreateMunicipality() {
        assertThrows(NullPointerException.class, () ->
                MunicipalityCreationHandler.createMunicipality(municipalitySample2));

        assertEquals(3, MunicipalityRepository.getInstance().getItemStream().toList().size());
    }
}
