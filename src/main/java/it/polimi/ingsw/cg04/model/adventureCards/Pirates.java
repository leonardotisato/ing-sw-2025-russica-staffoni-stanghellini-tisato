package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.PiratesState;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public class Pirates extends AdventureCard {
    @Expose
    private int firePower;
    @Expose
    private int reward;
    @Expose
    private List<Direction> directions;
    @Expose
    private List<Attack> attacks;

    public Pirates() {
        super();
        directions = new ArrayList<>();
        attacks = new ArrayList<>();
    }

    /**
     *
     * @return the firepower value as an Integer
     */
    @Override
    public Integer getFirePower() {
        return firePower;
    }

    // todo: figure out who needs to use this setter
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    /**
     *
     * @return the earned credits as an Integer
     */
    @Override
    public Integer getEarnedCredits() {
        return reward;
    }

    // todo: figure out who needs to use this setter
    public void setAttacks(List<Attack> attacks) {
        this.attacks = new ArrayList<>(attacks);
    }

    /**
     * Retrieves the direction from the list of directions at the specified index.
     *
     * @param i the index of the direction to retrieve
     * @return the direction corresponding to the provided index
     */
    @Override
    public Direction getDirection(int i) {
        return directions.get(i);
    }

    /**
     * Retrieves the attack from the list of attacks at the specified index.
     *
     * @param i the index of the attack to retrieve
     * @return the attack corresponding to the provided index
     */
    @Override
    public Attack getAttack(int i) {
        return attacks.get(i);
    }

    /**
     * Retrieves the list of attacks.
     *
     * @return a List of Attack instances representing the type of attacks available.
     */
    @Override
    public List<Attack> getAttacks() {
        return attacks;
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new PiratesState(game);
    }

    /**
     * Draws the content of the Pirates adventure card into the provided StringBuilder.
     *
     * @param sb the StringBuilder instance where the formatted card content will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        List<String> shots = renderShots();
        for (String shot : shots) {
            sb.append("│ ").append(centerText(shot, WIDTH - 4)).append(" │").append("\n");
        }
        sb.append("│ ").append(centerText("Required Fire power: " + this.firePower, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Earned credits: " + this.getEarnedCredits(), WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }

    /**
     * Generates a list of formatted strings representing shots.
     *
     * @return a list of strings where each string represents a formatted shot with direction and attack information
     */
    public List<String> renderShots() {
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
