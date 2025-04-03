package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.List;

public class SlaversState extends AdventureCardState {
    public SlaversState(List<Player> players, AdventureCard adventureCard) {
        super(players, adventureCard);
    }
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }
}
