package munchkin.integrator.domain.classes;

import munchkin.integrator.domain.Ability;
import munchkin.integrator.domain.Card;
import munchkin.integrator.domain.SubType;
import munchkin.integrator.domain.Type;
import munchkin.integrator.domain.boards.Asset;

import java.util.List;

import static munchkin.integrator.domain.SubType.CLASS;
import static munchkin.integrator.domain.Type.DUNGEON;

public class StandardClass extends Card {

    private Classes className;
    private List<Ability> abilities;

    public StandardClass(Type type, SubType subType, Asset asset, Classes className, List<Ability> abilities) {
        super(DUNGEON, CLASS, asset, className.title());
        this.className = className;
        this.abilities = abilities;
    }
}
