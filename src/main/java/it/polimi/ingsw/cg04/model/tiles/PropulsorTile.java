package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.Map;

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
}
