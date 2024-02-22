package it.cs.unicam.app_valorizzazione_territorio.model;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * This class represents relationship between a {@link Municipality} and a {@link AuthorizationEnum}.
 * It is used to represent the role of a {@link User} in a {@link Municipality}.
 */
@JsonView(View.Synthesized.class)
@Entity
@IdClass(Role.RoleKey.class)
@NoArgsConstructor(force = true)
public class Role {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "municipality_id", referencedColumnName = "ID")
    private final Municipality municipality;
    @Id
    @Enumerated(EnumType.STRING)
    private final AuthorizationEnum authorizationEnum;

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
        this.municipality = municipality;
        this.authorizationEnum = authorizationEnum;
    }

    public Municipality municipality() {
        return this.municipality;
    }

    public AuthorizationEnum authorizationEnum() {
        return this.authorizationEnum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Role role = (Role) obj;
        return this.municipality.equals(role.municipality()) && this.authorizationEnum == role.authorizationEnum();
    }

    @Override
    public int hashCode() {
        return 31 * this.municipality.hashCode() + this.authorizationEnum.hashCode();
    }

    protected static class RoleKey implements Serializable {
        private final Municipality municipality;
        private final AuthorizationEnum authorizationEnum;

        public RoleKey(Municipality municipality, AuthorizationEnum authorizationEnum) {
            this.municipality = municipality;
            this.authorizationEnum = authorizationEnum;
        }
    }

}
