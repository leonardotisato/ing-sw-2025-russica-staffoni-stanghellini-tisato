package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

public class LaserTile extends Tile {

    private final boolean isDoubleLaser;

    public LaserTile(boolean isDoubleLaser) {
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

}
