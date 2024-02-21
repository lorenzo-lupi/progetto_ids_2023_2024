package it.cs.unicam.app_valorizzazione_territorio.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * This class represents relationship between a {@link Municipality} and a {@link AuthorizationEnum}.
 * It is used to represent the role of a {@link User} in a {@link Municipality}.
 */
@Entity
@NoArgsConstructor(force = true)
public class Role {
    @EmbeddedId
    private RoleKey roleKey;

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

    public Role(Municipality municipality, AuthorizationEnum authorizationEnum) {
        this.roleKey = new RoleKey(municipality, authorizationEnum);
    }

    public Municipality municipality() {
        return roleKey.municipality;
    }

    public AuthorizationEnum authorizationEnum() {
        return roleKey.authorizationEnum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Role role = (Role) obj;
        return roleKey.municipality.equals(role.municipality()) && roleKey.authorizationEnum == role.authorizationEnum();
    }

    @Override
    public int hashCode() {
        return 31 * roleKey.municipality.hashCode() + roleKey.authorizationEnum.hashCode();
    }

    @Embeddable
    @NoArgsConstructor(force = true)
    private class RoleKey implements Serializable {
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "municipality_id", referencedColumnName = "ID")
        private final Municipality municipality;

        @Enumerated(EnumType.STRING)
        private final AuthorizationEnum authorizationEnum;

        public RoleKey(Municipality municipality, AuthorizationEnum authorizationEnum) {
            this.municipality = municipality;
            this.authorizationEnum = authorizationEnum;
        }
    }

}
