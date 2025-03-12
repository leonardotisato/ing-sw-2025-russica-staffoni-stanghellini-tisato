package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.ships.Ship;

public class PropulsorTile extends Tile {

    private final boolean isDoublePropulsor;

    public PropulsorTile(boolean isDoublePropulsor) {
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
