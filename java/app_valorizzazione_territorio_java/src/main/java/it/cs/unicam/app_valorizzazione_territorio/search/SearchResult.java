package it.cs.unicam.app_valorizzazione_territorio.search;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a search result produced by a {@link SearchEngine}.
 *
 * @param <S> the type of the {@link Searchable} and {@link Visualizable} items in the result
 */
public class SearchResult<S extends Visualizable> {
    private final List<S> results;

    public SearchResult() {
        this.results = new ArrayList<>();
    }

    public void addResult(S result) {
        this.results.add(result);
    }

    public List<S> getResults() {
        return results;
    }

}
