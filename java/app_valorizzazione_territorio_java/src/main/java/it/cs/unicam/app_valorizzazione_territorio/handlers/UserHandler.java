package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.UserIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.UserOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SMTPRequestHandler;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.CredentialsUtils;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents a handler for the user entity.
 * It provides all the methods needed to manage the user entity and
 * to generate a municipality administrator.
 */
@Service
public class UserHandler {
    private final UserJpaRepository userRepository;
    private final MunicipalityJpaRepository municipalityRepository;
    private final SMTPRequestHandler smtpRequestHandler;

    @Autowired
    public UserHandler(UserJpaRepository userRepository,
                       MunicipalityJpaRepository municipalityRepository,
                       SMTPRequestHandler smtpRequestHandler) {
        this.userRepository = userRepository;
        this.municipalityRepository = municipalityRepository;
        this.smtpRequestHandler = smtpRequestHandler;
    }

    /**
     * This method registers a new user in the system.
     *
     * @param userIF the user to register
     * @return the id of the registered user
     */
    public long registerUser(UserIF userIF) {
        if (!CredentialsUtils.isEmailValid(userIF.email()))
            throw new IllegalArgumentException("Invalid email");
        if (!CredentialsUtils.isPasswordValid(userIF.password()))
            throw new IllegalArgumentException("Invalid password");
        if (userExists(userIF.username()))
            throw new IllegalArgumentException("Username already exists");

        return insertUser(userIF);
    }


    /**
     * Generates a municipality admin for the given municipality, setting its email to the given email
     * and adding the admin role to the user.
     * Username is set to the name of the municipality concatenated with "Admin",
     * while the password is randomly generated.
     * If the generation of the user is successful, an email containing the credentials is sent to the given
     * email address.
     *
     * @param municipalityID the id of the municipality
     * @param userEmail      the email of the user
     * @return the id of the generated municipality administrator
     */
    public long generateMunicipalityAdministrator(long municipalityID, String userEmail) {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty())
            throw new IllegalArgumentException("Municipality not found");
        String password = CredentialsUtils.getRandomPassword();
        User municipalityAdmin = new User(municipality.get().getName() + "Admin", userEmail, password);
        municipalityAdmin.addRole(new Role(municipality.get(), AuthorizationEnum.ADMINISTRATOR));

        //Google Workspace account needed
        smtpRequestHandler.sendEmail(userEmail, "Credenziali di accesso",
        "Username: " + municipalityAdmin.getUsername() + "\nPassword: " + password);

        userRepository.save(municipalityAdmin);
        return municipalityAdmin.getID();
    }


    /**
     * This method returns the municipalities of the system.
     *
     * @return the municipalities of the system
     */
    public List<MunicipalityOF> getMunicipalities() {
        return municipalityRepository
                .findAll()
                .stream()
                .map(Municipality::getOutputFormat)
                .toList();
    }


    /**
     * This method returns true if the user is allowed to modify
     * the authorizations of the users in the system.
     *
     * @param userID the id of the user
     */
    public boolean isAllowedToModifyAuthorizations(long userID) {
        Optional<User> user = userRepository.getByID(userID);
        if (user.isEmpty())
            throw new IllegalArgumentException("User not found");
        return user
                .get()
                .getRoles()
                .stream()
                .map(Role::authorizationEnum)
                .anyMatch(authorizationEnum -> authorizationEnum == AuthorizationEnum.ADMINISTRATOR);
    }

    /**
     * This method returns the user with the given id.
     *
     * @param userID the id of the user
     * @return the user with the given id
     */
    public UserOF visualizeDetailedUser(long userID) {
        Optional<User> user = userRepository.getByID(userID);
        if(user.isEmpty())
            throw new IllegalArgumentException("User not found");
        return user
                .get()
                .getOutputFormat();
    }

    /**
     * This method returns the filtered users of the system.
     *
     * @param filters the filters to apply
     * @return the users of the system
     */
    @SuppressWarnings("unchecked")
    public List<UserOF> getFilteredUsers(List<SearchFilter> filters) {
        return (List<UserOF>) SearchUtils
                .getFilteredItems(userRepository.findAll(), filters);
    }

    /**
     * Returns the set of all the criteria available for the search.
     *
     * @return the set of all the criteria available for the search
     */
    public Set<String> getSearchCriteria() {
        return SearchUtils.getSearchCriteria();
    }

    /**
     * This method returns the search parameters for the user entity.
     *
     * @return the search parameters for the user entity
     */
    public List<String> getSearchParameters() {
        return (new User("serachParametersObject", "email_email@email.email", "AbcDe123!!AC"))
                .getParameters()
                .stream()
                .map(Parameter::toString)
                .toList();
    }

    /**
     * This method modifies the authorizations of the user with the given id.
     * It can be used only if the administrator is an administrator in at most ONE municipality.
     *
     * @param administratorID   the id of the administrator
     * @param userID            the id of the user to modify
     * @param newAuthorizations the new authorizations
     */
    public void modifyUserAuthorization(long administratorID,
                                        long userID,
                                        List<AuthorizationEnum> newAuthorizations) {
        Optional<User> user = userRepository.getByID(userID);
        Optional<User> administrator = userRepository.getByID(administratorID);
        if (user.isEmpty() || administrator.isEmpty())
            throw new IllegalArgumentException("User not found");
        Municipality municipality = getMunicipalityOfAdministrator(administrator.get());

        modifyUserAuthorization(user.get(), municipality, newAuthorizations);
    }


    /**
     * This method modifies the authorizations of the user with the given id.
     *
     * @param administratorID     the id of the administrator
     * @param userID              the id of the user to modify
     * @param authorizationsEnums the new authorizations
     */
    public void modifyUserAuthorization(long municipalityID,
                                        long administratorID,
                                        long userID,
                                        List<AuthorizationEnum> authorizationsEnums) {
        Optional<User> user = userRepository.getByID(userID);
        Optional<User> administrator = userRepository.getByID(administratorID);
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);

        if (user.isEmpty() || administrator.isEmpty() || municipality.isEmpty())
            throw new IllegalArgumentException("User or municipality not found");

        if (!administrator.get().getAuthorizations(municipality.get()).contains(AuthorizationEnum.ADMINISTRATOR))
            throw new IllegalArgumentException("user is not administrator for the given municipality");

        modifyUserAuthorization(user.get(), municipality.get(), authorizationsEnums);
    }

    /**
     * This method returns the municipalities administrated by the user with the given id.
     *
     * @param userID the id of the user
     * @return the municipalities administrated by the user with the given id
     */
    public List<MunicipalityOF> getMunicipalitiesAdministratedByUser(long userID) {
        Optional<User> user = userRepository.getByID(userID);
        if (user.isEmpty())
            throw new IllegalArgumentException("User not found");
        return  user.get()
                .getRoles()
                .stream()
                .filter(r -> r.authorizationEnum() == AuthorizationEnum.ADMINISTRATOR)
                .map(Role::municipality)
                .map(Municipality::getOutputFormat)
                .toList();
    }


    private boolean userExists(String username) {
        SearchEngine<User> searchEngine = new SearchEngine<>(userRepository.findAll());
        searchEngine.addCriterion(Parameter.USERNAME, new SearchCriterion<>(SearchCriterion.USERNAME, username));
        return !searchEngine.search().getResults().isEmpty();
    }

    private long insertUser(UserIF userIF) {
        User user = new User(userIF.username(), userIF.email(), userIF.password());
        return userRepository.saveAndFlush(user).getID();
    }

    private Municipality getMunicipalityOfAdministrator(User administrator) {
        List<Municipality> municipalities = administrator.getRoles()
                .stream()
                .filter(role -> role.authorizationEnum() == AuthorizationEnum.ADMINISTRATOR)
                .map(Role::municipality)
                .toList();

        if (municipalities.size() != 1)
            throw new IllegalArgumentException("The administrator is not an administrator for only one municipality");

        return municipalities.get(0);
    }

    private void modifyUserAuthorization(User user,
                                                Municipality municipality,
                                                List<AuthorizationEnum> authorizationEnums) {
        authorizationEnums.forEach(
                authorizationEnum -> user.setNewRoles(authorizationEnums, municipality)
        );
        userRepository.saveAndFlush(user);
    }
}
