package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.Map;

public class BatteryTile extends Tile {

    private final int maxBatteryCapacity;
    private int numBatteries;

    public BatteryTile(Map<Direction, Connection> connectionMap, int maxBatteryCapacity) {
        super(connectionMap);
        this.maxBatteryCapacity = maxBatteryCapacity;
        this.numBatteries = this.maxBatteryCapacity;
    }

    public Integer getMaxBatteryCapacity() {
        return this.maxBatteryCapacity;
    }

    public Integer getNumBatteries() {
        return numBatteries;
    }

    public void removeBatteries(Integer num) {
        if (num <= 0) {
            throw new IllegalArgumentException("Battery number must be greater than zero");
        }
        if (this.numBatteries < num) {
            System.out.println("Illegal operation: avail batteries" + this.numBatteries + " taken batteries: " + num);
            throw new RuntimeException("Illegal operation");
        }

        this.numBatteries -= num;
    }

    @Override
    public void broken(Ship ship){
        ship.removeBatteries(numBatteries);
    }
}
