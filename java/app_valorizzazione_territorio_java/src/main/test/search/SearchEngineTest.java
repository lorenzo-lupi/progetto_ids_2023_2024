package search;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.*;
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

    private static final GeoLocatable[] GEO_LOCATABLES = new PointOfInterest[] {
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
        municipalities[1].addGeoLocalizable(GEO_LOCATABLES[0]);
        municipalities[1].addGeoLocalizable(GEO_LOCATABLES[1]);
        municipalities[0].addGeoLocalizable(GEO_LOCATABLES[2]);
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
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(List.of(GEO_LOCATABLES));
        searchEngine.addCriterion(Parameter.NAME, SearchCriterion.EQUALS, "Università di Camerino");
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Università di Camerino", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchGeoLocalizableByDescription() {
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(List.of(GEO_LOCATABLES));
        searchEngine.addCriterion(Parameter.DESCRIPTION, SearchCriterion.CONTAINS, "Piazza");
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(1, searchResult.size());
        assertEquals("Piazza della Libertà", searchResult.get(0).getName());
    }

    @Test
    public void shouldSearchGeoLocalizableByMunicipality() {
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(List.of(GEO_LOCATABLES));
        searchEngine.addCriterion(Parameter.MUNICIPALITY, SearchCriterion.EQUALS, municipalities[1]);
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.containsAll(Arrays.asList(GEO_LOCATABLES[0], GEO_LOCATABLES[1])));
    }

    @Test
    public void shouldSearchGeoLocalizableByApproved1() {
        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(List.of(GEO_LOCATABLES));
        searchEngine.addCriterion(Parameter.APPROVAL_STATUS, SearchCriterion.EQUALS, ApprovalStatusEnum.APPROVED);
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(0, searchResult.size());
    }

    @Test
    public void shouldSearchGeoLocalizableByApproved2() {
        GEO_LOCATABLES[1].approve();
        GEO_LOCATABLES[2].approve();

        SearchEngine<GeoLocatable> searchEngine = new SearchEngine<>(List.of(GEO_LOCATABLES));
        searchEngine.addCriterion(Parameter.APPROVAL_STATUS, SearchCriterion.EQUALS, ApprovalStatusEnum.APPROVED);;
        List<GeoLocatable> searchResult = searchEngine.search().getResults();
        assertEquals(2, searchResult.size());
        assertTrue(searchResult.containsAll(Arrays.asList(GEO_LOCATABLES[1], GEO_LOCATABLES[2])));
    }
}
