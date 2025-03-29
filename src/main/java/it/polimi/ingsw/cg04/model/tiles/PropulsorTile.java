package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Ship;

public class PropulsorTile extends Tile {

    @Expose
    private boolean isDoublePropulsor;

    public PropulsorTile() {
        super();
    }

    @Override
    public Boolean isDoublePropulsor() {
        return isDoublePropulsor;
    }
    public void setDoublePropulsor(boolean isDoublePropulsor) {
        this.isDoublePropulsor = isDoublePropulsor;
    }

    @Override
    public void broken(Ship ship){
        if(!isDoublePropulsor){
            ship.updateBasePropulsionPower(-1);
        }
    }

    @Override
    public void place(Ship ship) {
        if(!isDoublePropulsor){
            ship.updateBasePropulsionPower(1);
        }
    }
}
