package munchkin.integrator.domain.asset;

public final class Asset {
    private final Image image;
    private final AssetIndex index;

    public Asset(Image image, AssetIndex index) {
        this.image = image;
        this.index = index;
    }

    public Asset(Asset cardAsset) {
        this.image = cardAsset.image;
        this.index = new AssetIndex(cardAsset.index);
    }

    public Image image() {
        return new Image(image);
    }

    public AssetIndex index() {
        return new AssetIndex(index);
    }
}
