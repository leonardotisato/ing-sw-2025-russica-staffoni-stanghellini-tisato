package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class ChooseTileFromBufferAction extends PlayerAction {
    String playerNickname;
    int idx;

    public ChooseTileFromBufferAction(String playerNickname, int idx) {
        this.playerNickname = playerNickname;
        this.idx = idx;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.chooseTileFromBuffer(player, idx);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        if (player.getHeldTile() != null) throw new InvalidActionException("Already holding a tile!");
        if (idx < 0 || idx > 1) throw new InvalidActionException("Index must be between 0 and 1!");
        if (player.getShip().getTilesBuffer().size() < idx + 1) throw new InvalidActionException("invalid index");

        return true;
    }

    @Override
    public String getPlayerNickname(){
        return playerNickname;
    }
}
