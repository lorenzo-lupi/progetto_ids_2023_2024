package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.UserDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.UserSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;


import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

/**
 * This class represents a handler for the modification of the authorization of a user.
 * It can only be accessed by a municipality administrator.
 */
public class ModifyAuthorizationHandler {

    /**
     * Modifies the authorization of a user.
     * @param administratorID the ID of the administrator
     * @param userID the ID of the user
     * @param newRole the new role
     * @throws UnsupportedOperationException if the user is not an administrator of the municipality
     */
    public static void modifyAuthorization(long administratorID, long userID, long municipalityID, AuthorizationEnum newRole) {
        modifyAuthorization(UserRepository.getInstance().getItemByID(administratorID),
                UserRepository.getInstance().getItemByID(userID),
                new Role(MunicipalityRepository.getInstance().getItemByID(municipalityID), newRole));
    }

    private static void modifyAuthorization(User administrator, User user, Role newRole) {
        if(newRole == null)
            throw new IllegalArgumentException("Role can't be null");

        if (!isAdministrator(administrator, newRole.municipality()))
            throw new UnsupportedOperationException("User is not an administrator of the municipality");

        user.addRole(newRole);
    }

    private static boolean isAdministrator(User user, Municipality municipality) {
        return user.getAuthorizations(municipality).stream()
                .anyMatch(a -> a.equals(AuthorizationEnum.ADMINISTRATOR));
    }




    /**
     * Returns the Synthesized Format of all the users registered in the system.
     * @param filters the filters to apply
     * @return the Synthesized Format of all the filtered users
     */
    @SuppressWarnings("unchecked")
    public static List<UserSOF> viewFilteredUsers(List<SearchFilter> filters) {
        return (List<UserSOF>) SearchHandler.getFilteredItems(UserRepository
                .getInstance()
                .getItemStream()
                .toList(), filters);
    }


    /**
     * Returns the Detailed Format of a User corresponding to the given ID.
     * @param userID the ID of the User to visualize
     * @return the Detailed Format of the User having the given ID
     */
    public static UserDOF viewUser(long userID) {
        return UserRepository.getInstance().getItemByID(userID).getDetailedFormat();
    }



}
