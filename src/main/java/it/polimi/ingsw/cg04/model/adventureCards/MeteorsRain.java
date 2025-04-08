package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.MeteorsRainState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Attack;

import java.util.ArrayList;
import java.util.List;

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
}
