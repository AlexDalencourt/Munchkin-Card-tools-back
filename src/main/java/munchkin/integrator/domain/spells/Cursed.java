package munchkin.integrator.domain.spells;

import munchkin.integrator.domain.Ability;
import munchkin.integrator.domain.boards.Asset;

import static munchkin.integrator.domain.SubType.CURSE;
import static munchkin.integrator.domain.Type.DUNGEON;

public class Cursed extends Spell {
    public Cursed(Asset asset, Ability spellEffect, String name) {
        super(DUNGEON, CURSE, asset, spellEffect, name);
    }
}
