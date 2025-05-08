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

    public List<Attack> getAttacks() { return attacks; }
    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }

    public List<Direction> getDirections() { return directions; }
    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public Direction getDirection(int i) { return directions.get(i); }

    public Attack getAttack(int i) { return attacks.get(i); }

    @Override
    public AdventureCardState createState(Game game) {
        return new MeteorsRainState(game);
    }

    @Override
    void drawContent(StringBuilder sb){
        List<String> meteors = renderMeteors();
        for (String meteor : meteors) {
            sb.append("│ ").append(centerText(meteor, WIDTH - 4)).append(" │").append("\n");
        }
    }

    public List<String> renderMeteors(){
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
