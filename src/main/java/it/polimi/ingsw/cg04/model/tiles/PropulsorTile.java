package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Ship;

public class PropulsorTile extends Tile {

    @Expose
    private boolean isDoublePropulsor;

    public PropulsorTile() {
        super();
        this.tileColor = "\u001B[38;5;220m";
    }

    /**
     *
     * @return {@code true} if the propulsor is a double propulsor, {@code false} otherwise.
     */
    @Override
    public Boolean isDoublePropulsor() {
        return isDoublePropulsor;
    }

    @Override
    String getName() {
        if (isDoublePropulsor) {
            return "D. Prop.";
        } else {
            return "S. Prop.";
        }
    }

    /**
     * updates {@code basePropulsionPower} attribute in ship,
     *
     * @param ship player's {@code Ship}
     * @param x {@code int} coordinate
     * @param y {@code int} coordinate
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        if(!isDoublePropulsor){
            ship.updateBasePropulsionPower(-1);
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
        if(!isDoublePropulsor){
            ship.updateBasePropulsionPower(1);
        }
    }
}
