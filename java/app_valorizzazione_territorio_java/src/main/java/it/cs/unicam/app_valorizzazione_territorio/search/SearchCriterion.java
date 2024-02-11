package it.cs.unicam.app_valorizzazione_territorio.search;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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

    public static final BiPredicate<Object,Object> EQUALS =
            (a, b) -> b.equals(a);
    public static final BiPredicate<Object,Object> STARTS_WITH =
            (a, b) -> a.toString().startsWith(b.toString());
    public static final BiPredicate<Object,Object> CONTAINS =
            (a, b) -> a.toString().contains(b.toString());
    public static final BiPredicate<Object, Object> INCLUDED_IN_BOX =
            (a, b) -> a instanceof Position p && b instanceof CoordinatesBox c && c.contains(p);
    public static final BiPredicate<Object, Object> INCLUDED_IN_COMPOUND_POINT =
            (a, b) -> a instanceof PointOfInterest g && b instanceof Collection c && c.contains(g);
    public static final BiPredicate<Object, Object> EQUALS_ID =
            (a, b) ->  a instanceof Long la && b instanceof Identifiable ic && ic.getID() == la;
    public static final BiPredicate<Object,  Object> CONTEST_PERMITS_USER =
            (a, b) -> a instanceof Contest c && b instanceof User u && c.permitsUser(u);
    public static final BiPredicate<Object, Object> USERNAME =
            (a, b) -> a instanceof String u && b instanceof String s && u.equals(s);

    public static final BiPredicate<Object, Object> TAKE_ALL =
            (a, b) -> true;
    /**
     * A Map that maps a String format to a BiPredicate that can be used for search.
     */
    public static final Map<String, BiPredicate<Object, Object>> stringToBiPredicate;

    static {
        stringToBiPredicate = new HashMap<>();
        stringToBiPredicate.put("EQUALS", EQUALS);
        stringToBiPredicate.put("STARTS_WITH", STARTS_WITH);
        stringToBiPredicate.put("CONTAINS", CONTAINS);
        stringToBiPredicate.put("INCLUDED_IN_BOX", INCLUDED_IN_BOX);
        stringToBiPredicate.put("INCLUDED_IN_COMPOUND_POINT", INCLUDED_IN_COMPOUND_POINT);
        stringToBiPredicate.put("EQUALS_ID", EQUALS_ID);
        stringToBiPredicate.put("CONTEST_PERMITS_USER", CONTEST_PERMITS_USER);
        stringToBiPredicate.put("USERNAME", USERNAME);
        stringToBiPredicate.put("TAKE_ALL", TAKE_ALL);
    }

    private final BiPredicate<Object, Object> predicate;
    private final T value;

    /**
     * Constructs a {@link SearchCriterion} with the given {@link BiPredicate} and value used for the search.
     * @param predicate the {@link BiPredicate} used for the search
     * @param value the value used for the search
     */
    public SearchCriterion(BiPredicate<Object,Object> predicate, T value) {
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
