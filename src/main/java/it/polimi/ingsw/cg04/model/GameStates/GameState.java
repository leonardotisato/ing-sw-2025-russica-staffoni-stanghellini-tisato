package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;
import java.util.Map;

public abstract class GameState {
    public void handleAction(Player player, PlayerAction action){
        return;
    }

    public void usePropulsors(Player player, List<Coordinates> coordinates, List<Integer> usedBatteries) {
        throw new RuntimeException("Invalid action!");
    }
    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost){
        throw new RuntimeException("Invalid action!");
    }

    public void handleBoxes(Player player, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes){
        throw new RuntimeException("Invalid action!");
    }

    public void landToPlanet(Player player, Integer planetIdx, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) {
        throw new RuntimeException("Invalid action!");
    }
}
