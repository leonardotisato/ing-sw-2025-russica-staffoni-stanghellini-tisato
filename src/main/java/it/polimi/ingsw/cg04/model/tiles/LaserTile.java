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

    /**
     *
     * @return {@code true} if the tile is a double laser, otherwise {@code false}.
     */
    @Override
    public Boolean isDoubleLaser() {
        return isDoubleLaser;
    }

    @Override
    public String getName() {
        if (isDoubleLaser) {
            return "D. Cannon";
        } else {
            return "S. Cannon";
        }
    }

    public void setDoubleLaser(boolean isDoubleLaser) {
        this.isDoubleLaser = isDoubleLaser;
    }

    /**
     * Determines and returns the shooting direction of the laser based on its connections.
     *
     * @return the {@code Direction} of the laser's shooting orientation if a GUN connection is found
     */
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
