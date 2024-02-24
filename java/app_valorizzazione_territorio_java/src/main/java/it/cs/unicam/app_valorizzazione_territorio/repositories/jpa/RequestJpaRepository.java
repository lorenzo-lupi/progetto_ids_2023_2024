package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestJpaRepository extends JpaRepository<Request<?>, Long> {

    @Query("select r from Request r where r.contest = ?1")
    List<Request<ContestContent>> findAllByContest(Contest contest);

    @Query("select r from Request r where r.sender = ?1")
    List<Request<?>> findAllBySender(User sender);
}
