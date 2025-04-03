package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.List;

public class StardustState extends AdventureCardState {

    public StardustState(Game game) {
        super(game);
        this.currPlayerIdx = sortedPlayers.size() - 1;
    }

    public void handleAction(Player player, PlayerAction action) {
        if(player == sortedPlayers.get(currPlayerIdx)) {
            action.execute(player);
            if(currPlayerIdx == 0) {
                triggerNextState();
            }
            else {
                currPlayerIdx--;
            }
        }
        else {
            throw new RuntimeException("It's not " + player + " turn.");
        }
    }
}
