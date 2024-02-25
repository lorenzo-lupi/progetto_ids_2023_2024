package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.UserOF;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Modifiable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Searchable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.CredentialsUtils;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class represents a user of the application.
 */
@Entity
@Table(name = "app_user")
@NoArgsConstructor(force = true)
public class User implements Searchable, Visualizable, Modifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String username;
    private String name;
    private String email;
    private String encryptedPassword;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_ID", referencedColumnName = "ID"),
            inverseJoinColumns = {@JoinColumn(name = "role_municipality_ID", referencedColumnName = "municipality_ID"),
                    @JoinColumn(name = "role_authorizationEnum", referencedColumnName = "authorizationEnum")})
    private final List<Role> roles;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_notifications",
            joinColumns = @JoinColumn(name = "user_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "notification_ID", referencedColumnName = "ID"))
    private final List<Notification> notifications;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_savedcontents",
            joinColumns = @JoinColumn(name = "user_ID"),
            inverseJoinColumns = @JoinColumn(name = "content_ID"))
    private final List<Content<?>> savedContents;

    /**
     * Creates a new user with the given username and email.
     *
     * @param username the username of the user
     * @param email    the email of the user
     */
    public User(String username, String email, String encryptedPassword) {
        if (username == null || email == null || encryptedPassword == null)
            throw new IllegalArgumentException("Parameters cannot be null");
        if (!CredentialsUtils.isEmailValid(email))
            throw new IllegalArgumentException("Invalid email");
        if (!CredentialsUtils.isPasswordValid(encryptedPassword))
            throw new IllegalArgumentException("Invalid password");

        this.username = username;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
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
        return this.encryptedPassword;
    }

    public void setPassword(String password) {
        this.encryptedPassword = CredentialsUtils.getEncryptedPassword(password);
    }

    public boolean matchesPassword(String password) {
        return CredentialsUtils.matchesPassword(password, this.encryptedPassword);
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
    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void addRole(Municipality municipality, AuthorizationEnum authorizationEnum) {
        this.roles.add(new Role(municipality, authorizationEnum));
    }

    public List<Notification> getNotifications() {
        return this.notifications;
    }

    public void addNotification(Notification notification) {
        notification.getUsers().add(this);
        this.notifications.add(notification);
    }

    public void removeNotification(Notification notification) {
        this.notifications.remove(notification);
    }

    public List<Content<?>> getSavedContents() {
        return this.savedContents;
    }

    public boolean addSavedContent(Content<?> content) {
        this.savedContents.add(content);
        return content.getSavedContentUsers().add(this);
    }

    public boolean removeSavedContent(Content<?> content) {
        this.savedContents.remove(content);
        return content.getSavedContentUsers().remove(this);
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
     * @param municipality   the municipality
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
    public UserOF getOutputFormat() {
        return new UserOF(this.getUsername(), this.getEmail(), this.getRoles(), this.getID());
    }

    @Override
    public boolean equals(Object obj) {
        return equalsID(obj);
    }

    @PreRemove
    public void preRemove() {
        this.savedContents.forEach(content -> content.getSavedContentUsers().remove(this));
        this.notifications.forEach(n -> n.getUsers().remove(this));
    }
}
