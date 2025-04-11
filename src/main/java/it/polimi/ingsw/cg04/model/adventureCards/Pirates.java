package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.PiratesState;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.ArrayList;
import java.util.List;

public class Pirates extends AdventureCard {
    @Expose
    private int firePower;
    @Expose
    private int reward;
    @Expose
    private List<Direction> directions;
    @Expose
    private List<Attack> shots;

    public Pirates() {
        super();
        directions = new ArrayList<>();
        shots = new ArrayList<>();
    }

    public Integer getFirePower() {
        return firePower;
    }

    // todo: figure out who needs to use this setter
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    public Integer getEarnedCredits() {
        return reward;
    }

    // todo: figure out who needs to use this setter
    public void setShots(List<Attack> shots) {
        this.shots = new ArrayList<>(shots);
    }

    public Direction getDirection(int i) {
        return directions.get(i);
    }

    public Attack getAttack(int i) {
        return shots.get(i);
    }

    public List<Attack> getAttacks() {
        return shots;
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new PiratesState(game);
    }
}
