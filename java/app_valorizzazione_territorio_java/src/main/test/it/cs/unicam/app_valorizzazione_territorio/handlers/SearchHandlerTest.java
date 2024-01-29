package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.SampleRepositoryProvider;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.ActivityTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SearchHandlerTest {

    private SearchHandler<Municipality> municipalitySearchHandler;
    private SearchHandler<GeoLocatable> geoLocatableSearchHandler;
    private SearchHandler<PointOfInterestContent> contentSearchHandler;
    private SearchHandler<User> userSearchHandler;

    @BeforeAll
    static void setUp() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @BeforeEach
    private void setUpSearchHandlers() {
        municipalitySearchHandler = new SearchHandler<>(MunicipalityRepository.getInstance().getItemStream().toList());
        geoLocatableSearchHandler = new SearchHandler<>(MunicipalityRepository.getInstance().getAllGeoLocatables().toList());
        contentSearchHandler = new SearchHandler<>(MunicipalityRepository.getInstance().getAllContents().toList());
        userSearchHandler = new SearchHandler<>(UserRepository.getInstance().getItemStream().toList());
    }

    @Test
    void shouldSearchWithoutCriterion() {
        List<? extends Identifiable> searchResult = userSearchHandler.getSearchResult();

        assertEquals(6, searchResult.size());
    }

    @Test
    void shouldSetSearchCriterion1() {
        municipalitySearchHandler.setSearchCriterion(
                Parameter.NAME.toString(), "EQUALS", "Macerata");
        List<? extends Identifiable> searchResult = municipalitySearchHandler.getSearchResult();

        assertEquals(1, searchResult.size());
        assertEquals(SampleRepositoryProvider.municipalities.get(0).getID(), searchResult.get(0).getID());
    }

    @Test
    void shouldSetSearchCriterion2() {
        contentSearchHandler.setSearchCriterion(
                Parameter.DESCRIPTION.toString(), "CONTAINS", "piazza");
        List<? extends Identifiable> searchResult = contentSearchHandler.getSearchResult();

        assertEquals(2, searchResult.size());
        assertEquals(Set.of(SampleRepositoryProvider.contents.get(1).getID(),
                SampleRepositoryProvider.contents.get(2).getID()),
                searchResult.stream().map(Identifiable::getID).collect(Collectors.toSet()));
    }

    @Test
    void shouldSetSearchCriterion3() {
        userSearchHandler.setSearchCriterion(
                Parameter.USERNAME.toString(), "STARTS_WITH", "Mario");
        List<? extends Identifiable> searchResult = userSearchHandler.getSearchResult();

        assertEquals(1, searchResult.size());
        assertEquals(SampleRepositoryProvider.users.get(5).getID(), searchResult.get(0).getID());
    }

    @Test
    void shouldSearchWithFilter() {
        geoLocatableSearchHandler.setSearchCriterion( new SearchFilter(Parameter.ACTIVITY_TYPE.toString(),
                "EQUALS", ActivityTypeEnum.RESTAURANT));
        List<? extends Identifiable> searchResult = geoLocatableSearchHandler.getSearchResult();

        assertEquals(1, searchResult.size());
        assertEquals(SampleRepositoryProvider.geoLocatables.get(5).getID(), searchResult.get(0).getID());
    }

    @Test
    void shouldSetMultipleSearchCriterion() {
        geoLocatableSearchHandler.setSearchCriterion(
                Parameter.CLASSIFICATION.toString(), "EQUALS", "Attraction");
        geoLocatableSearchHandler.setSearchCriterion(
                Parameter.MUNICIPALITY.toString(), "EQUALS", SampleRepositoryProvider.municipalities.get(1));
        List<? extends Identifiable> searchResult = geoLocatableSearchHandler.getSearchResult();

        assertEquals(2, searchResult.size());
        assertEquals(Set.of(SampleRepositoryProvider.geoLocatables.get(0).getID(),
                SampleRepositoryProvider.geoLocatables.get(1).getID()),
                searchResult.stream().map(Identifiable::getID).collect(Collectors.toSet()));
    }

    @Test
    void shouldGiveEmptyResultUponInvalidSearch() {
        geoLocatableSearchHandler.setSearchCriterion(
                Parameter.CLASSIFICATION.toString(), "INCLUDED_IN_BOX", "Attraction");
        List<? extends Identifiable> searchResult = geoLocatableSearchHandler.getSearchResult();

        assertEquals(0, searchResult.size());
    }

    @Test
    void shouldRestartSearch() {
        geoLocatableSearchHandler.setSearchCriterion(
                Parameter.CLASSIFICATION.toString(), "INCLUDED_IN_BOX", "Attraction");
        geoLocatableSearchHandler.startSearch();
        geoLocatableSearchHandler.setSearchCriterion(
                Parameter.CLASSIFICATION.toString(), "EQUALS", "Activity");
        List<? extends Identifiable> searchResult = geoLocatableSearchHandler.getSearchResult();

        assertEquals(2, searchResult.size());
    }

    @Test
    void shouldGetFilteredItemsWithNoFilters() {
        List<? extends Identifiable> searchResult = SearchHandler.getFilteredItems(
                UserRepository.getInstance().getItemStream().toList(), List.of());

        assertEquals(6, searchResult.size());
    }

    @Test
    void shouldGetFilteredItemsWithOneFilter() {
        List<? extends Identifiable> searchResult = SearchHandler.getFilteredItems(
                MunicipalityRepository.getInstance().getAllContents().toList(), List.of(
                        new SearchFilter(Parameter.APPROVAL_STATUS.toString(),
                                "EQUALS", ApprovalStatusEnum.PENDING)));

        assertEquals(1, searchResult.size());
        assertEquals(SampleRepositoryProvider.contents.get(0).getID(), searchResult.get(0).getID());
    }

    @Test
    void shouldGetFilteredItemsWithMoreFilters() {
        List<? extends Identifiable> searchResult = SearchHandler.getFilteredItems(
                UserRepository.getInstance().getItemStream().toList(), List.of(
                        new SearchFilter(Parameter.USERNAME.toString(), "STARTS_WITH", "P"),
                        new SearchFilter(Parameter.EMAIL.toString(), "CONTAINS", "gmail")));

        assertEquals(2, searchResult.size());
        assertEquals(Set.of(SampleRepositoryProvider.users.get(0).getID(),
                        SampleRepositoryProvider.users.get(1).getID()),
                searchResult.stream().map(Identifiable::getID).collect(Collectors.toSet()));
    }
}