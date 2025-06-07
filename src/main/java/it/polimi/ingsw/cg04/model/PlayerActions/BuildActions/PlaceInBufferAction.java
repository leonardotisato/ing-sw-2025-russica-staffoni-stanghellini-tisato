package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class PlaceInBufferAction extends PlayerAction {

    public PlaceInBufferAction(String playerNickname) {
        super(playerNickname);
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.placeInBuffer(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // check if the player is holding a tile
        if (player.getHeldTile() == null) {
            throw new InvalidActionException("You are not holding a tile!");
        }

        // check if the buffer is already full
        if (player.getShip().getTilesBuffer().size() == 2) throw new InvalidActionException("Buffer is full!");
        return true;
    }
}
