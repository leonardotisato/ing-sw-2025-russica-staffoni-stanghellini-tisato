package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class ReturnTileAction extends PlayerAction {

    public ReturnTileAction(String playerNickname) {
        super(playerNickname);
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.returnTile(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // cant return heldTile if you dont have one
        if (player.getHeldTile() == null)
            throw new InvalidActionException("You can't return a tile if you're not holding one");
        if (player.getHeldTile().getWasInBuffer())
            throw new InvalidActionException("You can't return a tile that was in the buffer");
        return true;
    }
}
