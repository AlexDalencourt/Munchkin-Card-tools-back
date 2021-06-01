package munchkin.integrator.domain.asset;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public final class Image {
    private final byte[] image;

    public Image(final byte[] image) {
        this.image = requireNonNull(image);
    }

    public Image(Image image) {
        this.image = image.image;
    }

    public byte[] image() {
        return Arrays.copyOf(image, image.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image1 = (Image) o;
        return Arrays.equals(image, image1.image);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(image);
    }
}
