package it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class ChooseBatteryAction extends PlayerAction {
    private final int x, y;

    public ChooseBatteryAction(String playerNick, int x, int y) {
        super(playerNick);
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.chooseBattery(player, x, y);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {

        // x=y=-1 means "Dont use batteries"
        if (x == -1 && y == -1) {
            return true;
        }

        // if received coords are out of bound
        if (!player.getShip().isSlotValid(x, y)) {
            throw new InvalidActionException("Coordinates " + x + ", " + y + " are out of bounds");
        }

        // check if input coordinates match a battery tile with batteries on it
        Integer batteriesInSelectedCoords = player.getShip().getTile(x, y).getNumBatteries();

        if (batteriesInSelectedCoords == null) {
            System.out.println("No batteries in selected coords");
            throw new InvalidActionException("There are no batteries in the tile at " + x + ", " + y + " )");
        }

        if (batteriesInSelectedCoords <= 0) {
            System.out.println("Expected batteries to be greater than 0");
            throw new InvalidActionException("There are not enough batteries in the tile at " + x + ", " + y + " )");
        }

        return true;
    }
}
