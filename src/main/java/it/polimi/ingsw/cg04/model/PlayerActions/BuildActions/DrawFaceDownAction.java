package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class DrawFaceDownAction extends PlayerAction {

    public DrawFaceDownAction(String playerNickname) {
        super(playerNickname);
    }

    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.drawFaceDown(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // player is already holding a tile
        if (player.getHeldTile() != null) throw new InvalidActionException("Already holding a tile!");
        return true;
    }
}
