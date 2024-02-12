package it.cs.unicam.app_valorizzazione_territorio.model;

import java.util.function.Predicate;

/**
 * This class represents relationship between a {@link Municipality} and a {@link AuthorizationEnum}.
 * It is used to represent the role of a {@link User} in a {@link Municipality}.
 */
public record Role(Municipality municipality, AuthorizationEnum authorizationEnum) {
    public static Predicate<User> isCuratorForMunicipality(Municipality municipality){
        return user -> user.getRoles().stream()
                .anyMatch(role -> role.authorizationEnum() == AuthorizationEnum.CURATOR
                        && role.municipality().equals(municipality));
    }

    public static Predicate<User> isContributorForMunicipality(Municipality municipality){
        return user -> user.getRoles().stream()
                .anyMatch(role -> role.authorizationEnum() == AuthorizationEnum.CONTRIBUTOR
                        && role.municipality().equals(municipality));
    }

    public static Predicate<User> isAtLeastContributorForMunicipality(Municipality municipality){
        return isCuratorForMunicipality(municipality).or(isContributorForMunicipality(municipality));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Role role = (Role) obj;
        return municipality.equals(role.municipality) && authorizationEnum == role.authorizationEnum;
    }

    @Override
    public int hashCode() {
        return 31 * municipality.hashCode() + authorizationEnum.hashCode();
    }

}
