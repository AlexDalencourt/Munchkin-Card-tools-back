package munchkin.integrator.domain;

import munchkin.integrator.domain.boards.Asset;

public abstract class Card {

    protected final Type type;
    protected final SubType subType;
    protected final Asset asset;
    protected final String name;

    protected Card(final Type type, final SubType subType, final Asset asset, final String name) {
        this.type = type;
        this.subType = subType;
        this.asset = asset;
        this.name = name;
    }
}
