package munchkin.integrator.domain.boards;

import java.util.Arrays;

public class Board {
    private final Sizing sizing;
    private final byte[] boardImage;

    public Board(String boardName, Sizing sizing, byte[] boardImage) {
        this.sizing = sizing;
        this.boardImage = boardImage;
    }

    public byte[] boardImage() {
        return Arrays.copyOf(boardImage, boardImage.length);
    }

    public Sizing sizing() {
        return new Sizing(sizing);
    }
}
