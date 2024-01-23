package it.cs.unicam.app_valorizzazione_territorio.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Supplier;

public enum CompoundPointTypeEnum {
    ITINERARY(LinkedList::new),
    EXPERIENCE(HashSet::new);

    private final Supplier<Collection<PointOfInterest>> collectionSupplier;
    CompoundPointTypeEnum(Supplier<Collection<PointOfInterest>> collectionSupplier){
        this.collectionSupplier = collectionSupplier;
    }

    public Collection<PointOfInterest> getCollection(){
        return collectionSupplier.get();
    }

    public static CompoundPointTypeEnum fromString(String type) {
        return switch (type) {
            case "itinerary" -> ITINERARY;
            case "experience" -> EXPERIENCE;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }
}
