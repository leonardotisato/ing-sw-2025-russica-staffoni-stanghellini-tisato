package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.HashSet;
import java.util.Set;

public class AlienSupportTile extends Tile {
    @Expose
    private CrewType supportedAlienColor;
    private final Set<Tile> adjacentHousingTiles;

    public AlienSupportTile() {
        this.adjacentHousingTiles = new HashSet<>();
    }

    public Set<Tile> getAdjacentHousingTiles() {
        return adjacentHousingTiles;
    }

    public void addAdjacentHousingTile(Tile tile){
        adjacentHousingTiles.add(tile);
    }

    public void removeAdjacentHousingTile(Tile tile){
        adjacentHousingTiles.remove(tile);
    }

    @Override
    public CrewType getSupportedAlienColor() {
        return supportedAlienColor;
    }

    public void setSupportedAlienColor(CrewType supportedAlienColor) {
        this.supportedAlienColor = supportedAlienColor;
    }

    /**
     * remove this tile supported crewType from connected HousingTiles attribute supportedCrewType
     * if now that HousingTile hosts an alien no longer supported removes it
     *
     * @param ship
     * @param x
     * @param y
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        // remove supported crewType
        for(Tile adj : adjacentHousingTiles) {
            if(adj.getSupportedCrewType().contains(getSupportedAlienColor())) {
                adj.removeSupportedCrewType(getSupportedAlienColor());
            }

            // if alien no longer supported remove it
            if(adj.getHostedCrewType() != null && !adj.getSupportedCrewType().contains(adj.getHostedCrewType())) {
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
     * add that HousingTile to this tile's lsit {@code adjacentHousingTile}
     *
     * @param ship
     * @param x
     * @param y
     */
    @Override
    public void place(Ship ship, int x, int y) {

        int shipHeight = ship.getTilesMatrix().length;
        int shipWidth = ship.getTilesMatrix()[0].length;
        Tile[][] tilesMatrix = ship.getTilesMatrix();

        for(int i=0; i<shipHeight; i++){
            for(int j=0; j<shipWidth; j++){
                if(tilesMatrix[i][j] != null && tilesMatrix[i][j].equals(this)){

                    // look UP -> check tile type and connection
                    if(i>0 /*&& tilesMatrix[i-1][j] instanceof HousingTile*/
                            && tilesMatrix[i-1][j] != null
                            && tilesMatrix[i-1][j].isCentralTile() != null
                            && this.isValidConnection(Direction.UP, tilesMatrix[i-1][j])
                            && this.getConnection(Direction.UP) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i-1][j]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player
                        ship.removeCrew(CrewType.HUMAN, i-1, j, 2);
                        tilesMatrix[i-1][j].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look LEFT -> check tile type and connection
                    if(j>0 /*&& tilesMatrix[i][j-1] instanceof HousingTile*/
                            && tilesMatrix[i][j-1] != null
                            && tilesMatrix[i][j-1].isCentralTile() != null
                            && this.isValidConnection(Direction.LEFT, tilesMatrix[i][j-1])
                            && this.getConnection(Direction.LEFT) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i][j-1]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player
                        ship.removeCrew(CrewType.HUMAN, i, j-1, 2);
                        tilesMatrix[i][j-1].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look RIGHT -> check tile type and connection
                    if(j<shipWidth-1 /*&& tilesMatrix[i][j+1] instanceof HousingTile*/
                            && tilesMatrix[i][j+1] != null
                            && tilesMatrix[i][j+1].isCentralTile() != null
                            && this.isValidConnection(Direction.RIGHT, tilesMatrix[i][j+1])
                            && this.getConnection(Direction.RIGHT) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i][j+1]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player
                        ship.removeCrew(CrewType.HUMAN, i, j+1, 2);
                        tilesMatrix[i][j+1].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look DOWN -> check tile type and connection
                    if(i<shipHeight-1 /*&& tilesMatrix[i+1][j] instanceof HousingTile*/
                            && tilesMatrix[i+1][j] != null
                            && tilesMatrix[i+1][j].isCentralTile() != null
                            && this.isValidConnection(Direction.DOWN, tilesMatrix[i+1][j])
                            && this.getConnection(Direction.DOWN) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i+1][j]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player
                        ship.removeCrew(CrewType.HUMAN, i+1, j, 2);
                        tilesMatrix[i+1][j].addSupportedCrewType(getSupportedAlienColor());
                    }
                    break;
                }
            }
        }
    }
}
