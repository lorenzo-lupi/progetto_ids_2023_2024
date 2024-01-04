package search;

import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.repositories.Repositories;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class SearchEngineTest {
    @BeforeAll
    public static void setUpMunicipalityRepositories() {
        Repositories.getInstance().getRepository(Municipality.class)
                .addAll(List.of(
                        new Municipality("Macerata", "Comune di Macerata",
                                new Position(43.29812657107886, 13.451878161920886),
                                new CoordinatesBox(new Position(43.317324, 13.409422), new Position (43.271074, 13.499990))),
                        new Municipality("Camerino", "Comune di Camerino",
                                new Position(43.13644468556232, 13.067156069846892),
                                new CoordinatesBox(new Position(43.153712, 13.036414), new Position (43.123261, 13.095768)))
                ));
    }
}
