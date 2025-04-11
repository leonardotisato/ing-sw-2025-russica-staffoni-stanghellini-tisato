package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.List;
import java.util.Map;

public abstract class AdventureCard {

    @Expose
    private String type;
    @Expose
    private int cardLevel;
    @Expose
    private int daysLost;
    // @Expose private boolean solved = false;

    public AdventureCard() {
    }

    //public abstract void solveEffect(Game game);

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public void setDaysLost(int daysLost) {
        this.daysLost = daysLost;
    }

    // public boolean getSolved() { return solved; }

    // public void setSolved(boolean solved) { this.solved = solved; }

    public Integer getFirePower() {
        return null;
    }

    // public int getLostGoods() { return 0; }

    public Integer getLostMembers() {
        return null;
    }

    public List<Boolean> getIsOccupied() {
        return null;
    }


    public Integer getEarnedCredits() {
        return null;
    }

    public int getMembersNeeded() {
        return 0;
    }

    public void setMembersNeeded(int i) {
        return;
    }

    public abstract AdventureCardState createState(Game game);

    public int getObtainedResourcesByType(BoxType type) {
        return 0;
    }

    public List<Map<BoxType, Integer>> getPlanetReward() {
        return null;
    }

    public Map<BoxType, Integer> getObtainedResources() {
        return null;
    }

    public Direction getDirection(int i) {
        return null;
    }

    public List<Attack> getAttacks() {
        return null;
    }

    public Attack getAttack(int i) {
        return null;
    }

    public int getLostGoods() { return 0; }

    public Map<BoxType, Integer> getBoxes() { return null; }

    // Duplicate method...
//    public int getLostMembers() { return 0; }

    public AdventureCardState createGameState(Game game) {
        return null;
    }
}
