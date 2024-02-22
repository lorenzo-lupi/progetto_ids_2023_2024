package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.List;

@JsonView(View.Synthesized.class)
public record MapOF(
        String osmData,
        List<Identifiable> points,
        long id
)
        implements Identifiable{

    @Override
    public long getID() {
        return this.id();
    }
}
