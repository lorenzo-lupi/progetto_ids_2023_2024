package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Role.RoleKey> {

    Role findByMunicipalityAndAuthorization(Municipality municipality, AuthorizationEnum authorization);

    default void createRolesForMunicipality(Municipality municipality) {
        Arrays.stream(AuthorizationEnum.values()).forEach(auth -> save(new Role(municipality, auth)));
    }
}
