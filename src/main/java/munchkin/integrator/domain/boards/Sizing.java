package munchkin.integrator.domain.boards;

public class Sizing {
    private int numberOfColumns;
    private int numberOfLines;

    public Sizing(int numberOfColumns, int numberOfLines) {
        this.numberOfColumns = numberOfColumns;
        this.numberOfLines = numberOfLines;
    }

    public Sizing(Sizing sizing) {
        numberOfColumns = sizing.numberOfColumns;
        numberOfLines = sizing.numberOfLines;
    }

    public int numberOfColumns() {
        return numberOfColumns;
    }

    public int numberOfLines() {
        return numberOfLines;
    }
}
