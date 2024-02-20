package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.UserIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalitySOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.CredentialsUtils;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

/**
 * This class represents a handler for the user entity.
 * It provides all the methods needed to manage the user entity.
 */
public class UserHandler {
    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final MunicipalityRepository municipalityRepository = MunicipalityRepository.getInstance();

    /**
     * This method registers a new user in the system.
     * @param userIF the user to register
     * @return the id of the registered user
     */
    public static long registerUser(UserIF userIF){
        if(!CredentialsUtils.isEmailValid(userIF.email()))
            throw new IllegalArgumentException("Invalid email");
        if(!CredentialsUtils.isPasswordValid(userIF.password()))
            throw new IllegalArgumentException("Invalid password");
        if(userExists(userIF.username()))
            throw new IllegalArgumentException("Username already exists");

        return insertUser(userIF);
    }


    /**
     * This method generates a new municipality administrator.
     * @param municipalityID the id of the municipality
     * @param userEmail the email of the user
     * @return the id of the generated municipality administrator
     */
    //TODO
    public static long generateMunicipalityAdministrator(long municipalityID,
                                                         String userEmail){
        return 0;
    }


    /**
     * This method returns the municipalities of the system.
     * @return the municipalities of the system
     */
    public static List<MunicipalitySOF> getMunicipalities(){
        return municipalityRepository
                .getItemStream()
                .map(Municipality::getSynthesizedFormat)
                .toList();
    }


    /**
     *  This method returns true if the user is allowed to modify
     *  the authorizations of the users in the system.
     *  @param userID the id of the user
     */
    public static boolean isAllowedToModifyAuthorizations(long userID){
        return userRepository
                .getItemByID(userID)
                .getRoles()
                .stream()
                .map(Role::authorizationEnum)
                .anyMatch(authorizationEnum -> authorizationEnum == AuthorizationEnum.ADMINISTRATOR);
    }

    /**
     * This method returns the user with the given id.
     * @param userID the id of the user
     * @return the user with the given id
     */
    public static UserDOF visualizeDetailedUser(long userID){
        return userRepository
                .getItemByID(userID)
                .getDetailedFormat();
    }

    /**
     * This method returns the filtered users of the system.
     * @param filters the filters to apply
     * @return the users of the system
     */
    @SuppressWarnings("unchecked")
    public static List<UserSOF> getFilteredUsers(List<SearchFilter> filters){
        return (List<UserSOF>)SearchHandler
                .getFilteredItems(userRepository.getItemStream().toList(), filters);
    }

    /**
     * This method modifies the authorizations of the user with the given id.
     * It can be used only if the administrator is an administrator in at most ONE municipality.
     * @param administratorID the id of the administrator
     * @param userID the id of the user to modify
     * @param newAuthorizations the new authorizations
     */
    public static void modifyUserAuthorization(long administratorID,
                                                  long userID,
                                                  List<AuthorizationEnum> newAuthorizations){
        User user = userRepository.getItemByID(userID);
        User administrator = userRepository.getItemByID(administratorID);
        Municipality municipality = getMunicipalityOfAdministrator(administrator);

        modifyUserAuthorization(user, municipality, newAuthorizations);
    }



    /**
     * This method modifies the authorizations of the user with the given id.
     * @param administratorID the id of the administrator
     * @param userID the id of the user to modify
     * @param authorizationsEnums the new authorizations
     */
    public static void modifyUserAuthorization(long municipalityID,
                                                  long administratorID,
                                                  long userID,
                                                  List<AuthorizationEnum> authorizationsEnums){
        User user = userRepository.getItemByID(userID);
        User administrator = userRepository.getItemByID(administratorID);
        Municipality municipality = municipalityRepository.getItemByID(municipalityID);

        if(!administrator.getAuthorizations(municipality).contains(AuthorizationEnum.ADMINISTRATOR))
            throw new IllegalArgumentException("user is not administrator for the given municipality");

        modifyUserAuthorization(user, municipality, authorizationsEnums);
    }

    /**
     * This method returns the municipalities administrated by the user with the given id.
     * @param userID the id of the user
     * @return the municipalities administrated by the user with the given id
     */
    public static List<MunicipalitySOF> getMunicipalitiesAdministratedByUser(long userID){
        return userRepository
                .getItemByID(userID)
                .getRoles()
                .stream()
                .filter(r -> r.authorizationEnum() == AuthorizationEnum.ADMINISTRATOR)
                .map(Role::municipality)
                .map(Municipality::getSynthesizedFormat)
                .toList();
    }

    //TODO
    public static List<String> getSearchParameters(){
        return null;
    }

    //TODO
    public static List<String> getSearchCriteria(){
        return null;
    }

    private static boolean userExists(String username){
        SearchEngine<User> searchEngine = new SearchEngine<>(userRepository.getItemStream().toList());
        searchEngine.addCriterion(Parameter.USERNAME, new SearchCriterion<>(SearchCriterion.USERNAME, username));
        return !searchEngine.search().getResults().isEmpty();
    }

    private static long insertUser(UserIF userIF){
        User user = new User(userIF.username(), userIF.email(), userIF.password());
        userRepository.add(user);
        return user.getID();
    }

    private static Municipality getMunicipalityOfAdministrator(User administrator){
        List<Municipality> municipalities = administrator.getRoles()
                .stream()
                .filter(role -> role.authorizationEnum() == AuthorizationEnum.ADMINISTRATOR)
                .map(Role::municipality)
                .toList();

        if(municipalities.size() != 1)
            throw new IllegalArgumentException("The administrator is not an administrator for only one municipality");

        return municipalities.get(0);
    }

    private static void modifyUserAuthorization(User user,
                                                Municipality municipality,
                                                List<AuthorizationEnum> authorizationEnums){
        authorizationEnums.forEach(
                authorizationEnum -> user.setNewRoles(authorizationEnums, municipality)
        );
    }
}
