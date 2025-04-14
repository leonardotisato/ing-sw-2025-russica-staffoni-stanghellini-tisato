package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class PickPileAction implements PlayerAction {
    String playerNickname;
    int pileIndex;

    public PickPileAction(String playerNickname, int pileIndex) {
        this.playerNickname = playerNickname;
        this.pileIndex = pileIndex;
    }

    @Override
    public void execute(Player player) {
        player.getGame().getGameState().pickPile(player, pileIndex);
    }

    @Override
    public boolean checkAction(Player player){
        return pileIndex > 0 && pileIndex < 4;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
