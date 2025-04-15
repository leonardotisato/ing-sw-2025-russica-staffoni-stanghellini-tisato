package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public class CloseFaceUpTilesAction implements PlayerAction {
    String playerNickname;

    public CloseFaceUpTilesAction(String playerNickname){
        this.playerNickname = playerNickname;
    }
    public void execute(Player player) {
        player.getGame().getGameState().closeFaceUpTiles(player);
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
