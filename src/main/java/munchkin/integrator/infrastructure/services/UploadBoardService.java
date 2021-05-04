package munchkin.integrator.infrastructure.services;

import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.infrastructure.repositories.BoardRepository;
import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;

import static java.util.Objects.requireNonNull;

public class UploadBoardService implements UploadBoard {

    private BoardRepository boardRepository;

    public UploadBoardService(BoardRepository boardRepository) {
        this.boardRepository = requireNonNull(boardRepository);
    }

    @Override
    public boolean uploadNewBoard(Board boardToSave) {
        return boardRepository.save(new BoardEntity(boardToSave)).getChecksum() != null;
    }

}
