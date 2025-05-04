package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Ship;

public class PropulsorTile extends Tile {

    @Expose
    private boolean isDoublePropulsor;

    public PropulsorTile() {
        super();
        if (isDoublePropulsor) {
            this.shortName = "DP";
        } else {
            this.shortName = "SP";
        }

    }

    @Override
    public Boolean isDoublePropulsor() {
        return isDoublePropulsor;
    }

    /**
     * updates {@code basePropulsionPower} attribute in ship,
     *
     * @param ship
     * @param x
     * @param y
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        if(!isDoublePropulsor){
            ship.updateBasePropulsionPower(-1);
        }
    }

    @Override
    public void place(Ship ship, int x, int y) {
        if(!isDoublePropulsor){
            ship.updateBasePropulsionPower(1);
        }
    }
}
