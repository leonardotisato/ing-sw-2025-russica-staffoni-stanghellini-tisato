package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class ReturnPileAction implements PlayerAction {
    String playerNickname;
    Integer pileIndex;

    public ReturnPileAction(String playerNickname, Integer pileIndex) {
        this.playerNickname = playerNickname;
        this.pileIndex = pileIndex;
    }

    public void execute(Player player) {
        player.getGame().getGameState().returnPile(player, pileIndex);
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
