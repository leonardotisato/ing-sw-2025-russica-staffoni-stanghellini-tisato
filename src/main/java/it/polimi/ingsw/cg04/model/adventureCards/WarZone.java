package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.WarZoneState;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.ArrayList;
import java.util.List;

public class WarZone extends AdventureCard {

    @Expose private List<String> checkParameter;
    @Expose private List<String> penaltyType;
    @Expose private List<Attack> shots;
    @Expose private List<Direction> directions;
    @Expose private int lostGoods;
    @Expose private int lostMembers;

    public WarZone() {
        super();
        this.checkParameter = new ArrayList<String>();
        this.penaltyType = new ArrayList<String>();
        this.shots = new ArrayList<Attack>();
        this.directions = new ArrayList<Direction>();
    }

    public List<String> getParameterCheck() {
        return checkParameter;
    }

    public void setParameterCheck(List<String> parameterCheck) {
        this.checkParameter = parameterCheck;
    }

    public List<String> getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(List<String> penaltyType) {
        this.penaltyType = penaltyType;
    }

    public List<Attack> getShots() {
        return shots;
    }

    public void setShots(List<Attack> shots) {
        this.shots = shots;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public int getLostGoods() {
        return lostGoods;
    }

    public void setLostGoods(int lostGoods) {
        this.lostGoods = lostGoods;
    }

    public int getLostMembers() {
        return lostMembers;
    }

    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    public AdventureCardState createState(Game game) {
        return new WarZoneState(game);
    }
}
