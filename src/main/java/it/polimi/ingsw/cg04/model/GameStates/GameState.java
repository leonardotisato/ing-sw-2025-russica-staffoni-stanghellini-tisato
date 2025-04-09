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

    public void placeTile(Player player, int x, int y) {
        throw new IllegalArgumentException("cant place tile in this game state");
    }

    public void placeInBuffer(Player player) {
        throw new IllegalArgumentException("cant place tile in buffer in this game state");
    }
}
