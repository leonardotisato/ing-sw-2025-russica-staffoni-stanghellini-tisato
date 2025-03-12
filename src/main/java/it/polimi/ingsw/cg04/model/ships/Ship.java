package it.polimi.ingsw.cg04.model.ships;

import it.polimi.ingsw.cg04.model.enumerations.*;

import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.util.*;

public class Ship {

    private final int level;
    private final int shipHeight;
    private final int shipWidth;
    private Tile[][] tilesMatrix;
    private boolean[][] validSlots;

    private final Set<Connection> connectors = new HashSet<>(Set.of(Connection.SINGLE, Connection.DOUBLE, Connection.UNIVERSAL));

    private int numHumans = 0;
    private int numAliens = 0;
    private int numBatteries = 0;
    private int numBrokenTiles = 0;
    private Map<AlienColor, Integer> aliens;
    private Map<BoxType, Integer> boxes;
    private int numExposedConnectors;
    private List<Direction> protectedDirections;
    private List<Tile> tilesBuffer;
    private double baseFirePower = 0;
    private int basePropulsionPower = 0;

    public Ship(int lev, PlayerColor playerColor) {
        this.level = lev;
        assert (level == 1 || level == 2);

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
        if (level == 2) {
            validSlots[0][3] = false;
        }

        tilesMatrix = new Tile[shipHeight][shipWidth];
        // todo: placeTile(new HousingTile(playerColor), shipHeight / 2, shipWidth / 2);
    }

    public Tile[][] getTilesMatrix() {
        return tilesMatrix;
    }

    public boolean[][] getValidSlots() {
        return validSlots;
    }

    public Tile getTile(int x, int y) {
        // todo: add exceptions
        return tilesMatrix[y][x];
    }
    public int getNumBrokenTiles() {
        return numBrokenTiles;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    /*
    public void updateNumBatteries() {
        int count = 0;
        for (int i = 0; i < shipHeight; i++) {
            for (int j = 0; j < shipWidth; j++) {
                if (tilesMatrix[i][j] != null) {
                    count = count + tilesMatrix[i][j].getNumBatteries();
                }
            }
        }
        numBrokenTiles = count;
    }
    */

    public int getNumAliens() {
        return numAliens;
    }

    /*
    public void updateNumAliens() {
        int count = 0;
        for (int i = 0; i < shipHeight; i++) {
            for (int j = 0; j < shipWidth; j++) {
                if (tilesMatrix[i][j] != null) {
                    // todo after implementing housingTile
                }
            }
        }
    }
    */

    public int getNumAliens(AlienColor color) {
        return aliens.get(color);
    }

    public int getNumHumans() {
        return numHumans;
    }

    public int getNumCrew() {
        return this.getNumHumans() + this.getNumAliens();
    }

    public int getNumExposedConnectors() {
        return numExposedConnectors;
    }

    public List<Direction> getProtectedDirections() {
        return protectedDirections;
    }

    public List<Tile> getTilesBuffer() {
        return tilesBuffer;
    }

    public void addTileInBuffer(Tile tile) {
        tilesBuffer.add(tile);
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

    public double getBaseFirePower() {
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

    public boolean isShipLegal() {

        // for all tiles, existing neighbouring tiles must have matching connector
        for (int i = 0; i < shipHeight; i++) {
            for (int j = 0; j < shipWidth; j++) {

                if (tilesMatrix[i][j] != null) {
                    Tile currTile = tilesMatrix[i][j];

                    if (i != 0 && !currTile.isValidConnection(Direction.UP, tilesMatrix[i - 1][j])) {
                        return false;
                    }

                    if (j != 0 && !currTile.isValidConnection(Direction.LEFT, tilesMatrix[i][j - 1])) {
                        return false;
                    }

                    if (i != shipHeight - 1 && !currTile.isValidConnection(Direction.DOWN, tilesMatrix[i + 1][j])) {
                        return false;
                    }

                    if (j != shipWidth - 1 && !currTile.isValidConnection(Direction.RIGHT, tilesMatrix[i][j + 1])) {
                        return false;
                    }
                }
            }
        }

        // todo: check that all propulsors are pointing backwards

        return false;
    }

    public int askNumBatteriesToUse() {
        return -1;
    }

    public int calcTotalPropulsionPower(int usedBatteries) {
        return -1;
    }

    public int calcTotalFirePower(int usedBatteries) {
        return -1;
    }

    // the method should return true if tile was broken, so state of the ship can be updated
    // k is height/width of the attack depending on dir
    public boolean handleMeteor(Direction dir, Meteor meteor, int k) {
        return true;
    }

    // the method should return true if tile was broken, so state of the ship can be updated
    // k is height/width of the attack depending on dir
    public boolean handleShot(Direction dir, Shot shot, int k) {
        return true;
    }

    public boolean placeTile(Tile tile, int x, int y) {

        // check if tile != null
        if (tile == null) {
            return false;
        }

        // check out-of-bound placement
        if (!validSlots[x][y]) {
            return false;
        }

        // check placement on top of existing tile
        if (tilesMatrix[x][y] != null) {
            return false;
        }

        // check that placed tile neighbours another tile. let pass any connection, "punish" illegal behaviours later in isShipLegal()
        boolean connectionExists = false;
        Connection currConnection, otherConnection = null;
        if (x != 0) {
            currConnection = tile.getConnection(Direction.UP);
            otherConnection = tile.getConnection(Direction.DOWN);
            if (connectors.contains(currConnection) && connectors.contains(otherConnection)) {
                connectionExists = true;
            }
        }
        if (y != 0) {
            currConnection = tile.getConnection(Direction.LEFT);
            otherConnection = tile.getConnection(Direction.RIGHT);
            if (connectors.contains(currConnection) && connectors.contains(otherConnection)) {
                connectionExists = true;
            }
        }
        if (x != this.shipHeight - 1) {
            currConnection = tile.getConnection(Direction.DOWN);
            otherConnection = tile.getConnection(Direction.UP);
            if (connectors.contains(currConnection) && connectors.contains(otherConnection)) {
                connectionExists = true;
            }
        }
        if (y != this.shipWidth - 1) {
            currConnection = tile.getConnection(Direction.RIGHT);
            otherConnection = tile.getConnection(Direction.LEFT);
            if (connectors.contains(currConnection) && connectors.contains(otherConnection)) {
                connectionExists = true;
            }
        }

        if (!connectionExists) {
            return false;
        }

        tilesMatrix[x][y] = tile;

        return true;
    }

    public void breakTile(Integer x, Integer y) {
        this.getTile(x, y).broken(this);
        this.tilesMatrix[x][y] = null;
    }

    public void updateBatteries(int i){
        numBatteries = numBatteries + i;
    }

    public void updateBaseFirePower(double i){
        baseFirePower = baseFirePower + i;
    }

    public void updateBasePropulsionPower(int i){
        basePropulsionPower = basePropulsionPower + i;
    }

    // todo: check if works
    public void removeProtectedDirections(Set<Direction> dir){
        for(Direction d : dir){
            for(Direction direction : protectedDirections){
                if(d.equals(direction)){
                    protectedDirections.remove(d);
                    break;
                }
            }
        }
    }

    public void removeBoxes(Map<BoxType, Integer> boxesToRemove){
        // for each key remove as many elements are there are in boxesToRemove
        for(BoxType type : BoxType.values()){
            boxes.put(type, boxes.get(type) - boxesToRemove.get(type));
        }
    }
}
