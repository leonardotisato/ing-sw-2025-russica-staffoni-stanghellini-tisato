package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

public class PlaceAction extends PlayerAction {
    private final String playerNickname;
    private final Coordinates coordinates;

    public PlaceAction(String playerNickname, int x, int y) {
        this.playerNickname = playerNickname;
        this.coordinates = new Coordinates(x, y);
    }
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.placeTile(player, coordinates.getX(), coordinates.getY());
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // check if slot is valid
        if (!player.getShip().isSlotValid(coordinates.getX(),coordinates.getY())) {
            throw new InvalidActionException("Coordinates" + coordinates.getX() + ", " + coordinates.getY() + "are out of bounds");
        }
        // check if the player is holding a tile
        if(player.getHeldTile()==null) {
            throw new InvalidActionException("You are not holding a tile!");
        }

        // check if the slot is already taken
        Tile[][] tiles = player.getShip().getTilesMatrix();
        if (tiles[coordinates.getX()][coordinates.getY()] != null) throw new InvalidActionException("Slot at " + coordinates.getX() + ", " + coordinates.getY() + " is already occupied!");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
