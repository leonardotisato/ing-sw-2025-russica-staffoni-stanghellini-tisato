package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Ship;

public class BatteryTile extends Tile {
    @Expose
    private int maxBatteryCapacity;
    @Expose
    private int numBatteries;

    public BatteryTile() {
        super();
    }

    public Integer getMaxBatteryCapacity() {
        return this.maxBatteryCapacity;
    }
    public void setMaxBatteryCapacity(Integer maxBatteryCapacity) {
        this.maxBatteryCapacity = maxBatteryCapacity;
    }

    public Integer getNumBatteries() {
        return numBatteries;
    }
    public void setNumBatteries(Integer numBatteries) {
        this.numBatteries = numBatteries;
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
    public void broken(Ship ship, int x, int y) {
        ship.removeBatteries(numBatteries, x, y);
        //ship.removeBatteries(numBatteries);
        //this.numBatteries = 0;
    }

    @Override
    public void place(Ship ship, int x, int y){
        this.numBatteries = this.maxBatteryCapacity;
        ship.addBatteries(numBatteries);
    }
}
