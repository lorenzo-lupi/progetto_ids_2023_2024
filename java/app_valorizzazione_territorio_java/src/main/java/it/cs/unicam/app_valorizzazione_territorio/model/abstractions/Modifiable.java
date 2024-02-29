package it.cs.unicam.app_valorizzazione_territorio.model.abstractions;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Classes implementing this interface provide a set of modifiable parameters and their associated
 * setters in order for their objects to be modified.
 */
public interface Modifiable {
    /**
     * Returns the mapping between modifiable parameters and their associated setters for this object.
     * @return
     */
    Map<Parameter, Consumer<Object>> getSettersMapping();

    /**
     * Returns the set of modifiable parameters defined for this object.
     * @return
     */
    default Set<Parameter> getModifiableParameters() {
        return getSettersMapping().keySet();
    }

    /**
     * Returns the modification procedure for the given setter method and the type of its argument,
     * in order to be used with object values.
     *
     * @param setter the setter method
     * @param clazz the type of the argument
     * @return the modification procedure
     */
    default <T> Consumer<Object> toObjectSetter(Consumer<? super T> setter, Class<T> clazz) {
        return (o) -> {
            if (clazz.isInstance(o)) {
                setter.accept((clazz.cast(o)));
            }
        };
    }
}
