package it.polimi.ingsw.cg04.controller.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class CloseFaceUpTilesAction extends PlayerAction {

    public CloseFaceUpTilesAction(String playerNickname) {
        super(playerNickname);
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.closeFaceUpTiles(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) {
        //return player.getHeldTile() == null; todo: do we need this check??
        return true;
    }
}
