package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUltils;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchUltilsTest {
/*
    @BeforeAll
    static void setUpRepositories() {
        SampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterAll
    static void clearRepositories() {
        SampleRepositoryProvider.clearAllRepositories();
    }
    @Test
    void shouldGetFilteredItemsWithNoFilters() {
        List<? extends Identifiable> searchResult = SearchUltils.getFilteredItems(
                SampleRepositoryProvider.users, List.of());

        assertEquals(8, searchResult.size());
    }

    @Test
    void shouldGetFilteredItemsWithOneFilter() {
        List<? extends Identifiable> searchResult = SearchUltils.getFilteredItems(
                SampleRepositoryProvider.contents, List.of(
                        new SearchFilter(Parameter.APPROVAL_STATUS.toString(),
                                "EQUALS", ApprovalStatusEnum.PENDING)));

        assertEquals(4, searchResult.size());
        assertEquals(SampleRepositoryProvider.FOTO_SAN_VENANZIO.getID(), searchResult.get(0).getID());
    }

    @Test
    void shouldGetFilteredItemsWithMoreFilters() {
        List<? extends Identifiable> searchResult = SearchUltils.getFilteredItems(
                SampleRepositoryProvider.users, List.of(
                        new SearchFilter(Parameter.USERNAME.toString(), "STARTS_WITH", "P"),
                        new SearchFilter(Parameter.EMAIL.toString(), "CONTAINS", "gmail")));

        assertEquals(2, searchResult.size());
        assertEquals(Set.of(SampleRepositoryProvider.TURIST_1.getID(),
                        SampleRepositoryProvider.TURIST_2.getID()),
                searchResult.stream().map(Identifiable::getID).collect(Collectors.toSet()));
    }

 */
}
