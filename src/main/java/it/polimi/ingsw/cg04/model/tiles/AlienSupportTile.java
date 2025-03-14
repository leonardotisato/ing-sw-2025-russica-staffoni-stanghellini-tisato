package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.Map;
import java.util.Set;

public class AlienSupportTile extends Tile {

    private final CrewType supportedAlienColor;
    private Set<Tile> adjacentHousingTiles;

    public AlienSupportTile(Map<Direction, Connection> connectionMap, CrewType supportedAlienColor) {
        super(connectionMap);
        this.supportedAlienColor = supportedAlienColor;
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
                adj.removeCrewType(getSupportedAlienColor());
            }

            // if alien no longer supported remove it
            if(!adj.getSupportedCrewType().contains(adj.getHostedCrewType())) {
                adj.removeCrewMember();
            }
        }
    }
}
