package it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class ChoosePropulsorAction extends PlayerAction {
    List<Coordinates> coordinates;
    List<Integer> usedBatteries;

    public ChoosePropulsorAction(String nickname, List<Coordinates> coordinates, List<Integer> usedBatteries) {
        super(nickname);
        this.coordinates = new ArrayList<>(coordinates);
        this.usedBatteries = new ArrayList<>(usedBatteries);
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.usePropulsors(player, coordinates, usedBatteries);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        GameState gameState = player.getGame().getGameState();
        List<Coordinates> propulsorCoordinates = player.getShip().getTilesMap().get("PropulsorTile");
        //the player is forced to player, he should send at least an empty list, otherwise error
        if (usedBatteries == null || coordinates == null)
            throw new InvalidActionException("You can't send null, send an empty list instead!");
        int numberOfBatteries = usedBatteries.stream().mapToInt(b -> b.intValue()).sum();
        int totDoublePropulsor = (int) propulsorCoordinates.stream()
                .map(coord -> player.getShip().getTile(coord.getX(), coord.getY()))
                .filter(t -> t.isDoublePropulsor())
                .count();
        //the player wants to use more double propulsor than the actual number of them in the ship
        if (numberOfBatteries > totDoublePropulsor)
            throw new InvalidActionException("You have not enough double propulsors in your ship!");
        //mismatch error
        if (usedBatteries.size() != coordinates.size())
            throw new InvalidActionException("Coordinates size and used batteries size should be the same!");
        for (int i = 0; i < usedBatteries.size(); i++) {
            //the tile in these coordinates is not a BatteryTile
            if (!coordinates.get(i).isIn(player.getShip().getTilesMap().get("BatteryTile"))) {
                throw new InvalidActionException("Tile in (" + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + ") is not a BatteryTile!");
            }
            //not enough battery in this tile
            if (usedBatteries.get(i) > player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY()).getNumBatteries()) {
                throw new InvalidActionException("BatteryTile in (" + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + ") has not enough batteries!");
            }
        }
        //the action is legal!
        return true;
    }

}
