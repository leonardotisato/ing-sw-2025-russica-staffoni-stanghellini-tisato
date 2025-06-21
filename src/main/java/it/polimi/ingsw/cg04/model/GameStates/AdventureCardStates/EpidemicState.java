package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.ViewController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EpidemicState extends AdventureCardState {
    private int crewMembersLost = 0;

    private List<Coordinates> membersToRemove = new ArrayList<Coordinates>();

    public EpidemicState(Game game) {
        super(game);
    }


    /**
     * Executes the logic for spreading an epidemic within the ship of the specified player.
     * This method iterates over all housing tiles on the player's ship and evaluates the
     * spread of the epidemic based on the tile connectivity and neighboring tiles with crew members.
     * Crew members affected by the epidemic are removed from the ship accordingly.
     *
     * @param player the player whose ship is affected by the epidemic
     * @throws InvalidStateException if the epidemic has already been spread by the player
     */
    @Override
    public void spreadEpidemic(Player player) throws InvalidStateException {
        if (played.get(sortedPlayers.indexOf(player)) == 1)
            throw new InvalidStateException("Player " + player.getName() + " has already spread epidemic");
        Ship ship = player.getShip();
        Tile[][] tilesMatrix = ship.getTilesMatrix();
        Tile currentTile;
        Coordinates currentCoordinates;
        List<Coordinates> housingTilesCoordinates = ship.getTilesMap().get("HousingTile");
        for (int i = 0; i < ship.getShipHeight(); i++) {
            for (int j = 0; j < ship.getShipWidth(); j++) {
                currentCoordinates = new Coordinates(i, j);
                if (tilesMatrix[i][j] != null && currentCoordinates.isIn(housingTilesCoordinates) && tilesMatrix[i][j].getNumCrew() > 0) {
                    currentTile = tilesMatrix[i][j];
                    // look UP -> check tile type and connection
                    if (i > 0 && new Coordinates(i - 1, j).isIn(housingTilesCoordinates)
                            && tilesMatrix[i - 1][j].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.UP, tilesMatrix[i - 1][j])
                            && currentTile.getConnection(Direction.UP) != Connection.EMPTY) {
                        membersToRemove.add(new Coordinates(i, j));
                    }
                    // look LEFT -> check tile type and connection
                    else if (j > 0 && new Coordinates(i, j - 1).isIn(housingTilesCoordinates)
                            && tilesMatrix[i][j - 1].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.LEFT, tilesMatrix[i][j - 1])
                            && currentTile.getConnection(Direction.LEFT) != Connection.EMPTY) {
                        membersToRemove.add(new Coordinates(i, j));
                    }

                    // look RIGHT -> check tile type and connection
                    else if (j < ship.getShipWidth() - 1 && new Coordinates(i, j + 1).isIn(housingTilesCoordinates)
                            && tilesMatrix[i][j + 1].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.RIGHT, tilesMatrix[i][j + 1])
                            && currentTile.getConnection(Direction.RIGHT) != Connection.EMPTY) {
                        membersToRemove.add(new Coordinates(i, j));
                    }

                    // look DOWN -> check tile type and connection
                    else if (i < ship.getShipHeight() - 1 && new Coordinates(i + 1, j).isIn(housingTilesCoordinates)
                            && tilesMatrix[i + 1][j].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.DOWN, tilesMatrix[i + 1][j])
                            && currentTile.getConnection(Direction.DOWN) != Connection.EMPTY) {
                        membersToRemove.add(new Coordinates(i, j));
                    }

                }
            }
        }

        for(Coordinates coordinates : membersToRemove) {
            System.out.println("Removing " + coordinates.getX() + " " + coordinates.getY());
            Tile housingTile = ship.getTile(coordinates.getX(), coordinates.getY());
            ship.removeCrew(housingTile.getHostedCrewType(), coordinates.getX(), coordinates.getY(), 1);
            crewMembersLost++;
        }
        membersToRemove.clear();

        played.set(sortedPlayers.indexOf(player), 1);
        this.addLog("Player " + player.getName() + " has spread epidemic and he lost " + crewMembersLost + " crew members.");
        if (!played.contains(0)) {
            this.appendLog("The epidemic is finished!");
            triggerNextState();
        }
    }

    /**
     * Updates the given view with the current state of the provided game object.
     *
     * @param view      the view instance that should be updated
     * @param toDisplay the game object containing the state to render on the view
     * @throws IOException if an input or output exception occurs during the view update
     */
    @Override
    public void updateView(View view, Game toDisplay) throws IOException {
        view.renderEpidemicState(toDisplay);
    }

    @Override
    public void updateStateController(ViewController controller, Game game) {
        controller.updateEpidemicController(game);
    }
}

