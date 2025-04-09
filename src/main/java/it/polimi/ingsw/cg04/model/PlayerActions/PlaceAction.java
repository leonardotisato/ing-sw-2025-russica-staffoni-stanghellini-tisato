package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

public class PlaceAction implements PlayerAction {
    private final String playerNickname;
    private final Coordinates coordinates;

    public PlaceAction(String playerNickname, int x, int y) {
        this.playerNickname = playerNickname;
        this.coordinates = new Coordinates(x, y);
    }
    public void execute(Player player) {
        Game game = player.getGame();
        game.getGameState().placeTile(player, coordinates.getX(), coordinates.getY());
    }

    @Override
    public boolean checkAction(Player player) {
        // check if slot is valid
        if (!player.getShip().isSlotValid(coordinates.getX(),coordinates.getY())) {
            return false;
        }
        // check if the player is holding a tile
        if(player.getHeldTile()==null) {
            return false;
        }

        // check if the slot is already taken
        Tile[][] tiles = player.getShip().getTilesMatrix();
        return tiles[coordinates.getX()][coordinates.getY()] == null;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
