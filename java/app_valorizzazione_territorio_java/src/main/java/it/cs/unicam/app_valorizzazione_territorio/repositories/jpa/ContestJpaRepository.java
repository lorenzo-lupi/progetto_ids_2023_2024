package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface ContestJpaRepository extends JpaRepository<Contest, Long>{
}
