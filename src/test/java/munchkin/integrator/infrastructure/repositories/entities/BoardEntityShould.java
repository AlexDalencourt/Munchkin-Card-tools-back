package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.domain.boards.Board;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardEntityShould {

    @Test
    public void mapped_entity_to_board() {
        BoardEntity entity = new BoardEntity(1L, "test".getBytes(), 2, 3);

        Board outputMapped = entity.toBoard();

        assertThat(outputMapped.boardId()).isEqualTo(entity.getChecksum());
        assertThat(outputMapped.boardImage().image()).isEqualTo(entity.getImage());
        assertThat(outputMapped.sizing().numberOfColumns()).isEqualTo(entity.getColumns());
        assertThat(outputMapped.sizing().numberOfLines()).isEqualTo(entity.getLines());
    }

}