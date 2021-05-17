package munchkin.integrator.domain.boards;

import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Board {
    private final Long boardId;
    private final Sizing sizing;
    private final byte[] boardImage;

    public Board(Long boardId, Sizing sizing, byte[] boardImage) {
        this.boardId = boardId;
        this.sizing = requireNonNull(sizing);
        this.boardImage = boardImage;
    }

    public Board(Board baseBoard, byte[] boardImage) {
        this.boardId = baseBoard.boardId();
        this.sizing = baseBoard.sizing();
        this.boardImage = boardImage;
    }

    public byte[] boardImage() {
        return Arrays.copyOf(boardImage, boardImage.length);
    }

    public Sizing sizing() {
        return new Sizing(sizing);
    }

    public Long boardId() {
        return boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(boardId, board.boardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId);
    }
}
