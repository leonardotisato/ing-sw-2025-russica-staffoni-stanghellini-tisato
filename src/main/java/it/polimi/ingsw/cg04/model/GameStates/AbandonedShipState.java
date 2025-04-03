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
    public AbandonedShipState(List<Player> players, AdventureCard adventureCard) {
        super(players, adventureCard);
    }
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }
}
