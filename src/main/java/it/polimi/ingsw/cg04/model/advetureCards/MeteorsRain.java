package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Meteor;

import java.util.ArrayList;
import java.util.List;

public class MeteorsRain extends AdventureCard {
    private final List<Direction> directions;
    private final List<Meteor> meteors;

    public MeteorsRain(int cardLevel, int daysLost) {
        super(cardLevel, daysLost);
        directions = new ArrayList<Direction>();
        meteors = new ArrayList<Meteor>();
    }

    public void addMeteorAttack(Direction direction, Meteor meteor) {
        directions.add(direction);
        meteors.add(meteor);
    }

    public List<Meteor> getMeteors() { return meteors; }

    public List<Direction> getDirections() { return directions; }

    public Direction getDirection(int i) { return directions.get(i); }

    public Meteor getMeteor(int i) { return meteors.get(i); }

    public void solveEffect(Game game) {

        /*
        *
        * for each meteor in list(meteor)
        *   dice = getPlayer(0).throw_dices
        *   for each player in list(players)
        *       player.handle_meteor
        *
        */

    }
}
