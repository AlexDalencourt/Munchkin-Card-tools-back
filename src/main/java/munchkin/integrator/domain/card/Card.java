package munchkin.integrator.domain.card;

import munchkin.integrator.domain.asset.Asset;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class Card {

    private final Asset cardAsset;

    public Card(Asset cardAsset) {
        this.cardAsset = cardAsset;
    }

    public Card(Card card) {
        requireNonNull(card);
        this.cardAsset = card.cardAsset;
    }

    public Asset cardAsset() {
        return new Asset(cardAsset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardAsset.equals(card.cardAsset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardAsset);
    }
}
