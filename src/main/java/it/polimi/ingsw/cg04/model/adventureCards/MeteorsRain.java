package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AbandonedShipState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
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
        int dice = 0;
        for (int i = 0; i < meteors.size(); i++) {
            dice = game.rollDices();
            if(dice < 4 || ((this.getDirection(i) == Direction.UP || this.getDirection(i) == Direction.DOWN) && dice > 10) ||
                    ((this.getDirection(i) == Direction.LEFT || this.getDirection(i) == Direction.RIGHT) && dice > 9)){
                continue;
            }
            else {
                if(this.getDirection(i) == Direction.UP || this.getDirection(i) == Direction.DOWN){
                    dice = dice - 4;
                }
                else if(this.getDirection(i) == Direction.LEFT || this.getDirection(i) == Direction.RIGHT){
                    dice = dice - 5;
                }
                for (Player player : game.getPlayers()) {
                    player.getShip().handleMeteor(this.getDirection(i), this.getMeteor(i), dice);
                }
            }
        }
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new MeteorsRainState(game.getSortedPlayers(), this);
    }
}
