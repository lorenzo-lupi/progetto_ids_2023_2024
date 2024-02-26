package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Positionable;

import java.util.List;

@JsonView(View.Synthesized.class)
public record MapOF(
        String osmData,
        List<Identifiable> points
)
        implements Identifiable{
    @Override
    public long getID() {
        return 0L;
    }
}
