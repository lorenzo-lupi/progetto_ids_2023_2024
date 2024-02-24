package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestJpaRepository extends JpaRepository<Contest, Long>{

    List<Contest> findAllByValidTrue();

    Optional<Contest> findByBaseContestIdAndValidTrue(long id);

}
