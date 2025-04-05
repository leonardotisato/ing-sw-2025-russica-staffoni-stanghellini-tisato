package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleCrewAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbandonedShipState extends AdventureCardState {
    public AbandonedShipState(Game game) {
        super(game);
    }
    public void handleAction(Player player, PlayerAction action) {
        if (player.equals(this.sortedPlayers.get(this.currPlayerIdx))) {
            action.execute(player);
        }
        else throw new RuntimeException("Player " + player.getName() + ", is not your turn!");
        if (!played.contains(0)) {
            triggerNextState();
        }
    }
}
