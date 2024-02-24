package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface ContentJpaRepository extends JpaRepository<Content<?>, Long> {

    <T extends ContentHost<T> & Visualizable> List<Content<T>> findByType(String type);

    default List<ContestContent> findAllContestContents() {
        return findByType("ContestContent").stream().map(c -> (ContestContent) c).toList();
    }

    default List<PointOfInterestContent> findAllPointOfInterestContents() {
        return findByType("PointOfInterestContent").stream().map(c -> (PointOfInterestContent) c).toList();
    }
}
