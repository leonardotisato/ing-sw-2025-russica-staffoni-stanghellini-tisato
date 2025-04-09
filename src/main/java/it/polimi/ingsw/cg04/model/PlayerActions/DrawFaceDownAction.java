package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class DrawFaceDownAction implements PlayerAction {
    String playerNickname;

    public DrawFaceDownAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void execute(Player player) {
        player.getGame().getGameState().drawFaceDown(player);
    }

    @Override
    public boolean checkAction(Player player) {
        return player.getHeldTile() == null;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
