package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.OpenSpaceState;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Shot;

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
    private List<Shot> shots;

    public Pirates() {
        super();
        directions = new ArrayList<Direction>();
        shots = new ArrayList<Shot>();
    }

    public void addCannonAttack(Direction direction, Shot shot) {
        directions.add(direction);
        shots.add(shot);
    }

    public int getFirePower() { return firePower; }
    public void setFirePower(int firePower) { this.firePower = firePower; }

    public int getReward() { return reward; }
    public void setReward(int reward) { this.reward = reward; }

    public List<Direction> getDirections() { return directions; }
    public void setDirections(List<Direction> directions) {
        this.directions = new ArrayList<>(directions);
    }

    public List<Shot> getShots() { return shots; }
    public void setShots(List<Shot> shots) {
        this.shots = new ArrayList<>(shots);
    }

    public Direction getDirection(int i) { return directions.get(i); }

    public Shot getShot(int i) { return shots.get(i); }

    public void solveEffect(Game game) {

        /*
         * list defeated_players
         * for each player
         *   player.calc_fire_power
         *   if(fire_power > pirates_fire_power)
         *       player.lose_days
         *       player.add_credits
         *       break
         *   if(fire_power == smuggler_fire_power)
         *       continue
         *   if(fire_power < smuggler_fire_power)
         *       defeated_players.add(player)
         *
         * for each shot
         *   defeated_players.get(0).roll_dices
         *   for each player in defeated_players
         *      player.handle_shot
         *
         */

    }

    @Override
    public AdventureCardState createState(Game game) {
        return new OpenSpaceState(game.getSortedPlayers(), this);
    }
}
