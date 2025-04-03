package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AdventureCardState implements GameState {
    protected List <Player> sortedPlayers;
    protected List<Integer> played;
    protected Integer  currPlayerIdx;
    protected AdventureCard card;
    public AdventureCardState(List<Player> players, AdventureCard adventureCard) {
        this.sortedPlayers = players;
        this.card = adventureCard;
        this.played = new ArrayList<>(Collections.nCopies(players.size(), 0));
        this.currPlayerIdx = 0;
    }

    public abstract void handleAction(Player player, PlayerAction action);
}
