package it.cs.unicam.app_valorizzazione_territorio.search;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a search result produced by a {@link SearchEngine}.
 *
 * @param <S> the type of the {@link Searchable} items in the result
 */
public class SearchResult<S extends Searchable> {
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
