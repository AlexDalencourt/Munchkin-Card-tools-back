package munchkin.integrator.domain.boards;

import java.util.List;

public interface UploadBoard {

    boolean uploadNewBoard(Board boardToSave);

    List<Board> getAllBoards(boolean resizeImages);

    Board cropBoard(long boardId, boolean persistCropsCards);
}
