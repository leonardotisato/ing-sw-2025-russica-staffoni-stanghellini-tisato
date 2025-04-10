package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class ShowFaceUpAction implements PlayerAction {
    String playerNickname;

    public ShowFaceUpAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    @Override
    public void execute(Player player) {
        player.getGame().getGameState().showFaceUp(player);
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