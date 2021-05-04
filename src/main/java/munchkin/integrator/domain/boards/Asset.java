package munchkin.integrator.domain.boards;

public class Asset {
    private final Board board;
    private final AssetIndex index;

    public Asset(Board board, AssetIndex index) {
        this.board = board;
        this.index = index;
    }

}
