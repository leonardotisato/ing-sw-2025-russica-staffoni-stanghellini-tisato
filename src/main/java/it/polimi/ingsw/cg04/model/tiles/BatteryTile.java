package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.ships.Ship;

public class BatteryTile extends Tile {

    private final int maxBatteryCapacity;
    private int numBatteries;

    public BatteryTile(int maxBatteryCapacity) {
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
        if (this.numBatteries < num) {
            System.out.println("Illegal operation: avail batteries" + this.numBatteries + " taken batteries: " + num);
            throw new RuntimeException("Illegal operation");
        }

        this.numBatteries -= num;
        assert this.numBatteries >= 0;
    }

    @Override
    public void broken(Ship ship){
        ship.updateBatteries(-numBatteries);
    }
}
