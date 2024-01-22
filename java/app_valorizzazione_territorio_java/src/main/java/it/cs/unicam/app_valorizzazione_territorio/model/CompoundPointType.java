package it.cs.unicam.app_valorizzazione_territorio.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Supplier;

public enum CompoundPointType {
    ITINERARY(LinkedList::new),
    EXPERIENCE(HashSet::new);

    private final Supplier<Collection<PointOfInterest>> collectionSupplier;
    CompoundPointType(Supplier<Collection<PointOfInterest>> collectionSupplier){
        this.collectionSupplier = collectionSupplier;
    }

    public Collection<PointOfInterest> getCollection(){
        return collectionSupplier.get();
    }
}
