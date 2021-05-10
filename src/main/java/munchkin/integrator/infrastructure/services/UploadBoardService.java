package munchkin.integrator.infrastructure.services;

import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.infrastructure.repositories.BoardRepository;
import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Board> getAllBoards() {
        return boardRepository.findAll().stream().map(BoardEntity::toBoard).collect(Collectors.toList());
    }
}
