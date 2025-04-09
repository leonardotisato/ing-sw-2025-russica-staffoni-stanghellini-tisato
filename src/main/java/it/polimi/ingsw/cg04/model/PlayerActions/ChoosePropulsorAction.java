package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class ChoosePropulsorAction implements PlayerAction {
    List<Coordinates> coordinates;
    List<Integer> usedBatteries;
    String nickname;

    public ChoosePropulsorAction(String nickname, List<Coordinates> coordinates, List<Integer> usedBatteries) {
        this.coordinates = new ArrayList<>(coordinates);
        this.usedBatteries = new ArrayList<>(usedBatteries);
        this.nickname = nickname;
    }

    public void execute(Player player) {
        GameState state = player.getGame().getGameState();
        state.usePropulsors(player, coordinates, usedBatteries);
    }

    @Override
    public boolean checkAction(Player player) {
        GameState gameState =  player.getGame().getGameState();
        List<Coordinates> propulsorCoordinates = player.getShip().getTilesMap().get("PropulsorTile");
        //the player is forced to player, he should send at least an empty list, otherwise error
        if(usedBatteries == null || coordinates == null) return false;
        int numberOfBatteries = usedBatteries.stream().mapToInt( b -> b.intValue()).sum();
        int totDoublePropulsor = (int)propulsorCoordinates.stream()
                .map(coord -> player.getShip().getTile(coord.getX(), coord.getY()))
                .filter(t -> t.isDoublePropulsor())
                .count();
        //the player wants to use more double propulsor than the actual number of them in the ship
        if(numberOfBatteries > totDoublePropulsor) return false;
        //mismatch error
        if(usedBatteries.size() != coordinates.size()) return false;
        for (int i = 0; i < usedBatteries.size(); i++) {
            //the tile in these coordinates is not a BatteryTile
            if(!coordinates.get(i).isIn(player.getShip().getTilesMap().get("BatteryTile"))){
                return false;
            }
            //not enough battery in this tile
            if(usedBatteries.get(i) > player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY()).getNumBatteries()){
                return false;
            }
        }
        //the action is legal!
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return this.nickname;
    }
}
