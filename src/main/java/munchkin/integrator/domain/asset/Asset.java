package munchkin.integrator.domain.asset;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class Asset {
    private final Image image;
    private final AssetIndex index;

    public Asset(Image image, AssetIndex index) {
        this.image = requireNonNull(image);
        this.index = requireNonNull(index);
        ;
    }

    public Asset(Asset cardAsset) {
        requireNonNull(cardAsset);
        this.image = cardAsset.image;
        this.index = new AssetIndex(cardAsset.index);
    }

    public Image image() {
        return new Image(image);
    }

    public AssetIndex index() {
        return new AssetIndex(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return image.equals(asset.image) && index.equals(asset.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, index);
    }
}
