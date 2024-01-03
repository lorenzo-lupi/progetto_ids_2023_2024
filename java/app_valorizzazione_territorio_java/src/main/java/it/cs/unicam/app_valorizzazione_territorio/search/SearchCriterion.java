package it.cs.unicam.app_valorizzazione_territorio.search;

import it.cs.unicam.app_valorizzazione_territorio.model.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.model.GeoLocalizable;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * This class represents a search criteria used by {@link SearchEngine}.
 * This class also provides some useful {@link BiPredicate} implementations for the construction
 * of a {@link SearchCriterion}.
 *
 * @param <T> the type of the value used for the search
 */
public class SearchCriterion<T> implements Predicate<Object> {
    public static final BiPredicate<Object,Object> EQUALS = (a, b) -> b.equals(a);
    public static final BiPredicate<Object,Object> STARTS_WITH = (a, b) -> a.toString().startsWith(b.toString());
    public static final BiPredicate<Object,Object> CONTAINS = (a, b) -> a.toString().contains(b.toString());
    public static final BiPredicate<Object, CoordinatesBox> INCLUDED_IN_BOX = (a, b) -> a instanceof Position p && b.contains(p);
    public static final BiPredicate<Object, Collection<GeoLocalizable>> INCLUDED_IN_COLLECTION = (a, b) -> a instanceof GeoLocalizable g && b.contains(g);
    private final BiPredicate<Object,T> predicate;
    private final T value;

    /**
     * Constructs a {@link SearchCriterion} with the given {@link BiPredicate} and value used for the search.
     * @param predicate the {@link BiPredicate} used for the search
     * @param value the value used for the search
     */
    public SearchCriterion(BiPredicate<Object,T> predicate, T value) {
        this.predicate = predicate;
        this.value = value;
    }

    /**
     * Tests the given parameter value with the value used for the search.
     * @param o the parameter value
     * @ return true if the given parameter value matches the value used for the search, false otherwise
     */
    @Override
    public boolean test(Object o) {
        return predicate.test(o,value);
    }
}
