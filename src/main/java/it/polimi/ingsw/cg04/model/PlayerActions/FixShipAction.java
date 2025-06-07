package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;

public class FixShipAction extends PlayerAction {

    private final List<Coordinates> coordinatesList;


    public FixShipAction(String playerNickname, List<Coordinates> coordinatesList) {
        super(playerNickname);
        this.coordinatesList = coordinatesList.stream()
                .distinct()
                .toList();
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.fixShip(player, coordinatesList);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // check that all tiles selected are not null in the ship matrix
        for (Coordinates coordinates : coordinatesList) {

            // if coords is out of bound return false
            if (!player.getShip().isSlotValid(coordinates.getX(), coordinates.getY())) {
                throw new InvalidActionException("Coordinates " + coordinates.getX() + ", " + coordinates.getY() + " are out of bounds");
            }

            if (player.getShip().getTile(coordinates.getX(), coordinates.getY()) == null) {
                throw new InvalidActionException("Tile at coordinates " + coordinates.getX() + ", " + coordinates.getY() + " is null");
            }
        }

        return true;
    }

}
