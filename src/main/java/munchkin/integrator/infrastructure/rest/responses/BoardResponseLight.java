package munchkin.integrator.infrastructure.rest.responses;

import munchkin.integrator.domain.boards.Sizing;

public class BoardResponseLight {

    private Long boardId;
    private Sizing sizing;

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Sizing getSizing() {
        return sizing;
    }

    public void setSizing(Sizing sizing) {
        this.sizing = sizing;
    }
}
