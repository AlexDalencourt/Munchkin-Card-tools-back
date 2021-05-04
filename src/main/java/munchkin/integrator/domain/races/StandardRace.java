package munchkin.integrator.domain.races;

import munchkin.integrator.domain.Card;
import munchkin.integrator.domain.boards.Asset;

import static munchkin.integrator.domain.SubType.RACE;
import static munchkin.integrator.domain.Type.DUNGEON;

public class StandardRace extends Card {
    private String name;
    private String description;

    public StandardRace(Asset asset, String name, String description) {
        super(DUNGEON, RACE, asset, name);
        this.name = name;
        this.description = description;
    }
}
