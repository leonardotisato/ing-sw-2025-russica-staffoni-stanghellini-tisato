package it.polimi.ingsw.cg04.model.ships;
import it.polimi.ingsw.cg04.model.enumerations.*;

import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.util.Map;
import java.util.List;
import java.util.Comparator;

public class Ship {

    private final int level;
    private final int shipHeight;
    private final int shipWidth;
    private Tile[][] tilesMatrix;
    private boolean[][] validSlots;

    private int numHumans = 0;
    private int numAliens = 0;
    private int numBatteries = 0;
    private int numBrokenTiles = 0;
    private Map<AlienColor, Integer> aliens;
    private Map<BoxType, Integer> boxes;
    private int numExposedConnectors;
    private List<Direction> protectedDirections;
    private Tile[] tilesBuffer;
    private int baseFirePower = 0;
    private int basePropulsionPower = 0;

    public Ship(int lev, PlayerColor playerColor) {
        this.level = lev;
        assert(level == 1 || level == 2);

        if (level == 1) {
            shipWidth = 5;
            shipHeight = 5;
        } else {
            shipWidth = 7;
            shipHeight = 5;
        }

        validSlots = new boolean[shipHeight][shipWidth];
        for (int i = 0; i < shipHeight; i++) {
            for (int j = 0; j < shipWidth; j++) {
                validSlots[i][j] = true;
            }
        }

        // common invalid slot positions
        validSlots[0][0] = false;
        validSlots[0][1] = false;
        validSlots[1][0] = false;

        validSlots[0][shipWidth - 1] = false;
        validSlots[0][shipHeight - 2] = false;
        validSlots[1][shipWidth - 1] = false;

        validSlots[shipHeight - 1][shipWidth / 2] = false;
        if (level == 2) { validSlots[0][3] = false; }

        tilesMatrix = new Tile[shipHeight][shipWidth];
        // todo: placeTile(new HousingTile(playerColor), shipHeight / 2, shipWidth / 2);
    }


    public int getNumBrokenTiles() {
        return numBrokenTiles;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public int getNumAliens() {
        return numAliens;
    }

    public int getNumAliens(AlienColor color) {
        return aliens.get(color);
    }

    public int getNumHumans() {
        return numHumans;
    }

    public  int getNumCrew() {
        return this.getNumHumans() + this.getNumAliens();
    }

    public int getNumExposedConnectors() {
        return numExposedConnectors;
    }

    public List<Direction> getProtectedDirections() {
        return protectedDirections;
    }

    public Tile[] getTilesBuffer() {
        return tilesBuffer;
    }

    public Map<AlienColor, Integer> getAliens() {
        return aliens;
    }

    // do we need this?
    public Map<BoxType, Integer> getBoxes() {
        return boxes;
    }

    public int getBoxes(BoxType type) {
        return boxes.get(type);
    }

    public int getBaseFirePower() {
        return baseFirePower;
    }

    public int getBasePropulsionPower() {
        return basePropulsionPower;
    }

    public void removeHumans(int lostHumans) {

        // todo: handle this gracefully
        assert this.getNumHumans() - lostHumans >= 0;

        numHumans -= lostHumans;
    }

    public void removeBatteries(int lostBatteries) {
        assert this.getNumBatteries() - lostBatteries >= 0;
        numBatteries -= lostBatteries;
    }

    public void removeAliens(AlienColor color, int lostAliens) {
        numAliens -= lostAliens;

        // todo: handle this gracefully
        assert aliens.get(color) != null;
        assert this.getNumAliens(color) - lostAliens >= 0;

        aliens.put(color, aliens.get(color) - lostAliens);
    }

    // this method remove 'lostBoxes' boxes, prioritizing high value boxes, then removes battery. As stated in the game rules
    // todo: this method only updates the new boxes map, does not remove box from a storageTile!!
    public void removeBoxes(int lostBoxes) {
        int yetToRemove = lostBoxes;
        List<BoxType> sortedTypes = this.boxes.keySet().stream().sorted(Comparator.comparing(BoxType::getPriority).reversed()).toList();

        for (BoxType type : sortedTypes) {
            if (yetToRemove > 0) {
                yetToRemove -= this.removeBoxes(type, yetToRemove);
            }
        }

        if (yetToRemove > 0) {
            this.removeBatteries(yetToRemove);
        }
    }

    // returns the number of boxes that were removed
    // todo: add storageTile location from the matrix to remove boxes from the tile as well?
    public int removeBoxes(BoxType type, int lostBoxes) {
        Integer currentCount = this.boxes.get(type);

        if (currentCount == null) {
            return 0;
        }

        if (currentCount >= lostBoxes) {
            this.boxes.put(type, currentCount - lostBoxes);
            return lostBoxes;
        }

        this.boxes.put(type, 0);
        return currentCount;
    }

    public boolean isShipLegal(){
        return false;
    }

    public int askNumBatteriesToUse() { return -1; }

    public int calcTotalPropulsionPower(int usedBatteries) {
        return -1;
    }

    public int calcTotalFirePower(int usedBatteries) {
        return -1;
    }

    // the method should return true if tile was broken, so state of the ship can be updated
    // k is height/width of the attack depending on dir
    public boolean handleMeteor(Direction dir, Meteor meteor, int k) {
        return  true;
    }

    // the method should return true if tile was broken, so state of the ship can be updated
    // k is height/width of the attack depending on dir
    public boolean handleShot(Direction dir, Shot shot, int k) {
        return true;
    }

    public boolean placeTile(Tile Tile, int x, int y) {

        // check out-of-bound placement
        if (!validSlots[x][y]) {
            return false;
        }

        // check placement on top of existing tile
        if (tilesMatrix[x][y] != null) {
            return false;
        }


        return true;
    }
}
