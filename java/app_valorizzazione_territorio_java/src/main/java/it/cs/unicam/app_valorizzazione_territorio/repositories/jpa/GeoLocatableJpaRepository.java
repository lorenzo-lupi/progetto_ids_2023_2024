package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoLocatableJpaRepository extends JpaRepository<GeoLocatable, Long> {
}
