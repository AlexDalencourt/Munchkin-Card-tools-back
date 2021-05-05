package munchkin.integrator.domain.boards;

import java.util.Arrays;

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

    public byte[] boardImage() {
        return Arrays.copyOf(boardImage, boardImage.length);
    }

    public Sizing sizing() {
        return new Sizing(sizing);
    }

    public Long boardId() {
        return boardId;
    }
}
