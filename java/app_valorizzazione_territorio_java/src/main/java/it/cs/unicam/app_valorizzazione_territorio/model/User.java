package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Modifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.CredentialsUtils;
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
    private String name;
    private String email;
    private String password;
    private final List<Role> roles;
    private final List<Notification> notifications;

    private final List<Content> savedContents;
    private final long ID = UserRepository.getInstance().getNextID();

    /**
     * Creates a new user with the given username and email.
     *
     * @param username the username of the user
     * @param email the email of the user
     */
    public User(String username, String email, String password) {
        if (username == null || email == null || password == null)
            throw new IllegalArgumentException("Parameters cannot be null");
        if (!CredentialsUtils.isEmailValid(email))
            throw new IllegalArgumentException("Invalid email");
        if (!CredentialsUtils.isPasswordValid(password))
            throw new IllegalArgumentException("Invalid password");

        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.savedContents = new ArrayList<>();
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEncryptedPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
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

    public void addRole(Municipality municipality, AuthorizationEnum authorizationEnum) {
        this.roles.add(new Role(municipality, authorizationEnum));
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

    public List<Content> getSavedContents() {
        return this.savedContents;
    }

    public boolean addSavedContent(Content content) {
        return this.savedContents.add(content);
    }

    public boolean removeSavedContent(Content content) {
        return this.savedContents.remove(content);
    }

    /**
     * Returns the authorizations of the user in the given municipality.
     *
     * @param municipality the municipality
     * @return the authorizations of the user in the given municipality
     */
    public Set<AuthorizationEnum> getAuthorizations(Municipality municipality) {
        return this.roles.stream()
                .filter(role -> role.municipality().equals(municipality))
                .map(Role::authorizationEnum)
                .collect(Collectors.toSet());
    }

    /**
     * Sets a new set of roles for the user in the given municipality.
     * The previous roles in the given municipality are removed and replaced with the new ones.
     *
     * @param authorizations the new authorizations
     * @param municipality the municipality
     */
    public void setNewRoles(List<AuthorizationEnum> authorizations, Municipality municipality) {
        this.roles.removeIf(role -> role.municipality().equals(municipality));
        authorizations.forEach(authorization -> this.roles.add(new Role(municipality, authorization)));
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parameters = new HashMap<>();
        parameters.put(Parameter.USERNAME, this.getUsername());
        parameters.put(Parameter.NAME, this.getName());
        parameters.put(Parameter.EMAIL, this.getEmail());
        return parameters;
    }

    @Override
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        return Map.of(Parameter.USERNAME, toObjectSetter(this::setUsername, String.class),
                Parameter.NAME, toObjectSetter(this::setName, String.class),
                Parameter.EMAIL, toObjectSetter(this::setEmail, String.class),
                Parameter.ADD_ROLE, toObjectSetter(this::addRole, Role.class));
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
}
