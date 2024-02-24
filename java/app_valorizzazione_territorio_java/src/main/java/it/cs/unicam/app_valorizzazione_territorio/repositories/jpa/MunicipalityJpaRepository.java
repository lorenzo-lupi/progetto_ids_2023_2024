package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface MunicipalityJpaRepository extends JpaRepository<Municipality, Long> {
    Stream<Municipality> findByDescriptionContaining(String description);
}
