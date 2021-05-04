package munchkin.integrator.infrastructure.repositories;

import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<BoardEntity, Long> {
}
