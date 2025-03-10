package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Shot;

import java.util.ArrayList;
import java.util.List;

public class Pirates extends AdventureCard {
    private final int firePower;
    private final int reward;
    private final List<Direction> directions;
    private final List<Shot> shots;

    public Pirates(int cardLevel, int daysLost, int firePower, int reward) {
        super(cardLevel, daysLost);
        this.firePower = firePower;
        this.reward = reward;
        directions = new ArrayList<Direction>();
        shots = new ArrayList<Shot>();
    }

    public void addCannonAttack(Direction direction, Shot shot) {
        directions.add(direction);
        shots.add(shot);
    }

    public int getFirePower() { return firePower; }

    public int getReward() { return reward; }

    public List<Direction> getDirections() { return directions; }

    public List<Shot> getShots() { return shots; }

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
}
