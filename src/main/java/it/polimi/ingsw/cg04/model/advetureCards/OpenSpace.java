package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

public class OpenSpace extends AdventureCard {

    public OpenSpace(int cardLevel, int daysLost) {
        super(cardLevel, daysLost);
    }

    public void solveEffect(Game game) {
        int totalPropulsionPower;

        for (Player player : game.getPlayers()) {
            totalPropulsionPower = player.getShip().calcTotalPropulsionPower(player.getShip().askNumBatteriesToUse());
            player.move(totalPropulsionPower);
        }
    }
}
