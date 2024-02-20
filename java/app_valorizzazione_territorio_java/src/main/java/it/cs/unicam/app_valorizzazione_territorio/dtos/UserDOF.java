package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;

import java.util.List;

/**
 * This class represents a User Data Output Format object.
 *
 * @param username
 * @param email
 * @param roles
 * @param ID
 */
public record UserDOF(String username,
                      String email,
                      List<Role> roles,
                      long ID) implements Identifiable {
    @Override
    public long getID() {
        return this.ID();
    }
}
