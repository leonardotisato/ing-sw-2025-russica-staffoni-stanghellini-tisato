package it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompareFirePowerAction extends PlayerAction {
    private final List<Coordinates> batteryCoordsList;
    private final List<Coordinates> cannonCoordsList;

    public CompareFirePowerAction(String playerNickName, List<Coordinates> batteryCoordsList, List<Coordinates> cannonCoordsList) {
        super(playerNickName);
        this.batteryCoordsList = batteryCoordsList;
        this.cannonCoordsList = cannonCoordsList;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.compareFirePower(player, batteryCoordsList, cannonCoordsList);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {

        // if both lists are null, player does not want to activate any double cannon
        if (batteryCoordsList == null && cannonCoordsList == null) {
            return true;
        }

        // format checks
        if (batteryCoordsList == null || cannonCoordsList == null) {
            throw new InvalidActionException("Wrong inputs format!");
        }
        if (batteryCoordsList.size() != cannonCoordsList.size()) {
            throw new InvalidActionException("batteryCoordsList.size() = " + batteryCoordsList.size() +
                    ", cannonCoordsList.size() = " + cannonCoordsList.size());
        }

        Ship ship = player.getShip();

        // check batteryCoordsList
        Set<Coordinates> batteryCoordsSet = new HashSet<>(batteryCoordsList);
        for (Coordinates batteryCoords : batteryCoordsSet) {

            // check that batteryCoords is a batteryTile
            if (!batteryCoords.isIn(ship.getTilesMap().get("BatteryTile"))) {
                throw new InvalidActionException("Tile in (" + batteryCoords.getX() + ", " + batteryCoords.getY() + ") is not a BatteryTile!");
            }

            // if it's a batteryTile then check that occurrences in batteryCoordsList is not bigger than batteries available in that tile
            int occurrences = (int) batteryCoordsList.stream().filter(c -> c.equals(batteryCoords)).count();
            if (occurrences > ship.getTile(batteryCoords.getX(), batteryCoords.getY()).getNumBatteries()) {
                throw new InvalidActionException("There are not enough batteries in (" + batteryCoords.getX() + ", " + batteryCoords.getY() + ")");
            }
        }

        // check cannonCoordsList
        Set<Coordinates> cannonCoordsSet = new HashSet<>(cannonCoordsList);
        for (Coordinates cannonCoords : cannonCoordsSet) {

            // check that cannonCoords is a cannonTile
            if (!cannonCoords.isIn(ship.getTilesMap().get("LaserTile"))) {
                throw new InvalidActionException("Tile in (" + cannonCoords.getX() + ", " + cannonCoords.getY() + ") is not a CannonTile!");
            }

            // check that cannon isDouble
            if (!ship.getTile(cannonCoords.getX(), cannonCoords.getY()).isDoubleLaser()) {
                throw new InvalidActionException("Cannon in (" + cannonCoords.getX() + ", " + cannonCoords.getY() + ") is not double-cannon!");
            }
        }

        return true;
    }
}
