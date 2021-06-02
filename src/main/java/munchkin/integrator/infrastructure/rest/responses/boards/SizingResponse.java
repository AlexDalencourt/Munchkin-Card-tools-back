package munchkin.integrator.infrastructure.rest.responses.boards;

import munchkin.integrator.domain.boards.Sizing;

import java.io.Serial;
import java.io.Serializable;

public class SizingResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1412215974308939833L;

    private int numberOfColumns;

    private int numberOfLines;

    public SizingResponse() {
    }

    public SizingResponse(Sizing sizing) {
        this.numberOfColumns = sizing.numberOfColumns();
        this.numberOfLines = sizing.numberOfLines();
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }
}
