package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.RoleTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.Request;
import it.cs.unicam.app_valorizzazione_territorio.requests.RequestFactory;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the promotion requests.
 */
public class PromotionRequestHandler {
    /**
     * Returns the possible promotions.
     *
     * @return the possible promotions
     */
    public static List<String> obtainPossiblePromotions() {
        return Arrays.stream(RoleTypeEnum.values())
                .filter(p -> !p.equals(RoleTypeEnum.ADMINISTRATOR))
                .map(Enum::toString)
                .toList();
    }

    /**
     * Inserts a promotion request for the user corresponding to the given ID, for the municipality corresponding to the given ID,
     *
     * @param userId         the ID of the user
     * @param municipalityId the ID of the municipality
     * @param roleType       the role type
     * @param message        the message
     */
    public static long insertPromotionRequest(long userId,
                                              long municipalityId,
                                              RoleTypeEnum roleType,
                                              String message) {
        if (roleType == null || message == null)
            throw new IllegalArgumentException("parameters can't be null");

        Municipality municipality = MunicipalityRepository.getInstance().getItemByID(municipalityId);
        User user = UserRepository.getInstance().getItemByID(userId);

        if (user.getAuthorizations(MunicipalityRepository.getInstance().getItemByID(municipalityId)).contains(roleType))
            throw new UnsupportedOperationException("User is already a " + roleType.toString());

        Request<User> request = RequestFactory.getPromotionRequest(UserRepository.getInstance().getItemByID(userId),
                new Role(municipality, roleType),
                message);

        RequestRepository.getInstance().add(request);

        return request.getID();
    }

}
