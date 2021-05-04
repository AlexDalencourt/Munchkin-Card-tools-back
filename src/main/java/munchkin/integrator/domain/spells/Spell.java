package munchkin.integrator.domain.spells;

import munchkin.integrator.domain.Ability;
import munchkin.integrator.domain.Card;
import munchkin.integrator.domain.SubType;
import munchkin.integrator.domain.Type;
import munchkin.integrator.domain.boards.Asset;

import static munchkin.integrator.domain.SubType.SPELL;

public class Spell extends Card {

    private final Ability spellEffect;

    public Spell(Type type, Asset asset, Ability spellEffect, String name) {
        super(type, SPELL, asset, name);
        this.spellEffect = spellEffect;
    }

    public Spell(Type type, SubType subType, Asset asset, Ability spellEffect, String name) {
        super(type, subType, asset, name);
        this.spellEffect = spellEffect;
    }
}
