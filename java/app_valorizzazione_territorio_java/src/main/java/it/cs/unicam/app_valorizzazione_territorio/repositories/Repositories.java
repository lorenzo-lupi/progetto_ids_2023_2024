package it.cs.unicam.app_valorizzazione_territorio.repositories;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a singleton that contains all the repositories of the system.
 */
public class Repositories {
    private final Map<Class<?>, Repository<?>> repositories;

    private static final Repositories instance = new Repositories();

    private Repositories() {
        this.repositories = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the class.
     * @return the singleton instance of the class
     */
    public static Repositories getInstance() {
        return instance;
    }

    /**
     * Returns the repository of the specified type.
     * @param type the type of the repository
     * @param <T> the type of the repository
     * @return the repository of the specified type
     */
    @SuppressWarnings("unchecked")
    public <T> Repository<T> getRepository(Class<T> type) {
        if (!this.repositories.containsKey(type))
            this.repositories.put(type, new Repository<>());
        return (Repository<T>) this.repositories.get(type);
    }
}
