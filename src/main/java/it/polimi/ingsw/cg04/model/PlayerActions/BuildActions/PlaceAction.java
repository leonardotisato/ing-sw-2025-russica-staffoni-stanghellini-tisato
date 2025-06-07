package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

public class PlaceAction extends PlayerAction {
    private final Coordinates coordinates;

    public PlaceAction(String playerNickname, int x, int y) {
        super(playerNickname);
        this.coordinates = new Coordinates(x, y);
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();

        // backdoor codes for faster shipbuilding
        if (coordinates.getX() == 103 && coordinates.getY() == 103) {
            state.setShip(player, coordinates.getX(), coordinates.getY());
        } else if (coordinates.getX() == 104 && coordinates.getY() == 104) {
            state.setShip(player, coordinates.getX(), coordinates.getY());
        } else if (coordinates.getX() == 105 && coordinates.getY() == 105) {
            state.setShip(player, coordinates.getX(), coordinates.getY());
        } else {
            // normal place actions
            state.placeTile(player, coordinates.getX(), coordinates.getY());
        }

        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {

        // allow backdoor codes
        if (coordinates.getX() == 103 && coordinates.getY() == 103) return true;
        if (coordinates.getX() == 104 && coordinates.getY() == 104) return true;
        if (coordinates.getX() == 105 && coordinates.getY() == 105) return true;

        // check if the slot is valid
        if (!player.getShip().isSlotValid(coordinates.getX(), coordinates.getY())) {
            throw new InvalidActionException("Coordinates " + coordinates.getX() + ", " + coordinates.getY() + " are out of bounds");
        }
        // check if the player is holding a tile
        if (player.getHeldTile() == null) {
            throw new InvalidActionException("You are not holding a tile!");
        }

        if (!(player.getShip().isPlacingLegal(player.getHeldTile(), coordinates.getX(), coordinates.getY()))) {
            throw new InvalidActionException("You can't place a tile at " + coordinates.getX() + ", " + coordinates.getY() + "!");
        }

        // check if the slot is already taken
        Tile[][] tiles = player.getShip().getTilesMatrix();
        if (tiles[coordinates.getX()][coordinates.getY()] != null)
            throw new InvalidActionException("Slot at " + coordinates.getX() + ", " + coordinates.getY() + " is already occupied!");
        return true;
    }
}
