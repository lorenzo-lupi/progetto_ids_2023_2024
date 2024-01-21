package it.cs.unicam.app_valorizzazione_territorio.abstractions;

/**
 * Classes implementing this interface can provide specific objects for their synthetic
 * and detailed visualization.
 * They also provide a unique identifier in order to be identified in the system from their
 * synthetic or detailed representation.
 */
public interface Visualizable extends Identifiable{

    /**
     * Returns a record containing the synthetic information of the object.
     *
     * @return a record containing the synthetic information of the object
     */
    Identifiable getSynthesizedFormat();

    /**
     * Returns a record containing the detailed information of the object.
     *
     * @return a record containing the detailed information of the object
     */
    Identifiable getDetailedFormat();
}
