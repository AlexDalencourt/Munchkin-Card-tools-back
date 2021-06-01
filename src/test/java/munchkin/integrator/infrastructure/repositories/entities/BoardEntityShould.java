package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.domain.asset.Asset;
import munchkin.integrator.domain.asset.AssetIndex;
import munchkin.integrator.domain.asset.Image;
import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.Sizing;
import munchkin.integrator.domain.card.Card;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardEntityShould {

    @Test
    public void mapped_entity_to_board() {
        BoardEntity entity = new BoardEntity(1L, "test".getBytes(), 2, 3, null);

        Board outputMapped = entity.toBoard();

        assertThat(outputMapped.boardId()).isEqualTo(entity.getChecksum());
        assertThat(outputMapped.boardImage().image()).isEqualTo(entity.getImage());
        assertThat(outputMapped.sizing().numberOfColumns()).isEqualTo(entity.getColumns());
        assertThat(outputMapped.sizing().numberOfLines()).isEqualTo(entity.getLines());
    }

    @Test
    public void mapped_board_to_entity() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(new Asset(new Image("IMAGE1".getBytes()), new AssetIndex(0, 1))));
        cards.add(new Card(new Asset(new Image("IMAGE2".getBytes()), new AssetIndex(1, 1))));
        cards.add(new Card(new Asset(new Image("IMAGE3".getBytes()), new AssetIndex(3, 2))));
        cards.add(new Card(new Asset(new Image("IMAGE4".getBytes()), new AssetIndex(0, 4))));
        Board board = new Board(1L, new Sizing(4, 9), new Image("IMAGE".getBytes()), cards);

        BoardEntity mappedEntity = new BoardEntity(board);

        assertThat(mappedEntity.getChecksum()).isEqualTo(board.boardId());
        assertThat(mappedEntity.getColumns()).isEqualTo(board.sizing().numberOfColumns());
        assertThat(mappedEntity.getLines()).isEqualTo(board.sizing().numberOfLines());
        assertThat(mappedEntity.getImage()).isEqualTo(board.boardImage().image());
        assertThat(mappedEntity.getCards()).hasSameSizeAs(board.cards());
    }

}