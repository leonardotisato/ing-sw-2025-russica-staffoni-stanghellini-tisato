package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.tiles.Tile;

public class ChooseTileAction implements PlayerAction {
    String playerNickname;
    Tile tile;

    public ChooseTileAction(String playerNickname, Tile tile) {
        this.playerNickname = playerNickname;
        this.tile = tile;
    }

    public void execute(Player player) {
        player.getGame().getGameState().chooseTile(player, tile);
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
