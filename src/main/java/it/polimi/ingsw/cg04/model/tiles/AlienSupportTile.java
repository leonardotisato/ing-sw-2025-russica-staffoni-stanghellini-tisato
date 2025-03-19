package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.Ship;

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
    public void broken(Ship ship) {
        // remove supported crewType
        for(Tile adj : adjacentHousingTiles) {
            if(adj.getSupportedCrewType().contains(getSupportedAlienColor())) {
                adj.removeSupportedCrewType(getSupportedAlienColor());
            }

            // if alien no longer supported remove it
            if(!adj.getSupportedCrewType().contains(adj.getHostedCrewType())) {
                ship.removeCrewByType(adj.getHostedCrewType());
                adj.removeCrewMember();

            }
        }
    }

    @Override
    public void place(Ship ship) {

        int shipHeight = ship.getTilesMatrix().length;
        int shipWidth = ship.getTilesMatrix()[0].length;
        Tile[][] tilesMatrix = ship.getTilesMatrix();

        for(int i=0; i<shipHeight; i++){
            for(int j=0; j<shipWidth; j++){
                if(tilesMatrix[i][j].equals(this)){

                    // look UP
                    if(i>0 && tilesMatrix[i-1][j] instanceof HousingTile) {
                        addAdjacentHousingTile(tilesMatrix[i-1][j]);
                        tilesMatrix[i-1][j].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look LEFT
                    if(j>0 && tilesMatrix[i][j-1] instanceof AlienSupportTile){
                        addAdjacentHousingTile(tilesMatrix[i][j-1]);
                        tilesMatrix[i][j-1].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look RIGHT
                    if(j<shipWidth-1 && tilesMatrix[i][j+1] instanceof AlienSupportTile){
                        addAdjacentHousingTile(tilesMatrix[i][j+1]);
                        tilesMatrix[i][j+1].addSupportedCrewType(getSupportedAlienColor());
                    }

                    // look DOWN
                    if(i<shipHeight-1 && tilesMatrix[i+1][j] instanceof HousingTile) {
                        addAdjacentHousingTile(tilesMatrix[i+1][j]);
                        tilesMatrix[i+1][j].addSupportedCrewType(getSupportedAlienColor());
                    }
                }
            }
        }
    }
}
