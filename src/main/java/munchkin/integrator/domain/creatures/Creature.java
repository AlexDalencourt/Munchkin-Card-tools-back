package munchkin.integrator.domain.creatures;

import munchkin.integrator.domain.Ability;
import munchkin.integrator.domain.Card;
import munchkin.integrator.domain.Level;
import munchkin.integrator.domain.SubType;
import munchkin.integrator.domain.boards.Asset;

import static munchkin.integrator.domain.Type.DUNGEON;

public class Creature extends Card {

    private final Level level;
    private final Ability ability;
    private final UnfortunateIncident unfortunateIncident;
    private final int rewards;

    public Creature(Asset asset, Level level, Ability ability, UnfortunateIncident unfortunateIncident, int rewards, String name) {
        super(DUNGEON, SubType.CREATURE, asset, name);
        this.level = level;
        this.ability = ability;
        this.unfortunateIncident = unfortunateIncident;
        this.rewards = rewards;
    }
}
