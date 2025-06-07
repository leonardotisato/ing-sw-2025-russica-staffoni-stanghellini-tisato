package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlienSupportTile extends Tile {
    @Expose
    private CrewType supportedAlienColor;
    private final Set<Tile> adjacentHousingTiles;

    public AlienSupportTile() {
        this.adjacentHousingTiles = new HashSet<>();
        this.shortName = "A. Support";

        if (supportedAlienColor == CrewType.BROWN_ALIEN) {
            this.tileColor = "\u001B[33m";
        } else {
            this.tileColor = "\u001B[38;5;205m";
        }
    }

    @Override
    public String getName() {
        if (supportedAlienColor == CrewType.BROWN_ALIEN) {
            return "B. Support";
        } else {
            return "P. Support";
        }
    }

    @Override
    public String getTileColor() {
        if (supportedAlienColor == CrewType.BROWN_ALIEN) {
            return "\u001B[33m";
        } else {
            return "\u001B[38;5;205m";
        }
    }

    /**
     *
     * @return a set of Tile objects representing the adjacent housing tiles.
     */
    @Override
    public Set<Tile> getAdjacentHousingTiles() {
        return adjacentHousingTiles;
    }

    /**
     * Adds a housing tile adjacent to this tile.
     *
     * @param tile the Tile object representing the housing tile to be added as adjacent
     */
    @Override
    public void addAdjacentHousingTile(Tile tile) {
        adjacentHousingTiles.add(tile);
    }

    /**
     * Removes the specified adjacent housing tile from the set of housing tiles connected to this tile.
     *
     * @param tile the Tile object representing the housing tile to be removed from the adjacency list
     */
    @Override
    public void removeAdjacentHousingTile(Tile tile) {
        adjacentHousingTiles.remove(tile);
    }

    /**
     *
     * @return the CrewType indicating the supported alien color for this tile
     */
    @Override
    public CrewType getSupportedAlienColor() {
        return supportedAlienColor;
    }

    // todo: handle unused method
    public void setSupportedAlienColor(CrewType supportedAlienColor) {
        this.supportedAlienColor = supportedAlienColor;
    }

    /**
     * remove this tile supported crewType from connected HousingTiles attribute supportedCrewType
     * if now that HousingTile hosts an alien no longer supported removes it
     *
     * @param ship player's ship
     * @param x    coordinate
     * @param y    coordinate
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        // remove supported crewType
        for (Tile adj : adjacentHousingTiles) {
            if (adj.getSupportedCrewType().contains(getSupportedAlienColor())) {
                adj.removeSupportedCrewType(getSupportedAlienColor());
            }

            // if alien no longer supported remove it
            if (adj.getHostedCrewType() != null && !adj.getSupportedCrewType().contains(adj.getHostedCrewType())) {
                ship.removeCrewByType(adj.getHostedCrewType());
                //ship.removeCrew(adj.getHostedCrewType(), ?, ?, adj.getNumCrew());
                adj.removeCrewMember();
            }

            // clear in case of reuse of this tile
            adjacentHousingTiles.clear();
        }
    }

    /**
     * if there is an HousingTile connected to this tile, update it's supportedCrewType, remove HUMANS from it,
     * add that HousingTile to this tile's list {@code adjacentHousingTile}
     *
     * @param ship player's ship
     * @param x    coordinate
     * @param y    coordinate
     */
    @Override
    public void place(Ship ship, int x, int y) {

        int shipHeight = ship.getTilesMatrix().length;
        int shipWidth = ship.getTilesMatrix()[0].length;
        Tile[][] tilesMatrix = ship.getTilesMatrix();
        List<Coordinates> housingTilesIdx = ship.getTilesMap().get("HousingTile");
        for (int i = 0; i < shipHeight; i++) {
            for (int j = 0; j < shipWidth; j++) {
                if (tilesMatrix[i][j] != null && tilesMatrix[i][j].equals(this)) {
                    // look UP -> check tile type and connection
                    if (i > 0 && new Coordinates(i - 1, j).isIn(housingTilesIdx)
                            && this.isValidConnection(Direction.UP, tilesMatrix[i - 1][j])
                            && this.getConnection(Direction.UP) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i - 1][j]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player

                        // UNCOMMENT THE FOLLOWING LINE TO APLY THE LOGGIC THAT REMOVES HUMANS
                        // IN CASE A HOUSING SUPPORT TILE SUPPORTS ONE OR MORE ALIEN TYPES
                        //ship.removeCrew(CrewType.HUMAN, i-1, j, 2);
                        tilesMatrix[i - 1][j].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look LEFT -> check tile type and connection
                    if (j > 0 && new Coordinates(i, j - 1).isIn(housingTilesIdx)
                            && this.isValidConnection(Direction.LEFT, tilesMatrix[i][j - 1])
                            && this.getConnection(Direction.LEFT) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i][j - 1]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player

                        // UNCOMMENT THE FOLLOWING LINE TO APLY THE LOGGIC THAT REMOVES HUMANS
                        // IN CASE A HOUSING SUPPORT TILE SUPPORTS ONE OR MORE ALIEN TYPES
                        //ship.removeCrew(CrewType.HUMAN, i, j-1, 2);
                        tilesMatrix[i][j - 1].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look RIGHT -> check tile type and connection
                    if (j < shipWidth - 1 && new Coordinates(i, j + 1).isIn(housingTilesIdx)
                            && this.isValidConnection(Direction.RIGHT, tilesMatrix[i][j + 1])
                            && this.getConnection(Direction.RIGHT) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i][j + 1]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player

                        // UNCOMMENT THE FOLLOWING LINE TO APLY THE LOGGIC THAT REMOVES HUMANS
                        // IN CASE A HOUSING SUPPORT TILE SUPPORTS ONE OR MORE ALIEN TYPES
                        //ship.removeCrew(CrewType.HUMAN, i, j+1, 2);
                        tilesMatrix[i][j + 1].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look DOWN -> check tile type and connection
                    if (i < shipHeight - 1 && new Coordinates(i + 1, j).isIn(housingTilesIdx)
                            && this.isValidConnection(Direction.DOWN, tilesMatrix[i + 1][j])
                            && this.getConnection(Direction.DOWN) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i + 1][j]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player

                        // UNCOMMENT THE FOLLOWING LINE TO APLY THE LOGGIC THAT REMOVES HUMANS
                        // IN CASE A HOUSING SUPPORT TILE SUPPORTS ONE OR MORE ALIEN TYPES
                        //ship.removeCrew(CrewType.HUMAN, i+1, j, 2);
                        tilesMatrix[i + 1][j].addSupportedCrewType(getSupportedAlienColor());
                    }
                    break;
                }
            }
        }
    }
}
