package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.List;

public abstract class AdventureCardState implements GameState {
    private List <Player> sortedPlayers;
    private List<Integer> played;
    private Integer  currPlayerIdx;
    private AdventureCard card;

    public abstract void handleAction(Player player, PlayerAction action);
}
