package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentJpaRepository extends JpaRepository<Content<?>, Long> {

    <T extends ContentHost<T> & Visualizable> List<Content<T>> findByType(String type);

    @Query("select c from Content c where c.type = 'PointOfInterestContent'")
    List<PointOfInterestContent> findAllPointOfInterestContents();

    @Query("select c from Content c where c.type = 'ContestContent'")
    List<ContestContent> findAllContestContents();

    @Query("select c from Content c where c.type = 'PointOfInterestContent' AND c.ID = ?1")
    Optional<PointOfInterestContent> findPointOfInterestContentById(long id);

    @Query("select c from Content c where c.type = 'ContestContent' AND c.ID = ?1")
    Optional<ContestContent> findContestContentById(long id);
}
