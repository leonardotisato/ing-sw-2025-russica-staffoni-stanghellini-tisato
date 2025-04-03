package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class PlanetsState extends AdventureCardState {
    Map<Player, Integer> chosenPlanets;
    public PlanetsState(Game game) {
        super(game);
        this.chosenPlanets = new HashMap<>();
    }
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }
}
