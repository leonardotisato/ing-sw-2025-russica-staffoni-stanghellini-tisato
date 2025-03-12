package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.Map;

public class LaserTile extends Tile {

    private final boolean isDoubleLaser;

    public LaserTile(Map<Direction, Connection> connectionMap, boolean isDoubleLaser) {
        super(connectionMap);
        this.isDoubleLaser = isDoubleLaser;
    }

    @Override
    public Boolean isDoubleLaser() {
        return isDoubleLaser;
    }

    @ Override
    public Direction getShootingDirection() {
        for (Direction dir : connections.keySet()) {
            if (connections.get(dir) == Connection.GUN) {
                return dir;
            }
        }

        assert(false);
        return null;
    }

    @Override
    public void broken(Ship ship){
        if(!isDoubleLaser){
            if(getShootingDirection() == Direction.UP){
                ship.updateBaseFirePower(-1);
            } else ship.updateBaseFirePower(-0.5);
        }
    }

}
