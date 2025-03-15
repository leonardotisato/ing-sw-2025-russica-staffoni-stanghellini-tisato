package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AlienSupportTile extends Tile {

    private final CrewType supportedAlienColor;
    private final Set<Tile> adjacentHousingTiles;

    public AlienSupportTile(Map<Direction, Connection> connectionMap, CrewType supportedAlienColor) {
        super(connectionMap);
        this.supportedAlienColor = supportedAlienColor;
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
}
