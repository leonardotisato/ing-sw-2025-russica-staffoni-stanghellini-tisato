package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

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
