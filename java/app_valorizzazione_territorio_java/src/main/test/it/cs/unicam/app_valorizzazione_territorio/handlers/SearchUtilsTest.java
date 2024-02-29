package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
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
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {SampleRepositoryProvider.class})
@DataJpaTest
class SearchUtilsTest {

    @Autowired
    private SampleRepositoryProvider sampleRepositoryProvider;

    @BeforeEach
    public void setUp() {
        sampleRepositoryProvider.setUpAllRepositories();
    }

    @AfterEach
    public void clear() {
        sampleRepositoryProvider.clearAllRepositories();
    }
    @Test
    void shouldGetFilteredItemsWithNoFilters() {
        List<? extends Identifiable> searchResult = SearchUtils.getFilteredItems(
                sampleRepositoryProvider.getUserJpaRepository().findAll(), List.of());

        assertEquals(8, searchResult.size());
    }

    @Test
    void shouldGetFilteredItemsWithOneFilter() {
        List<? extends Identifiable> searchResult = SearchUtils.getFilteredItems(
                sampleRepositoryProvider.getContentJpaRepository().findAll(), List.of(
                        new SearchFilter(Parameter.APPROVAL_STATUS.toString(),
                                "EQUALS", ApprovalStatusEnum.PENDING)));

        assertEquals(4, searchResult.size());
        assertEquals(sampleRepositoryProvider.FOTO_SAN_VENANZIO.getID(), searchResult.get(0).getID());
    }

    @Test
    void shouldGetFilteredItemsWithMoreFilters() {
        List<? extends Identifiable> searchResult = SearchUtils.getFilteredItems(
                sampleRepositoryProvider.getUserJpaRepository().findAll(), List.of(
                        new SearchFilter(Parameter.USERNAME.toString(), "STARTS_WITH", "P"),
                        new SearchFilter(Parameter.EMAIL.toString(), "CONTAINS", "gmail")));

        assertEquals(2, searchResult.size());
        assertEquals(Set.of(sampleRepositoryProvider.TURIST_1.getID(),
                        sampleRepositoryProvider.TURIST_2.getID()),
                searchResult.stream().map(Identifiable::getID).collect(Collectors.toSet()));
    }
}
