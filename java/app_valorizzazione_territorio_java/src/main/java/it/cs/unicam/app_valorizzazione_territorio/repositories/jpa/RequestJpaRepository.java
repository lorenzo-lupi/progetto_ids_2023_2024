package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.requests.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestJpaRepository extends JpaRepository<Request<?>, Long> {
}
