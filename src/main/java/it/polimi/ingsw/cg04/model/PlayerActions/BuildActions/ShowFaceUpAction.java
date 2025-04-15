package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

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
        //todo: is this check correct??
        return player.getHeldTile() == null;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}