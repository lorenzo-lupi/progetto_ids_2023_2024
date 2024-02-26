package it.cs.unicam.app_valorizzazione_territorio.repositories.jpa;

import it.cs.unicam.app_valorizzazione_territorio.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageJpaRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByID(long id);
}
