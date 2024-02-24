package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestJpaRepository extends JpaRepository<Contest, Long>{

    List<Contest> findByValidTrue();
    default Contest findByBaseContestIdAndValidTrue(long id){
        return this.findByValidTrue().stream()
                .filter(c -> c.getBaseContestID() == id)
                .findFirst()
                .orElse(null);
    }

}
