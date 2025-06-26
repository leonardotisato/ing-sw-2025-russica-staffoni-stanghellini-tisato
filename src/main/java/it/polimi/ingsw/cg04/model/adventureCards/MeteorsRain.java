package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.MeteorsRainState;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Attack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public class MeteorsRain extends AdventureCard {
    @Expose
    private List<Direction> directions;
    @Expose
    private List<Attack> attacks;

    public MeteorsRain() {
        super();
        directions = new ArrayList<Direction>();
        attacks = new ArrayList<Attack>();
    }

    public void addMeteorAttack(Direction direction, Attack meteor) {
        directions.add(direction);
        attacks.add(meteor);
    }

    /**
     *
     * @return the list of attacks
     */
    public List<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }

    /**
     *
     * @return a list of Direction representing the directions of the attacks
     */
    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    /**
     * Retrieves the direction at the specified index in the list of directions.
     *
     * @param i the index of the direction to retrieve
     * @return the Direction at the specified index
     */
    public Direction getDirection(int i) {
        return directions.get(i);
    }

    /**
     * Retrieves the attack at the specified index in the list of attacks.
     *
     * @param i the index of the attack to retrieve
     * @return the Attack at the specified index
     */
    public Attack getAttack(int i) {
        return attacks.get(i);
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new MeteorsRainState(game);
    }

    /**
     * Draws the content of the MeteorsRain adventure card.
     *
     * @param sb the StringBuilder to which the content of the card will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        List<String> meteors = renderMeteors();
        for (String meteor : meteors) {
            sb.append("│ ").append(centerText(meteor, WIDTH - 4)).append(" │").append("\n");
        }
    }

    /**
     * Renders the meteor descriptions by creating textual representations of all meteor attacks.
     *
     * @return a list of strings, where each string corresponds to a line of meteor attack representation
     */
    public List<String> renderMeteors() {
        Map<Direction, String> directionToArrow = new HashMap<>();
        directionToArrow.put(Direction.UP, "↓");
        directionToArrow.put(Direction.RIGHT, "←");
        directionToArrow.put(Direction.LEFT, "→");
        directionToArrow.put(Direction.DOWN, "↑");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getDirections().size(); i++) {
            sb.append(directionToArrow.get(directions.get(i)) + " " + attacks.get(i).toString() + " " + directionToArrow.get(directions.get(i))).append("\n");
        }
        return toLines(sb.toString());
    }
}
