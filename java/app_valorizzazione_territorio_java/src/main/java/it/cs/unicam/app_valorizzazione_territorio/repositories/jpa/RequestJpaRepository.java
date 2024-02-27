package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.ContestRequest;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.MunicipalityRequest;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestJpaRepository extends JpaRepository<Request<?>, Long> {

    @Query("select r from Request r where r.requestType = 'Municipality'")
    List<MunicipalityRequest<?>> findAllMunicipalityRequests();

    @Query("select r from Request r where r.requestType = 'Contest'")
    List<ContestRequest> findAllContestRequests();

    @Query("select r from Request r where r.requestType = 'Municipality' AND r.ID = ?1")
    Optional<MunicipalityRequest<?>> findMunicipalityRequestById(long id);

    @Query("select r from Request r where r.requestType = 'Contest' AND r.ID = ?1")
    Optional<ContestRequest> findContestRequestById(long id);

    @Query("select r from Request r where r.contest = ?1")
    List<Request<ContestContent>> findAllByContest(Contest contest);

    @Query("select r from Request r where r.sender = ?1")
    List<Request<?>> findAllBySender(User sender);

}
