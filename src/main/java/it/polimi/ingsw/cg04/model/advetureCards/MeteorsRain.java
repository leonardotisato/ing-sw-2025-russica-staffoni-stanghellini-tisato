package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Meteor;

import java.util.ArrayList;
import java.util.List;

public class MeteorsRain extends AdventureCard {
    private List<Direction> direction;
    private List<Meteor> shot;

    public MeteorsRain() {
        direction = new ArrayList<Direction>();
        shot = new ArrayList<Meteor>();
    }

    public void solveEffect() {}
}
