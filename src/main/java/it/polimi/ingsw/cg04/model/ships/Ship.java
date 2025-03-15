package it.polimi.ingsw.cg04.model.ships;

import it.polimi.ingsw.cg04.model.enumerations.*;

import it.polimi.ingsw.cg04.model.tiles.Tile;
import java.util.HashSet;

import java.util.*;

public class Ship {

    private final int level;
    private final PlayerColor color;
    private final int shipHeight;
    private final int shipWidth;
    private Tile[][] tilesMatrix;
    private boolean[][] validSlots;

    private final Set<Connection> connectors = new HashSet<>(Set.of(Connection.SINGLE, Connection.DOUBLE, Connection.UNIVERSAL));

    private int numBatteries = 0;
    private int numBrokenTiles = 0;
    private int numExposedConnectors;
    private double baseFirePower = 0;
    private int basePropulsionPower = 0;

    private Map<CrewType, Integer> crewMap;
    private Map<BoxType, Integer> boxes;
    private List<Direction> protectedDirections;
    private List<Tile> tilesBuffer;


    public Ship(int lev, PlayerColor playerColor) {
        this.level = lev;
        this.color = playerColor;
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

    // returns Tile in slot (x,y) and null if no tile is found in (x,y)
    public Tile getTile(int x, int y) {

        if (x < 0 || y < 0 || x >= this.shipHeight || y >= this.shipWidth) {
            throw new IllegalArgumentException("Requested slot is out of bounds!");
        }

        if (!validSlots[x][y]) {
            throw new IllegalArgumentException("Requested slot is not a valid slot!");
        }

        return tilesMatrix[x][y];
    }

    public int getNumBrokenTiles() {
        return numBrokenTiles;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public int getNumAliens() {
        return crewMap.get(CrewType.PINK_ALIEN) + crewMap.get(CrewType.BROWN_ALIEN);
    }

    public int getNumCrewByType(CrewType type) {
        return crewMap.get(type);
    }

    public int getNumCrew() {
        return crewMap.get(CrewType.PINK_ALIEN) + crewMap.get(CrewType.BROWN_ALIEN) + crewMap.get(CrewType.HUMAN);
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

    public void removeCrewByType(CrewType type) {

        // todo: handle this gracefully
        assert crewMap.containsKey(type);
        assert crewMap.get(type) > 0;

        crewMap.put(type, this.getNumCrewByType(type) - 1);
    }

    public void removeBatteries(int lostBatteries) {
        assert this.getNumBatteries() - lostBatteries >= 0;
        numBatteries -= lostBatteries;
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
        this.updateExposedConnectors();
    }

    public void updateBaseFirePower(double i) {
        baseFirePower = baseFirePower + i;
    }

    public void updateBasePropulsionPower(int i) {
        basePropulsionPower = basePropulsionPower + i;
    }

    // todo: check if works
    public void removeProtectedDirections(Set<Direction> dir) {
        for (Direction d : dir) {
            for (Direction direction : protectedDirections) {
                if (d.equals(direction)) {
                    protectedDirections.remove(d);
                    break;
                }
            }
        }
    }

    public void updateExposedConnectors(){
        int cont = 0;
        // set of connections that increases the count, used to make code more readable
        Set<Connection> connectors = new HashSet<>(Set.of(Connection.SINGLE, Connection.DOUBLE, Connection.UNIVERSAL));
        for(int i = 0; i<shipHeight; i++) {
            for(int j = 0; j<shipWidth; j++) {
                if (tilesMatrix[i][j] != null) {

                    if (i == 0) {
                        if (connectors.contains(tilesMatrix[i][j].getConnection(Direction.UP))){cont++;}
                    }
                    else {
                        if (tilesMatrix[i-1][j] == null && connectors.contains(tilesMatrix[i][j].getConnection(Direction.UP))){cont++;}
                    }

                    if (i == shipHeight - 1) {
                        if(connectors.contains(tilesMatrix[i][j].getConnection(Direction.DOWN))){cont++;}
                    }
                    else {
                        if(tilesMatrix[i+1][j] == null && connectors.contains(tilesMatrix[i][j].getConnection(Direction.DOWN))){cont++;}
                    }

                    if (j == 0) {
                        if(connectors.contains(tilesMatrix[i][j].getConnection(Direction.LEFT))){cont++;}
                    }
                    else {
                        if(tilesMatrix[i][j-1] == null && connectors.contains(tilesMatrix[i][j].getConnection(Direction.LEFT))){ cont++; }
                    }

                    if (j == shipWidth - 1) {
                        if(connectors.contains(tilesMatrix[i][j].getConnection(Direction.RIGHT))) { cont++; }
                    } else {
                        if(tilesMatrix[i][j+1] == null && connectors.contains(tilesMatrix[i][j].getConnection(Direction.RIGHT))){ cont++; }
                    }
                }
            }
        }
        this.numExposedConnectors = cont;
    }
}
