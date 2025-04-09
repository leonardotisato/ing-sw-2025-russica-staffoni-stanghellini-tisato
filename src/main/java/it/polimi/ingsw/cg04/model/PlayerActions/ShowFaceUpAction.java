package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

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
        if(player.getHeldTile()!=null) {
            return false;
        }
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}