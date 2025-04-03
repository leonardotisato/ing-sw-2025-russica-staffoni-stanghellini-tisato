package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

import java.util.Map;

public class PlanetsState extends AdventureCardState {
    Map<Player, Integer> choosenPlanet;

    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }
}
