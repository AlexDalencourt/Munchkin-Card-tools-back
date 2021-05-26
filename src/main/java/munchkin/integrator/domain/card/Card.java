package munchkin.integrator.domain.card;

import munchkin.integrator.domain.asset.Asset;

public abstract class Card {

    private final Asset cardAsset;

    public Card(Asset cardAsset) {
        this.cardAsset = cardAsset;
    }

    public Asset cardAsset() {
        return new Asset(cardAsset);
    }
}
