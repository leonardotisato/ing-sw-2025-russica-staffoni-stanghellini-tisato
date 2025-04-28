package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;

public class ChooseTileAction extends PlayerAction {
    String playerNickname;
    int tileID;

    public ChooseTileAction(String playerNickname, int tileID) {
        this.playerNickname = playerNickname;
        this.tileID = tileID;
    }

    public void execute(Player player) throws InvalidStateException {
        player.getGame().getGameState().chooseTile(player, tileID);
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // player is already holding a tile
        if (player.getHeldTile() != null) throw new InvalidActionException("Already holding a tile!");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
