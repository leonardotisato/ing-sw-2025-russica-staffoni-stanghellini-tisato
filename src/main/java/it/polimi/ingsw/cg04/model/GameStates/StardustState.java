package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.List;

public class StardustState extends AdventureCardState {
    public StardustState(Game game) {
        super(game);
    }
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }
}
