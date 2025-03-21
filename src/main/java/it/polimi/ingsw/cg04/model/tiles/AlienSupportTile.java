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

    @Override
    // remove supported crewType form adjacent HousingTiles
    // if this implies that now those HousingTiles contains unsupportedCrewTypes remove them
    public void broken(Ship ship) {
        // remove supported crewType
        for(Tile adj : adjacentHousingTiles) {
            if(adj.getSupportedCrewType().contains(getSupportedAlienColor())) {
                adj.removeSupportedCrewType(getSupportedAlienColor());
            }

            // if alien no longer supported remove it
            if(adj.getHostedCrewType() != null && !adj.getSupportedCrewType().contains(adj.getHostedCrewType())) {
                ship.removeCrewByType(adj.getHostedCrewType());
                adj.removeCrewMember();
            }

            // clear in case of reuse of this tile
            adjacentHousingTiles.clear();
        }
    }

    // if there is a HousingTile adjacent to this add it to the adjacentList
    // update supportedCrewType for adjacent HousingTiles and remove it's crewMembers than leave the choice ok alien/human to the player
    @Override
    public void place(Ship ship) {

        int shipHeight = ship.getTilesMatrix().length;
        int shipWidth = ship.getTilesMatrix()[0].length;
        Tile[][] tilesMatrix = ship.getTilesMatrix();

        for(int i=0; i<shipHeight; i++){
            for(int j=0; j<shipWidth; j++){
                if(tilesMatrix[i][j] != null && tilesMatrix[i][j].equals(this)){

                    // look UP -> check tile type and connection
                    if(i>0 && tilesMatrix[i-1][j] instanceof HousingTile
                            && this.isValidConnection(Direction.UP, tilesMatrix[i-1][j])
                            && this.getConnection(Direction.UP) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i-1][j]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player
                        tilesMatrix[i-1][j].removeCrewMember();
                        tilesMatrix[i-1][j].removeCrewMember();
                        ship.removeCrewByType(CrewType.HUMAN);
                        ship.removeCrewByType(CrewType.HUMAN);
                        tilesMatrix[i-1][j].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look LEFT -> check tile type and connection
                    if(j>0 && tilesMatrix[i][j-1] instanceof HousingTile
                            && this.isValidConnection(Direction.LEFT, tilesMatrix[i][j-1])
                            && this.getConnection(Direction.LEFT) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i][j-1]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player
                        tilesMatrix[i][j-1].removeCrewMember();
                        tilesMatrix[i][j-1].removeCrewMember();
                        ship.removeCrewByType(CrewType.HUMAN);
                        ship.removeCrewByType(CrewType.HUMAN);
                        tilesMatrix[i][j-1].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look RIGHT -> check tile type and connection
                    if(j<shipWidth-1 && tilesMatrix[i][j+1] instanceof HousingTile
                            && this.isValidConnection(Direction.RIGHT, tilesMatrix[i][j+1])
                            && this.getConnection(Direction.RIGHT) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i][j+1]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player
                        tilesMatrix[i][j+1].removeCrewMember();
                        tilesMatrix[i][j+1].removeCrewMember();
                        ship.removeCrewByType(CrewType.HUMAN);
                        ship.removeCrewByType(CrewType.HUMAN);
                        tilesMatrix[i][j+1].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look DOWN -> check tile type and connection
                    if(i<shipHeight-1 && tilesMatrix[i+1][j] instanceof HousingTile
                            && this.isValidConnection(Direction.DOWN, tilesMatrix[i+1][j])
                            && this.getConnection(Direction.DOWN) != Connection.EMPTY) {
                        addAdjacentHousingTile(tilesMatrix[i+1][j]);
                        // if this is adjacent to a housing tile than remove crew members and leave the choice to the player
                        tilesMatrix[i+1][j].removeCrewMember();
                        tilesMatrix[i+1][j].removeCrewMember();
                        ship.removeCrewByType(CrewType.HUMAN);
                        ship.removeCrewByType(CrewType.HUMAN);
                        tilesMatrix[i+1][j].addSupportedCrewType(getSupportedAlienColor());
                    }
                    break;
                }
            }
        }
    }
}
