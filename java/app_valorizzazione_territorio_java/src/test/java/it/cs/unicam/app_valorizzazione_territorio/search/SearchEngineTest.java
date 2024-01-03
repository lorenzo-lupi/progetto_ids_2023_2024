package it.cs.unicam.app_valorizzazione_territorio.search;

import it.cs.unicam.app_valorizzazione_territorio.model.*;
import it.cs.unicam.app_valorizzazione_territorio.repositories.Repositories;
import it.cs.unicam.app_valorizzazione_territorio.repositories.Repository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchEngineTest {

    private Repository<Searchable> testRepository;

    @BeforeAll
    void buildTestRepository(){
        Municipality municipality1 = new Municipality("Comune1", "__", null, null);
        Municipality municipality2 = new Municipality("Comune1", "__", null, null);
        Municipality municipality3 = new Municipality("Comune1", "__", null, null);

        testRepository = new Repository<>();
        testRepository.add(new Content("Descrizione test", Collections.emptyList()));
        testRepository.add(new Content("Descrizione montagna", Collections.emptyList()));
        testRepository.add(new Content("Descrizione mare", Collections.emptyList()));
        testRepository.add(new GeoLocalizable(new Position(30, 40), "test", List.of(), municipality1));
        testRepository.add(new GeoLocalizable(new Position(30, 40), "test", List.of(), municipality3));
        testRepository.add(municipality2);
    }

    @Test
    void researchTest(){
        //assertEquals(6, )

    }





}