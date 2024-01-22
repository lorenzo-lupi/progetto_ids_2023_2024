package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserSOF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a user of the application.
 */
public class User implements Identifiable, Visualizable {
    private String username;
    private final String email;
    private final List<Role> roles;

    private final long ID = UserRepository.getInstance().getNextID();

    /**
     * Creates a new user with the given username and email.
     *
     * @param username the username of the user
     * @param email the email of the user
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.roles = new ArrayList<>();
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addRole(Municipality municipality, RoleTypeEnum roleTypeEnum) {
        this.roles.add(new Role(municipality, roleTypeEnum));
    }

    /**
     * Returns the authorizations of the user in the given municipality.
     *
     * @param municipality the municipality
     * @return the authorizations of the user in the given municipality
     */
    public Set<RoleTypeEnum> getAuthorizations(Municipality municipality) {
        return this.roles.stream()
                .filter(role -> role.municipality().equals(municipality))
                .map(Role::roleTypeEnum)
                .collect(Collectors.toSet());
    }

    @Override
    public long getID() {
        return this.ID;
    }

    @Override
    public UserSOF getSynthesizedFormat() {
        return new UserSOF(this.getUsername(), this.getID());
    }

    @Override
    public UserDOF getDetailedFormat() {
        return new UserDOF(this.getUsername(), this.getEmail(), this.getRoles(), this.getID());
    }
}
