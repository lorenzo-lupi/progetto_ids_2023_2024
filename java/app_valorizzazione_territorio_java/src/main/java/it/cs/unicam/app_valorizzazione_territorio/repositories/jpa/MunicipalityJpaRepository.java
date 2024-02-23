package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface MunicipalityJpaRepository extends JpaRepository<Municipality, Long> {
    Stream<Municipality> findByDescriptionContaining(String description);
}
