package munchkin.integrator.infrastructure.services;

import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.infrastructure.repositories.BoardRepository;
import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;

import java.util.List;
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
}
