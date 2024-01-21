package it.cs.unicam.app_valorizzazione_territorio.model;

import java.util.function.Predicate;

/**
 * This class represents relationship between a {@link Municipality} and a {@link RoleTypeEnum}.
 * It is used to represent the role of a {@link User} in a {@link Municipality}.
 */
public record Role(Municipality municipality, RoleTypeEnum roleTypeEnum) {
    public static Predicate<User> isCuratorForMunicipality(Municipality municipality){
        return user -> user.getRoles().stream()
                .anyMatch(role -> role.roleTypeEnum() == RoleTypeEnum.CURATOR
                        && role.municipality().equals(municipality));
    }

    public static Predicate<User> isContributorForMunicipality(Municipality municipality){
        return user -> user.getRoles().stream()
                .anyMatch(role -> role.roleTypeEnum() == RoleTypeEnum.CONTRIBUTOR
                        && role.municipality().equals(municipality));
    }

}
