package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class ReturnTileAction implements PlayerAction {
    String playerNickname;

    public ReturnTileAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }
    public void execute(Player player) {
        player.getGame().getGameState().returnTile(player);
    }

    @Override
    public boolean checkAction(Player player) {
        // cant return heldTile if you dont have one
        return player.getHeldTile() != null;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
