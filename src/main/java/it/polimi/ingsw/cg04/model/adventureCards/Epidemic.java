package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AbandonedShipState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.tiles.AlienSupportTile;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;

public class Epidemic extends AdventureCard {

    public Epidemic() {
        super();
    }

    public void solveEffect(Player player) {
        Ship ship = player.getShip();
        Tile[][] tilesMatrix = ship.getTilesMatrix();
        Tile currentTile;
        for (int i = 0; i < ship.getShipHeight(); i++) {
            for (int j = 0; j < ship.getShipWidth(); j++) {
                if (tilesMatrix[i][j] != null && tilesMatrix[i][j] instanceof HousingTile && tilesMatrix[i][j].getNumCrew() > 0) {
                    currentTile = tilesMatrix[i][j];
                    // look UP -> check tile type and connection
                    if (i > 0 && tilesMatrix[i - 1][j] instanceof HousingTile
                            && tilesMatrix[i - 1][j].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.UP, tilesMatrix[i - 1][j])
                            && currentTile.getConnection(Direction.UP) != Connection.EMPTY) {
                        ship.removeCrew(currentTile.getHostedCrewType(), i, j, 1);
                    }
                    // look LEFT -> check tile type and connection
                    else if (j > 0 && tilesMatrix[i][j - 1] instanceof HousingTile
                            && tilesMatrix[i][j - 1].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.LEFT, tilesMatrix[i][j - 1])
                            && currentTile.getConnection(Direction.LEFT) != Connection.EMPTY) {
                        ship.removeCrew(currentTile.getHostedCrewType(), i, j, 1);
                    }

                    // look RIGHT -> check tile type and connection
                    else if (j < ship.getShipWidth() - 1 && tilesMatrix[i][j + 1] instanceof HousingTile
                            && tilesMatrix[i][j + 1].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.RIGHT, tilesMatrix[i][j + 1])
                            && currentTile.getConnection(Direction.RIGHT) != Connection.EMPTY) {
                        ship.removeCrew(currentTile.getHostedCrewType(), i, j, 1);
                    }

                    // look DOWN -> check tile type and connection
                    else if (i < ship.getShipHeight() - 1 && tilesMatrix[i + 1][j] instanceof HousingTile
                            && tilesMatrix[i + 1][j].getNumCrew() > 0
                            && currentTile.isValidConnection(Direction.DOWN, tilesMatrix[i + 1][j])
                            && currentTile.getConnection(Direction.DOWN) != Connection.EMPTY) {
                        ship.removeCrew(currentTile.getHostedCrewType(), i, j, 1);
                    }

                }
            }
        }
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new EpidemicState(game.getSortedPlayers(), this);
    }
}
