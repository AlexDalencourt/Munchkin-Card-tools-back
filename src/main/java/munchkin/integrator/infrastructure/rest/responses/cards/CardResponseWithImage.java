package munchkin.integrator.infrastructure.rest.responses.cards;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public final class CardResponseWithImage implements Serializable {

    @Serial
    private static final long serialVersionUID = -2570879238599608778L;

    private final byte[] card;

    public CardResponseWithImage() {
        card = null;
    }

    public CardResponseWithImage(byte[] card) {
        this.card = requireNonNull(card);
    }

    public byte[] getCard() {
        return card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardResponseWithImage that = (CardResponseWithImage) o;
        return Arrays.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(card);
    }
}
