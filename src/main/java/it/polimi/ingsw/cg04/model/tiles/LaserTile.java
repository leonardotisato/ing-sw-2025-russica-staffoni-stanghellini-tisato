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
        this.tileColor = "\u001B[35m";
    }

    @Override
    public Boolean isDoubleLaser() {
        return isDoubleLaser;
    }

    @Override
    String getName() {
        if (isDoubleLaser) {
            return "D. Cannon";
        } else {
            return "S. Cannon";
        }
    }

    // todo: handle unused method
    public void setDoubleLaser(boolean isDoubleLaser) {
        this.isDoubleLaser = isDoubleLaser;
    }

    @Override
    public Direction getShootingDirection() {
        for (Direction dir : connections.keySet()) {
            if (connections.get(dir) == Connection.GUN) {
                return dir;
            }
        }

        assert (false);
        return null;
    }

    /**
     * updates {@code baseFirePower} attribute in ship
     *
     * @param ship player's {@code Ship}
     * @param x {@code int} coordinate
     * @param y {@code int} coordinate
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        if (!isDoubleLaser) {
            if (getShootingDirection() == Direction.UP) {
                ship.updateBaseFirePower(-1);
            } else ship.updateBaseFirePower(-0.5);
        }
    }

    /**
     * updates {@code baseFirePower} attribute in ship
     *
     * @param ship player's {@code Ship}
     * @param x {@code int} coordinate
     * @param y {@code int} coordinate
     */
    @Override
    public void place(Ship ship, int x, int y) {
        if (!isDoubleLaser) {
            if (getShootingDirection() == Direction.UP) {
                ship.updateBaseFirePower(1);
            } else ship.updateBaseFirePower(0.5);
        }
    }
}
