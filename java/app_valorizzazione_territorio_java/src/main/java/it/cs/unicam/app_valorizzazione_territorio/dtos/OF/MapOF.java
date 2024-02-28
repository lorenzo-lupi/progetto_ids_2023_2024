package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

import java.util.List;

@JsonView(View.Synthesized.class)
public record MapOF(
        Object osmData,
        @JsonFilter(value = "Synthesized")
        List<Identifiable> points
)
        implements Identifiable{
    @JsonIgnore
    @Override
    public long getID() {
        return 0L;
    }
}
