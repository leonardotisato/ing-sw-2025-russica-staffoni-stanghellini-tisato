package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.Ship;

public class LaserTile extends Tile {

    @Expose
    private boolean isDoubleLaser;

    public LaserTile() {
        super();
    }

    @Override
    public Boolean isDoubleLaser() {
        return isDoubleLaser;
    }
    public void setDoubleLaser(boolean isDoubleLaser) {
        this.isDoubleLaser = isDoubleLaser;
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
