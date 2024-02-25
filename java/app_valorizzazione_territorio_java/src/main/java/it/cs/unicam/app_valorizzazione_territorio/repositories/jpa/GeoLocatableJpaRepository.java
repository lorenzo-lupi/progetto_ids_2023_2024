package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPoint;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeoLocatableJpaRepository extends JpaRepository<GeoLocatable, Long> {

    Optional<GeoLocatable> findByID(long ID);
    @Query("select c from GeoLocatable c where c.geoLocatableType IN ('Attraction', 'Activity', 'Event')")
    List<PointOfInterest> findAllPointsOfInterest();

    @Query("select c from GeoLocatable c where c.geoLocatableType = 'CompoundPoint'")
    List<CompoundPoint> findAllCompoundPoints();

    @Query("select c from GeoLocatable c where c.geoLocatableType IN ('Attraction', 'Activity', 'Event')" +
            "AND c.ID = ?1")
    Optional<PointOfInterest> findPointOfInterestById(long id);

    @Query("select c from GeoLocatable c where c.geoLocatableType = 'CompoundPoint'" +
            "AND c.ID = ?1")
    Optional<CompoundPoint> findCompoundPointById(long id);
}
