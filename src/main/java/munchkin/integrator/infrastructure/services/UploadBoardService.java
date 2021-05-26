package munchkin.integrator.infrastructure.services;

import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.infrastructure.repositories.BoardRepository;
import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;

import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class UploadBoardService implements UploadBoard {

    private final BoardRepository boardRepository;

    private final ImageService imageService;

    public UploadBoardService(BoardRepository boardRepository, ImageService imageService) {
        this.boardRepository = requireNonNull(boardRepository);
        this.imageService = requireNonNull(imageService);
    }

    @Override
    public boolean uploadNewBoard(Board boardToSave) {
        return boardRepository.save(new BoardEntity(boardToSave)).getChecksum() != null;
    }

    @Override
    public List<Board> getAllBoards(boolean resizeImages) {
        List<Board> allBoards = boardRepository.findAll().stream().map(BoardEntity::toBoard).collect(Collectors.toList());
        if (resizeImages) {
            return imageService.reziseBoards(allBoards, 10);
        }
        return allBoards;
    }

    @Override
    public Board cropBoard(long boardId) {
        Optional<BoardEntity> boardResult = boardRepository.findById(boardId);
        BoardEntity board = boardResult.orElseThrow(() -> new MissingResourceException("Board not exist", "Board", ((Long) boardId).toString()));
        for (int column = 0; column < board.getColumns(); column++) {
            for (int line = 0; line < board.getLines(); line++) {
                imageService.cropImage(column, line, board.getImage());
            }
        }
        return null;
    }
}
