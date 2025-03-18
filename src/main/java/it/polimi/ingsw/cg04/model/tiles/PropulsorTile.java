package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.Map;

public class PropulsorTile extends Tile {

    private final boolean isDoublePropulsor;

    public PropulsorTile(Map<Direction, Connection> connectionMap, boolean isDoublePropulsor) {
        super();
        this.isDoublePropulsor = isDoublePropulsor;
    }

    @Override
    public Boolean isDoublePropulsor() {
        return isDoublePropulsor;
    }

    @Override
    public void broken(Ship ship){
        if(!isDoublePropulsor){
            ship.updateBasePropulsionPower(-1);
        }
    }
}
