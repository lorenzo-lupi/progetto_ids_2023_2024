package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MunicipalityIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityOF;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MunicipalityHandlerTest {
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
        long id = MunicipalityHandler.createMunicipality(municipalitySample1);

        assertEquals(4, MunicipalityRepository.getInstance().getItemStream().toList().size());
        assertEquals("Matelica", MunicipalityRepository.getInstance().getItemByID(id).getName());
    }

    @Test
    public void shouldNotCreateMunicipality() {
        assertThrows(NullPointerException.class, () ->
                MunicipalityHandler.createMunicipality(municipalitySample2));

        assertEquals(3, MunicipalityRepository.getInstance().getItemStream().toList().size());
    }

    @Test
    void shouldViewFilteredMunicipalities1() {
        List<MunicipalityOF> municipalities = MunicipalityHandler.viewFilteredMunicipalities(List.of(
                new SearchFilter("NAME", "EQUALS", "Macerata")));
        assertEquals(1, municipalities.size());
        assertEquals(Set.of(SampleRepositoryProvider.MACERATA.getOutputFormat()),
                new HashSet<>(municipalities));
    }

    @Test
    void shouldViewFilteredMunicipalities2() {
        List<MunicipalityOF> municipalities = MunicipalityHandler.viewFilteredMunicipalities(List.of(
                new SearchFilter(Parameter.NAME.toString(), "EQUALS", "Macerata"),
                new SearchFilter(Parameter.POSITION.toString(), "INCLUDED_IN_BOX",
                        SampleRepositoryProvider.MACERATA.getCoordinatesBox())));
        assertEquals(1, municipalities.size());
        assertEquals(Set.of(SampleRepositoryProvider.MACERATA.getOutputFormat()),
                new HashSet<>(municipalities));
    }

    @Test
    void shouldViewMunicipality() {
        assertEquals(SampleRepositoryProvider.CAMERINO.getOutputFormat(),
                MunicipalityHandler.viewMunicipality(SampleRepositoryProvider.CAMERINO.getID()));
    }

    @Test
    void shouldGetSearchResult() {
        List<MunicipalityOF> municipalities = MunicipalityHandler.viewFilteredMunicipalities(List.of());
        assertEquals(3, municipalities.size());
        assertEquals(Set.of(
                        SampleRepositoryProvider.MACERATA.getOutputFormat(),
                        SampleRepositoryProvider.CAMERINO.getOutputFormat(),
                        SampleRepositoryProvider.COMUNE_DEI_TEST.getOutputFormat()),
                new HashSet<>(municipalities));
    }
}