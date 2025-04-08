package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;

public abstract class GameState {
    public void handleAction(Player player, PlayerAction action){
        return;
    }

    public void usePropulsors(Player player, List<Coordinates> coordinates, List<Integer> usedBatteries) {
        throw new RuntimeException("Invalid action!");
    }
    public void handleCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost){
        throw new RuntimeException("Invalid action!");
    }
}
