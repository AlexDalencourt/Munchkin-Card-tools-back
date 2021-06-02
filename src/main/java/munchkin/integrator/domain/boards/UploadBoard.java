package munchkin.integrator.domain.boards;

import munchkin.integrator.domain.card.Card;

import java.util.List;

public interface UploadBoard {

    boolean uploadNewBoard(Board boardToSave);

    List<Board> getAllBoards(boolean resizeImages);

    List<Card> cropBoard(long boardId, boolean persistCropsCards);
}
