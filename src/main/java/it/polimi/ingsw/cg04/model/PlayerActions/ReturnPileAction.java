package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class ReturnPileAction implements PlayerAction {
    String playerNickname;

    public ReturnPileAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void execute(Player player) {
        player.getGame().getGameState().returnPile(player);
    }

    @Override
    public boolean checkAction(Player player) {
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
