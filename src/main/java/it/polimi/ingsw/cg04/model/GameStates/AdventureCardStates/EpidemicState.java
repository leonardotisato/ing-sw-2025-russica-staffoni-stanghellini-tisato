package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;

public class EpidemicState extends AdventureCardState {
    private int crewMembersLost = 0;
    public EpidemicState(Game game) {super(game);}


    public void spreadEpidemic(Player player) throws InvalidStateException {
        if (played.get(sortedPlayers.indexOf(player)) == 1) throw new InvalidStateException("Player " + player.getName() + " has already spread epidemic");
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
                    if (i > 0 && new Coordinates(i-1, j).isIn(housingTilesCoordinates)
                            && tilesMatrix[i - 1][j].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.UP, tilesMatrix[i - 1][j])
                            && currentTile.getConnection(Direction.UP) != Connection.EMPTY) {
                        ship.removeCrew(currentTile.getHostedCrewType(), i, j, 1);
                        crewMembersLost++;
                    }
                    // look LEFT -> check tile type and connection
                    else if (j > 0 && new Coordinates(i, j-1).isIn(housingTilesCoordinates)
                            && tilesMatrix[i][j - 1].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.LEFT, tilesMatrix[i][j - 1])
                            && currentTile.getConnection(Direction.LEFT) != Connection.EMPTY) {
                        ship.removeCrew(currentTile.getHostedCrewType(), i, j, 1);
                        crewMembersLost++;
                    }

                    // look RIGHT -> check tile type and connection
                    else if (j < ship.getShipWidth() - 1 && new Coordinates(i, j+1).isIn(housingTilesCoordinates)
                            && tilesMatrix[i][j + 1].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.RIGHT, tilesMatrix[i][j + 1])
                            && currentTile.getConnection(Direction.RIGHT) != Connection.EMPTY) {
                        ship.removeCrew(currentTile.getHostedCrewType(), i, j, 1);
                        crewMembersLost++;
                    }

                    // look DOWN -> check tile type and connection
                    else if (i < ship.getShipHeight() - 1 && new Coordinates(i+1, j).isIn(housingTilesCoordinates)
                            && tilesMatrix[i + 1][j].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.DOWN, tilesMatrix[i + 1][j])
                            && currentTile.getConnection(Direction.DOWN) != Connection.EMPTY) {
                        ship.removeCrew(currentTile.getHostedCrewType(), i, j, 1);
                        crewMembersLost++;
                    }

                }
            }
        }
        played.set(sortedPlayers.indexOf(player), 1);
        this.addLog("Player " + player.getName() + " has spread epidemic and he lost " + crewMembersLost + " crew members.");
        if(!played.contains(0)){
            this.addLog("The epidemic is finished!");
            triggerNextState();
        }
    }

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder(super.render(playerName));
        stringBuilder.append("\n".repeat(3));
        stringBuilder.append("Send x to spread the epidemic (you need to spread this epidemic to continue the game).");
        stringBuilder.append("\n");
        stringBuilder.append("You may lose some crew members!").append("\n");
        return stringBuilder.toString();
    }
}

