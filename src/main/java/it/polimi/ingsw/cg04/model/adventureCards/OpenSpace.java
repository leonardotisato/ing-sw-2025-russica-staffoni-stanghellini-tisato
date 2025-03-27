package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

import java.util.List;

public class OpenSpace extends AdventureCard {

    public OpenSpace() {
        super();
    }

    public void solveEffect(Player player, List<List<Integer>> coordinates, List<Integer> usedBatteries) {
        for(int i = 0; i < usedBatteries.size(); i++){
            player.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).get(0), coordinates.get(i).get(1));
        }
        player.move(player.getShip().getBasePropulsionPower() + usedBatteries.size() * 2);
    }
}
