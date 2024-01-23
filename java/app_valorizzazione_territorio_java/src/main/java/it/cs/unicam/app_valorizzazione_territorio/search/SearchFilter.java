package it.cs.unicam.app_valorizzazione_territorio.search;

/**
 * This class represents a search filter that can be used to filter a collection of searchable items.
 * A search filter is composed by all the information needed to perform a search,
 * such as the object parameter to test, a predicate representing the criterion and a reference value.
 *
 * The parameter and the predicate are represented by a {@link String} format.
 * In order to provide valid results, parameter and predicate must be mapped to a valid {@link Parameter}
 * and {@link SearchCriterion} respectively, and must compatible with each other.
 *
 * Providing invalid parameters or predicates will result in an {@link IllegalArgumentException}
 * during construction, while providing incompatible parameters and predicates or invalid values
 * will result in an empty search result during search.
 *
 * @param parameter a string representation of the parameter of the object to test
 * @param predicate a string representation of the predicate representing the search criterion
 * @param value the reference value
 */
public record SearchFilter(String parameter, String predicate, Object value) {

    /**
     * Constructs a {@link SearchFilter} with the given parameters.
     *
     * @param parameter a string representation of the parameter of the object to test
     * @param predicate a string representation of the predicate representing the search criterion
     * @param value the reference value
     * @throws IllegalArgumentException if any of the parameters is null or invalid
     */
    public SearchFilter {
        if (parameter == null || predicate == null || value == null)
            throw new IllegalArgumentException("Parameters must not be null");

        if (!Parameter.stringToParameter.containsKey(parameter))
            throw new IllegalArgumentException("Invalid parameter");

        if (!SearchCriterion.stringToBiPredicate.containsKey(predicate))
            throw new IllegalArgumentException("Invalid predicate");
    }
}
