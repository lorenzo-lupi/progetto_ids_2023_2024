package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Modifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserSOF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class represents a user of the application.
 */
public class User implements Searchable, Visualizable, Modifiable {
    private String username;
    private String email;
    private final List<Role> roles;
    private final List<Notification> notifications;
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
        this.notifications = new ArrayList<>();
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
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

    public List<Notification> getNotifications() {
        return this.notifications;
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    public void removeNotification(Notification notification) {
        this.notifications.remove(notification);
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
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parameters = new HashMap<>();
        parameters.put(Parameter.USERNAME, this.getUsername());
        parameters.put(Parameter.EMAIL, this.getEmail());
        return parameters;
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

    @Override
    public boolean equals(Object obj) {
        return equalsID(obj);
    }

    @Override
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        return Map.of(Parameter.USERNAME, toObjectSetter(this::setUsername, String.class),
                Parameter.EMAIL, toObjectSetter(this::setEmail, String.class),
                Parameter.ADD_ROLE, toObjectSetter(this::addRole, Role.class));
    }
}
