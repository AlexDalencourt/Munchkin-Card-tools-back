package munchkin.integrator.infrastructure.rest.responses.boards;

import munchkin.integrator.domain.boards.Board;

import java.io.Serial;
import java.io.Serializable;

public class BoardResponseWithResource implements Serializable {

    @Serial
    private static final long serialVersionUID = -9212150056597574063L;

    private Long boardId;
    private SizingResponse sizing;
    private byte[] image;

    public BoardResponseWithResource(Board baseBoard) {
        this.boardId = baseBoard.boardId();
        this.sizing = new SizingResponse(baseBoard.sizing());
        this.image = baseBoard.boardImage().image();
    }

    public Long getBoardId() {
        return boardId;
    }

    public SizingResponse getSizing() {
        return sizing;
    }

    public byte[] getImage() {
        return image;
    }

    @SuppressWarnings("unused")
    public BoardResponseWithResource() {
    }

    @SuppressWarnings("unused")
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    @SuppressWarnings("unused")
    public void setSizing(SizingResponse sizing) {
        this.sizing = sizing;
    }

    @SuppressWarnings("unused")
    public void setImage(byte[] image) {
        this.image = image;
    }
}
