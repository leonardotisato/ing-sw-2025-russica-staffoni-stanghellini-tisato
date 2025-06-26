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

    @Expose
    private List<String> checkParameter;
    @Expose
    private List<String> penaltyType;
    @Expose
    private List<Attack> attacks;
    @Expose
    private List<Direction> directions;
    @Expose
    private int lostGoods;
    @Expose
    private int lostMembers;

    public WarZone() {
        super();
        this.checkParameter = new ArrayList<>();
        this.penaltyType = new ArrayList<>();
        this.attacks = new ArrayList<>();
        this.directions = new ArrayList<>();
    }

    /**
     *
     * @return a List of Strings representing the parameter checks.
     */
    @Override
    public List<String> getParameterCheck() {
        return checkParameter;
    }

    // todo: no usages
    public void setParameterCheck(List<String> parameterCheck) {
        this.checkParameter = parameterCheck;
    }

    /**
     *
     * @return a List of Strings representing the penalty types.
     */
    @Override
    public List<String> getPenaltyType() {
        return penaltyType;
    }

    // todo: no usages
    public void setPenaltyType(List<String> penaltyType) {
        this.penaltyType = penaltyType;
    }

    /**
     *
     * @return a List of Attack instances representing the types of attacks.
     */
    @Override
    public List<Attack> getAttacks() {
        return attacks;
    }

    /**
     *
     * @param i the index of the attack to retrieve
     * @return the attack corresponding to the provided index
     */
    @Override
    public Attack getAttack(int i) {
        return attacks.get(i);
    }

    /**
     *
     * @param i the index of the direction to retrieve
     * @return the direction corresponding to the provided index
     */
    @Override
    public Direction getDirection(int i) {
        return directions.get(i);
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }

    /**
     *
     * @return a List of Direction instances representing the directions from where the attacks will come from.
     */
    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    /**
     *
     * @return the quantity of lost goods as an integer
     */
    @Override
    public int getLostGoods() {
        return lostGoods;
    }

    public void setLostGoods(int lostGoods) {
        this.lostGoods = lostGoods;
    }

    /**
     *
     * @return the number of lost members as an Integer
     */
    public Integer getLostMembers() {
        return lostMembers;
    }

    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    public AdventureCardState createState(Game game) {
        return new WarZoneState(game);
    }

    /**
     * Draws the formatted content of the WarZone adventure card into the provided StringBuilder.
     *
     * @param sb the StringBuilder instance where the card's content will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        List<String> toShow = new ArrayList<>();
        for (int i = 0; i < checkParameter.size(); i++) {
            toShow = renderPenalties(i);
            for (String s : toShow) {
                sb.append("│ ").append(centerText(s, WIDTH - 4)).append(" │").append("\n");
            }
        }
    }

    /**
     * Generates a formatted list of strings representing penalties based on the specified parameter index.
     *
     * @param i the index of the parameter and penalty type to use for rendering penalties
     * @return a list of strings where each string represents a formatted penalty or additional shot information
     */
    List<String> renderPenalties(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("↓ ").append(checkParameter.get(i)).append(": ").append(penaltyType.get(i));
        if (penaltyType.get(i).equals("HANDLESHOTS")) {
            sb.append("\n");
            List<String> shots = renderShots();
            for (String s : shots) {
                sb.append(s).append("\n");
            }
        }
        return toLines(sb.toString());
    }

    /**
     * Generates a list of formatted strings representing the shots,
     * including direction symbols and corresponding attack details.
     *
     * @return a list of strings where each string contains the attack type
     *         flanked by directional symbols representing the attack's origin and target direction
     */
    List<String> renderShots() {
        Map<Direction, String> directionToArrow = new HashMap<>();
        directionToArrow.put(Direction.UP, "↓");
        directionToArrow.put(Direction.RIGHT, "←");
        directionToArrow.put(Direction.LEFT, "→");
        directionToArrow.put(Direction.DOWN, "↑");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getAttacks().size(); i++) {
            sb.append(directionToArrow.get(directions.get(i))).append(" ").append(attacks.get(i).toString()).append(" ").append(directionToArrow.get(directions.get(i))).append("\n");
        }
        return toLines(sb.toString());
    }
}

