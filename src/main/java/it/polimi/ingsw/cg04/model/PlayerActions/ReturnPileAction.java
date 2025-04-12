package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class ReturnPileAction implements PlayerAction {
    String playerNickname;
    int pileIndex;

    public ReturnPileAction(String playerNickname, int pileIndex) {
        this.playerNickname = playerNickname;
        this.pileIndex = pileIndex;
    }

    public void execute(Player player) {
        player.getGame().getGameState().returnPile(player);
    }

    @Override
    public boolean checkAction(Player player) {
        return pileIndex >= 0 && pileIndex < 2;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
