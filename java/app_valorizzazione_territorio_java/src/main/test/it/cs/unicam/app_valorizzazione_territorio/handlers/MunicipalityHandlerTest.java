package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MunicipalityIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityOF;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class,
        MunicipalityHandler.class})
@DataJpaTest
class MunicipalityHandlerTest {

    @Autowired
    SampleRepositoryProvider sampleRepositoryProvider;
    @Autowired
    MunicipalityHandler municipalityHandler;


    MunicipalityIF municipalitySample1 = new MunicipalityIF("Matelica",
            "Decrizione di Matelica",
            new Position(43.251227, 13.009103),
            new CoordinatesBox(new Position(43.277326, 12.973482),
                    new Position(43.239917, 13.047051)),
            new ArrayList<>());

    MunicipalityIF municipalitySample2 = new MunicipalityIF("Tolentino",
            "Descrizione di Tolentino", null, null, null);

    @BeforeEach
    public void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    public void clearRepositories() {
        sampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    public void shouldCreateMunicipality() {
        long id = municipalityHandler.createMunicipality(municipalitySample1);

        assertEquals(4, sampleRepositoryProvider.getMunicipalityJpaRepository().findAll().size());
        assertEquals("Matelica", sampleRepositoryProvider.getMunicipalityJpaRepository().getByID(id).get().getName());
    }

  @Test
    public void shouldNotCreateMunicipality() {
        assertThrows(NullPointerException.class, () ->
                municipalityHandler.createMunicipality(municipalitySample2));

        assertEquals(3, sampleRepositoryProvider.getMunicipalityJpaRepository().findAll().size());
    }
  
    @Test
    void shouldViewFilteredMunicipalities1() {
        List<MunicipalityOF> municipalities = municipalityHandler.viewFilteredMunicipalities(List.of(
                new SearchFilter("NAME", "EQUALS", "Macerata")));
        assertEquals(1, municipalities.size());
        assertEquals(Set.of(sampleRepositoryProvider.MACERATA.getOutputFormat()),
                new HashSet<>(municipalities));
    }

    @Test
    void shouldViewFilteredMunicipalities2() {
        List<MunicipalityOF> municipalities = municipalityHandler.viewFilteredMunicipalities(List.of(
                new SearchFilter(Parameter.NAME.toString(), "EQUALS", "Macerata"),
                new SearchFilter(Parameter.POSITION.toString(), "INCLUDED_IN_BOX",
                        sampleRepositoryProvider.MACERATA.getCoordinatesBox())));
        assertEquals(1, municipalities.size());
        assertEquals(Set.of(sampleRepositoryProvider.MACERATA.getOutputFormat()),
                new HashSet<>(municipalities));
    }

    @Test
    void shouldViewMunicipality() {
        assertEquals(sampleRepositoryProvider.CAMERINO.getOutputFormat(),
                municipalityHandler.viewMunicipality(sampleRepositoryProvider.CAMERINO.getID()));
    }

    @Test
    void shouldGetSearchResult() {
        List<MunicipalityOF> municipalities = municipalityHandler.viewFilteredMunicipalities(List.of());
        assertEquals(3, municipalities.size());
        assertEquals(Set.of(
                        sampleRepositoryProvider.MACERATA.getOutputFormat(),
                        sampleRepositoryProvider.CAMERINO.getOutputFormat(),
                        sampleRepositoryProvider.COMUNE_DEI_TEST.getOutputFormat()),
                new HashSet<>(municipalities));
    }
}