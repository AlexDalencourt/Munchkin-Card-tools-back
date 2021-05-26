package munchkin.integrator.domain.boards;

import java.util.Objects;

public final class Sizing {
    private final int numberOfColumns;
    private final int numberOfLines;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sizing sizing = (Sizing) o;
        return numberOfColumns == sizing.numberOfColumns && numberOfLines == sizing.numberOfLines;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfColumns, numberOfLines);
    }
}
