package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.Map;

public class AlienSupportTile extends Tile {

    private final CrewType supportedAlienColor;

    public AlienSupportTile(Map<Direction, Connection> connectionMap, CrewType supportedAlienColor) {
        super(connectionMap);
        this.supportedAlienColor = supportedAlienColor;
    }

    @Override
    public CrewType getSupportedAlienColor() {
        return supportedAlienColor;
    }
}
