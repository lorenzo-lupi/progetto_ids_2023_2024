package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.model.User;

/**
 * This singleton class provides a repository for the users of the system.
 */
public class UserRepository extends Repository<User>{
    private static UserRepository instance;

    private UserRepository() {
        super();
    }

    public static UserRepository getInstance() {
        if (instance == null) instance = new UserRepository();
        return instance;
    }
}
