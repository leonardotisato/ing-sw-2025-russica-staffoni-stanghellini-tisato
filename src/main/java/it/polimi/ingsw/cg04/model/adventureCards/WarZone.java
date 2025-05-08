package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.WarZoneState;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public class WarZone extends AdventureCard {

    @Expose private List<String> checkParameter;
    @Expose private List<String> penaltyType;
    @Expose private List<Attack> attacks;
    @Expose private List<Direction> directions;
    @Expose private int lostGoods;
    @Expose private int lostMembers;

    public WarZone() {
        super();
        this.checkParameter = new ArrayList<String>();
        this.penaltyType = new ArrayList<String>();
        this.attacks = new ArrayList<Attack>();
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

    public List<Attack> getAttacks() {
        return attacks;
    }

    public Attack getAttack(int i) { return attacks.get(i); }

    public Direction getDirection(int i) { return directions.get(i); }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
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

    public Integer getLostMembers() {
        return lostMembers;
    }

    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    public AdventureCardState createState(Game game) {
        return new WarZoneState(game);
    }

    @Override
    void drawContent(StringBuilder sb) {
        List<String> toShow = new ArrayList<>();
        for(int i = 0; i < checkParameter.size(); i++){
            toShow = renderPenalties(i);
            for(String s : toShow){
                sb.append("│ ").append(centerText(s, WIDTH - 4)).append(" │").append("\n");
            }
        }
    }

    List<String> renderPenalties(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("↓ " + checkParameter.get(i) + ": " + penaltyType.get(i));
        if (penaltyType.get(i).equals("HANDLESHOTS")){
            sb.append("\n");
            List<String> shots = renderShots();
            for (String s : shots) {
                sb.append(s).append("\n");
            }
        }
        return toLines(sb.toString());
    }

    List<String> renderShots(){
        Map<Direction, String> directionToArrow = new HashMap<>();
        directionToArrow.put(Direction.UP, "↓");
        directionToArrow.put(Direction.RIGHT, "←");
        directionToArrow.put(Direction.LEFT, "→");
        directionToArrow.put(Direction.DOWN, "↑");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getAttacks().size(); i++) {
            sb.append(directionToArrow.get(directions.get(i)) + " " + attacks.get(i).toString() + " " + directionToArrow.get(directions.get(i))).append("\n");
        }
        return toLines(sb.toString());
    }
}

