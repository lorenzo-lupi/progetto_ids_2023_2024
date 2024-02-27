package it.cs.unicam.app_valorizzazione_territorio.search;

import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.*;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class})
@DataJpaTest
public class SearchEngineTest {

    @Autowired
    private SampleRepositoryProvider sampleRepositoryProvider;
    private List<Municipality> municipalities;
    private List<GeoLocatable> geoLocatables;
    @BeforeEach
    public void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();
        municipalities = sampleRepositoryProvider.getMunicipalityJpaRepository().findAll();
        geoLocatables = sampleRepositoryProvider.getGeoLocatableJpaRepository().findAll();
    }

    @AfterEach
    public void clear() {
        sampleRepositoryProvider.clearAllRepositories();
    }

    @Test
    public void shouldSearchMunicipalityByName() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(municipalities);
        searchEngine.addCriterion(Parameter.NAME, SearchCriterion.EQUALS, "Macerata");
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Macerata", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchMunicipalityByDescription1() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(municipalities);
        searchEngine.addCriterion(Parameter.DESCRIPTION, SearchCriterion.CONTAINS, "Camerino");
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Camerino", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchMunicipalityByDescription2() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(municipalities);
        searchEngine.addCriterion(Parameter.DESCRIPTION, SearchCriterion.STARTS_WITH, "Comune di");
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(3, searchResult.size());
        assertTrue(searchResult.containsAll(municipalities));
    }

    @Test
    public void shouldSearchMunicipalityByPosition() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(municipalities);
        searchEngine.addCriterion(Parameter.POSITION, SearchCriterion.EQUALS, new Position(43.13644468556232, 13.067156069846892));
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Camerino", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchMunicipalityByCoordinatesBox() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(municipalities);
        searchEngine.addCriterion(Parameter.POSITION, SearchCriterion.INCLUDED_IN_BOX,
                new CoordinatesBox(new Position(43.302654, 12.960106), new Position (43.099477, 13.546755)));
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(3, searchResult.size());
        assertTrue(searchResult.containsAll(municipalities));
    }

    @Test
    public void shouldSearchGeoLocalizableByName() {
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(geoLocatables);
        searchEngine.addCriterion(Parameter.NAME, SearchCriterion.EQUALS, "Università di Camerino");
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Università di Camerino", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchGeoLocalizableByDescription() {
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(geoLocatables);
        searchEngine.addCriterion(Parameter.DESCRIPTION, SearchCriterion.CONTAINS, "Piazza");
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Piazza della Libertà", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchGeoLocalizableByMunicipality() {
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(geoLocatables);
        searchEngine.addCriterion(Parameter.MUNICIPALITY, SearchCriterion.EQUALS, municipalities.get(1));
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(8, searchResult.size());
        assertTrue(searchResult.containsAll(List.of(
                sampleRepositoryProvider.UNIVERSITY_CAMERINO,
                sampleRepositoryProvider.VIA_MADONNA_CARCERI,
                sampleRepositoryProvider.CORSA_SPADA,
                sampleRepositoryProvider.PIZZERIA_ENJOY,
                sampleRepositoryProvider.BASILICA_SAN_VENANZIO)));
    }

    @Test
    public void shouldSearchGeoLocalizableByApproved1() {
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(geoLocatables);
        searchEngine.addCriterion(Parameter.APPROVAL_STATUS, SearchCriterion.EQUALS, ApprovalStatusEnum.APPROVED);
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(9, searchResult.size());
    }

    @Test
    public void shouldSearchGeoLocalizableByApproved2() {
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(geoLocatables);
        searchEngine.addCriterion(Parameter.APPROVAL_STATUS, SearchCriterion.EQUALS, ApprovalStatusEnum.PENDING);;
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        System.out.println(searchResult.get(0).getName());
        assertTrue(searchResult.contains(sampleRepositoryProvider.PIAZZA_LIBERTA));
    }
}
