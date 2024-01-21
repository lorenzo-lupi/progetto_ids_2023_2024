package search;

import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchEngineTest {

    private static final Municipality[] municipalities = new Municipality[] {
            new Municipality("Macerata", "Comune di Macerata",
                    new Position(43.29812657107886, 13.451878161920886),
                    new CoordinatesBox(new Position(43.317324, 13.409422), new Position (43.271074, 13.499990))),
            new Municipality("Camerino", "Comune di Camerino",
                    new Position(43.13644468556232, 13.067156069846892),
                    new CoordinatesBox(new Position(43.153712, 13.036414), new Position (43.123261, 13.095768)))
    };

    private static final PointOfInterest[] POINT_OF_INTERESTS = new PointOfInterest[] {
            new PointOfInterest("Università di Camerino", "Università di Camerino",
                    new Position(43.13644468556232, 13.067156069846892),
                    municipalities[1]),
            new PointOfInterest("Via Madonna delle Carceri", "Via Madonna delle Carceri",
                    new Position(43.140, 13.069),
                    municipalities[1]),
            new PointOfInterest("Piazza della Libertà", "Piazza della Libertà",
                    new Position(43.29812657107886, 13.451878161920886),
                    municipalities[0])
    };
    @BeforeAll
    public static void setUpCollections() {
        municipalities[1].addGeoLocalizable(POINT_OF_INTERESTS[0]);
        municipalities[1].addGeoLocalizable(POINT_OF_INTERESTS[1]);
        municipalities[0].addGeoLocalizable(POINT_OF_INTERESTS[2]);
    }

    @Test
    public void shouldSearchMunicipalityByName() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(List.of(municipalities));
        searchEngine.addCriterion(Parameter.NAME, SearchCriterion.EQUALS, "Macerata");
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Macerata", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchMunicipalityByDescription1() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(List.of(municipalities));
        searchEngine.addCriterion(Parameter.DESCRIPTION, SearchCriterion.CONTAINS, "Camerino");
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Camerino", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchMunicipalityByDescription2() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(List.of(municipalities));
        searchEngine.addCriterion(Parameter.DESCRIPTION, SearchCriterion.STARTS_WITH, "Comune di");
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.containsAll(Arrays.stream(municipalities).toList()));
    }

    @Test
    public void shouldSearchMunicipalityByPosition() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(List.of(municipalities));
        searchEngine.addCriterion(Parameter.POSITION, SearchCriterion.EQUALS, new Position(43.13644468556232, 13.067156069846892));
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Camerino", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchMunicipalityByCoordinatesBox() {
        SearchEngine<Municipality> searchEngine = new SearchEngine<>(List.of(municipalities));
        searchEngine.addCriterion(Parameter.POSITION, SearchCriterion.INCLUDED_IN_BOX,
                new CoordinatesBox(new Position(43.302654, 12.960106), new Position (43.099477, 13.546755)));
        List<Municipality> searchResult = searchEngine.search().getResults();
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.containsAll(Arrays.stream(municipalities).toList()));
    }

    @Test
    public void shouldSearchGeoLocalizableByName() {
        SearchEngine<PointOfInterest> searchEngine = new SearchEngine<>(List.of(POINT_OF_INTERESTS));
        searchEngine.addCriterion(Parameter.NAME, SearchCriterion.EQUALS, "Università di Camerino");
        List<PointOfInterest> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Università di Camerino", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchGeoLocalizableByDescription() {
        SearchEngine<PointOfInterest> searchEngine = new SearchEngine<>(List.of(POINT_OF_INTERESTS));
        searchEngine.addCriterion(Parameter.DESCRIPTION, SearchCriterion.CONTAINS, "Piazza");
        List<PointOfInterest> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Piazza della Libertà", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchGeoLocalizableByMunicipality() {
        SearchEngine<PointOfInterest> searchEngine = new SearchEngine<>(List.of(POINT_OF_INTERESTS));
        searchEngine.addCriterion(Parameter.MUNICIPALITY, SearchCriterion.EQUALS, municipalities[1]);
        List<PointOfInterest> searchResult = searchEngine.search().getResults();
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.containsAll(Arrays.asList(POINT_OF_INTERESTS[0], POINT_OF_INTERESTS[1])));
    }

    @Test
    public void shouldSearchGeoLocalizableByApproved1() {
        SearchEngine<PointOfInterest> searchEngine = new SearchEngine<>(List.of(POINT_OF_INTERESTS));
        searchEngine.addCriterion(Parameter.APPROVED, SearchCriterion.EQUALS, true);
        List<PointOfInterest> searchResult = searchEngine.search().getResults();
        assertEquals(0, searchResult.size());
    }

    @Test
    public void shouldSearchGeoLocalizableByApproved2() {
        POINT_OF_INTERESTS[1].setApproved(true);
        POINT_OF_INTERESTS[2].setApproved(true);

        SearchEngine<PointOfInterest> searchEngine = new SearchEngine<>(List.of(POINT_OF_INTERESTS));
        searchEngine.addCriterion(Parameter.APPROVED, SearchCriterion.EQUALS, true);
        List<PointOfInterest> searchResult = searchEngine.search().getResults();
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.containsAll(Arrays.asList(POINT_OF_INTERESTS[1], POINT_OF_INTERESTS[2])));
    }
}
