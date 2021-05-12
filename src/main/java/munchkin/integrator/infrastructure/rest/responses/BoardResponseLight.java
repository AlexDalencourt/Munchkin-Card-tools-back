package munchkin.integrator.infrastructure.rest.responses;

import munchkin.integrator.domain.boards.Board;

import java.io.Serial;
import java.io.Serializable;

public class BoardResponseLight implements Serializable {

    @Serial
    private static final long serialVersionUID = 5119330116945571346L;

    private Long boardId;
    private SizingResponse sizing;

    @SuppressWarnings("unused")
    public BoardResponseLight() {
    }

    public BoardResponseLight(Board baseBoard) {
        this.boardId = baseBoard.boardId();
        this.sizing = new SizingResponse(baseBoard.sizing());
    }

    public Long getBoardId() {
        return boardId;
    }

    @SuppressWarnings("unused")
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public SizingResponse getSizing() {
        return sizing;
    }

    @SuppressWarnings("unused")
    public void setSizing(SizingResponse sizing) {
        this.sizing = sizing;
    }
}
