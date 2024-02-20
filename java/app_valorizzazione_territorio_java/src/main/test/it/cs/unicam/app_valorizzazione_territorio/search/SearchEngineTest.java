package it.cs.unicam.app_valorizzazione_territorio.search;

import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchEngineTest {

    private static List<Municipality> municipalities;
    private static List<GeoLocatable> geoLocatables;
    @BeforeAll
    public static void setUp() {
        SampleRepositoryProvider.setUpMunicipalitiesRepository();
        municipalities = MunicipalityRepository.getInstance().getItemStream().toList();
        geoLocatables = MunicipalityRepository.getInstance().getAllGeoLocatables().toList();
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
        assertTrue(searchResult.containsAll(List.of(geoLocatables.get(0), geoLocatables.get(1),
                geoLocatables.get(3), geoLocatables.get(5), geoLocatables.get(6))));
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
        assertTrue(searchResult.contains(geoLocatables.get(4)));
    }
}
