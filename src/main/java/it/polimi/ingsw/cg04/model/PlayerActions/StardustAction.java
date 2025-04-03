package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;

public class StardustAction implements PlayerAction {

    public void execute(Player player) {
        player.move(-player.getShip().getNumExposedConnectors());
    }
}
