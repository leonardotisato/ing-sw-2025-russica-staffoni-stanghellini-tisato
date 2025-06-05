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
        this.shortName = "Battery";
        this.tileColor = "\u001B[32m";
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

    /**
     * removes batteries from tile and ship {@code numBatteries} attribute,
     * the {@code x} and {@code y} attributes are needed to simplify the place/remove process
     *
     * @param ship player's ship
     * @param x coordinate
     * @param y coordinate
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        ship.removeBatteries(numBatteries, x, y);
    }

    /**
     * updates attribute {@code numBatteries} of the ship and this tile,
     * the {@code x} and {@code y} attributes are needed to simplify the place/remove process
     *
     * @param ship player's ship
     * @param x coordinate
     * @param y coordinate
     */
    @Override
    public void place(Ship ship, int x, int y) {
        this.numBatteries = this.maxBatteryCapacity;
        ship.addBatteries(numBatteries);
    }

    @Override
    void drawContent(StringBuilder sb) {
        String availBatteries = String.format("%d/%d", numBatteries, maxBatteryCapacity);
        sb.append("│ ").append(centerText(availBatteries, boxWidth - 4)).append(" │").append("\n");
    }
}
