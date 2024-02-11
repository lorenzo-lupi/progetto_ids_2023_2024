package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalitySOF;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import it.cs.unicam.app_valorizzazione_territorio.utils.SampleRepositoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MunicipalityVisualizationHandlerTest {

    @BeforeAll
    static void setUpRepository() {
        SampleRepositoryProvider.setUpMunicipalitiesRepository();
    }

    @Test
    void shouldViewFilteredMunicipalities1() {
        List<MunicipalitySOF> municipalities = MunicipalityVisualizationHandler.viewFilteredMunicipalities(List.of(
                new SearchFilter("NAME", "EQUALS", "Macerata")));
        assertEquals(1,municipalities.size());
        assertEquals(Set.of(SampleRepositoryProvider.MACERATA.getSynthesizedFormat()),
                new HashSet<>(municipalities));
    }

    @Test
    void shouldViewFilteredMunicipalities2() {
        List<MunicipalitySOF> municipalities = MunicipalityVisualizationHandler.viewFilteredMunicipalities(List.of(
                new SearchFilter(Parameter.NAME.toString(), "EQUALS", "Macerata"),
                new SearchFilter(Parameter.POSITION.toString(), "INCLUDED_IN_BOX",
                        SampleRepositoryProvider.MACERATA.getCoordinatesBox())));
        assertEquals(1,municipalities.size());
        assertEquals(Set.of(SampleRepositoryProvider.MACERATA.getSynthesizedFormat()),
                new HashSet<>(municipalities));
    }

    @Test
    void shouldViewMunicipality() {
        assertEquals(SampleRepositoryProvider.CAMERINO.getDetailedFormat(),
                MunicipalityVisualizationHandler.viewMunicipality(SampleRepositoryProvider.CAMERINO.getID()));
    }

    @Test
    void shouldGetSearchResult() {
        List<MunicipalitySOF> municipalities = MunicipalityVisualizationHandler.viewFilteredMunicipalities(List.of());
        assertEquals(3,municipalities.size());
        assertEquals(Set.of(
                SampleRepositoryProvider.MACERATA.getSynthesizedFormat(),
                SampleRepositoryProvider.CAMERINO.getSynthesizedFormat(),
                SampleRepositoryProvider.COMUNE_DEI_TEST.getSynthesizedFormat()),
                new HashSet<>(municipalities));
    }
}