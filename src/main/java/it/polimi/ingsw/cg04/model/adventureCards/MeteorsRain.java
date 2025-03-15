package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Meteor;

import java.util.ArrayList;
import java.util.List;

public class MeteorsRain extends AdventureCard {
    @Expose
    private List<Direction> directions;
    @Expose
    private List<Meteor> meteors;

    public MeteorsRain() {
        super();
        directions = new ArrayList<Direction>();
        meteors = new ArrayList<Meteor>();
    }

    public void addMeteorAttack(Direction direction, Meteor meteor) {
        directions.add(direction);
        meteors.add(meteor);
    }

    public List<Meteor> getMeteors() { return meteors; }
    public void setMeteors(List<Meteor> meteors) {
        this.meteors = meteors;
    }

    public List<Direction> getDirections() { return directions; }
    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

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
