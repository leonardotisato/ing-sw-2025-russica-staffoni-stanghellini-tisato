package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class CloseFaceUpTilesAction extends PlayerAction {
    String playerNickname;

    public CloseFaceUpTilesAction(String playerNickname){
        this.playerNickname = playerNickname;
    }
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

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
