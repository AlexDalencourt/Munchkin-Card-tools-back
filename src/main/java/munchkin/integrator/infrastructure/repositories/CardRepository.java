package munchkin.integrator.infrastructure.repositories;

import munchkin.integrator.infrastructure.repositories.entities.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
}
