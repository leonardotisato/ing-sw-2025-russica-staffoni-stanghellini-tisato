package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Shot;

import java.util.ArrayList;
import java.util.List;

public class Pirates extends AdventureCard {
    private final int firePower;
    private final int reward;
    private final List<Direction> directions;
    private final List<Shot> shots;

    public Pirates(int cardLevel, int daysLost, int firePower, int reward) {
        super(cardLevel, daysLost);
        this.firePower = firePower;
        this.reward = reward;
        directions = new ArrayList<Direction>();
        shots = new ArrayList<Shot>();
    }

    public void addCannonAttack(Direction direction, Shot shot) {
        directions.add(direction);
        shots.add(shot);
    }

    public int getFirePower() { return firePower; }

    public int getReward() { return reward; }

    public List<Direction> getDirections() { return directions; }

    public List<Shot> getShots() { return shots; }

    public Direction getDirection(int i) { return directions.get(i); }

    public Shot getShot(int i) { return shots.get(i); }

    public void solveEffect() {    }
}
