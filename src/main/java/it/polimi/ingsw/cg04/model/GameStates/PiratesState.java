package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PiratesState extends AdventureCardState {
    List<Player> defeatedPlayers;
    Integer dice;
    Player winner;
    public PiratesState(List<Player> players, AdventureCard adventureCard) {
        super(players, adventureCard);
        this.defeatedPlayers = new ArrayList<>();
        this.dice = 0;
        winner = null;
    }
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }
}
