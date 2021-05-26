package munchkin.integrator.domain.asset;

import java.util.Objects;

public final class AssetIndex {
    private final int column;
    private final int line;

    public AssetIndex(int column, int line) {
        this.column = column;
        this.line = line;
    }

    public AssetIndex(AssetIndex index) {
        this.column = index.column;
        this.line = index.line;
    }

    public int column() {
        return this.column;
    }

    public int line() {
        return this.line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetIndex that = (AssetIndex) o;
        return column == that.column && line == that.line;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, line);
    }
}
