package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.*;

import it.polimi.ingsw.cg04.model.tiles.LaserTile;
import it.polimi.ingsw.cg04.model.tiles.PropulsorTile;
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
    private List<Tile> tilesBuffer = new ArrayList<>();


    public Ship(int lev, PlayerColor playerColor) {
        this.level = lev;
        this.color = playerColor;
        assert (level == 1 || level == 2);

        protectedDirections = new ArrayList<>();
        tilesBuffer = new ArrayList<>();

        crewMap = new HashMap<>();
        crewMap.put(CrewType.HUMAN, 0);
        crewMap.put(CrewType.PINK_ALIEN, 0);
        crewMap.put(CrewType.BROWN_ALIEN, 0);

        boxes = new HashMap<>();
        boxes.put(BoxType.BLUE, 0);
        boxes.put(BoxType.GREEN, 0);
        boxes.put(BoxType.YELLOW, 0);
        boxes.put(BoxType.RED, 0);

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


    // ship structure and tiles management
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

    public boolean placeTile(Tile tile, int x, int y) {

        if (x < 0 || y < 0 || x >= this.shipHeight || y >= this.shipWidth) {
            throw new IllegalArgumentException("Requested slot is out of bounds!");
        }
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

        /*
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
        */

        tilesMatrix[x][y] = tile;   // place tile in the ship
        tile.place(this);   // update resources and tiles params
        updateExposedConnectors();  // update exposedConnectors attribute

        return true;
    }

    public void breakTile(Integer x, Integer y) {
        if (x < 0 || y < 0 || x >= this.shipHeight || y >= this.shipWidth) { throw new IllegalArgumentException("Requested slot is out of bounds!"); }
        if (!validSlots[x][y]) { throw new IllegalArgumentException("Requested slot is out of bounds!"); }
        if(tilesMatrix[x][y] == null) { throw new IllegalArgumentException("Requested slot is not a valid slot!"); }

        this.getTile(x, y).broken(this);
        this.tilesMatrix[x][y] = null;
        this.updateExposedConnectors();
    }

    public List<Tile> getTilesBuffer() {
        return tilesBuffer;
    }

    public void addTileInBuffer(Tile tile) {
        tilesBuffer.add(tile);
    }

    public Tile takeFromTileBuffer(int index) {

        if (index < 0 || index >= this.tilesBuffer.size()) {
            throw new IllegalArgumentException("Requested slot is out of bounds!");
        }

        if (tilesMatrix[index] == null) {
            throw new IllegalArgumentException("Requested slot is not a valid slot!");
        }

        return tilesBuffer.remove(index);
    }

    // ship resources
    public int getNumBatteries() {
        return numBatteries;
    }

    public int getNumCrewByType(CrewType type) {
        return crewMap.get(type);
    }

    public int getNumCrew() {
        return crewMap.get(CrewType.PINK_ALIEN) + crewMap.get(CrewType.BROWN_ALIEN) + crewMap.get(CrewType.HUMAN);
    }

    public int getNumAliens() {
        return crewMap.get(CrewType.PINK_ALIEN) + crewMap.get(CrewType.BROWN_ALIEN);
    }

    // do we need this?
    public Map<BoxType, Integer> getBoxes() {
        return boxes;
    }

    public int getBoxes(BoxType type) {
        return boxes.get(type);
    }

    public void removeBatteries(int lostBatteries) {
        assert this.getNumBatteries() - lostBatteries >= 0;
        numBatteries -= lostBatteries;
    }

    public void addBatteries(int newBatteries) {
        assert this.getNumBatteries() + newBatteries >= 0;
        numBatteries += newBatteries;
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

    // needed for player choice to remove single box
    public void removeBox(BoxType boxType, int x, int y) {
        // exception are already in tile methode
        getTile(x, y).removeBox(boxType);   // remove box from tile's map

        if(boxes.get(boxType) <= 0) {
            throw new RuntimeException("Illegal Operation! No boxes to remove!");
        }
        boxes.put(boxType, boxes.get(boxType) - 1); // remove from ship's map
    }

    // needed for player choice to add single box
    public void addBox(BoxType boxType, int x, int y) {
        // exception are already in tile methode
        getTile(x, y).addBox(boxType);  // add box to tile's map

        boxes.put(boxType, boxes.get(boxType) + 1); // add box to ship's map
    }

    public void removeCrewByType(CrewType type) {

        // non bellissimo ma potrebbe dover essere necessario in caso di rimozione tile
        if(type == null) { return; }

        // todo: handle this gracefully
        assert crewMap.containsKey(type);
        assert crewMap.get(type) > 0;

        crewMap.put(type, this.getNumCrewByType(type) - 1);
    }

    public void addCrewByType(CrewType type) {
        // todo: may needs exceptions
        crewMap.put(type, this.getNumCrewByType(type) + 1);
    }


    // stats
    public int getNumExposedConnectors() {
        return numExposedConnectors;
    }

    public List<Direction> getProtectedDirections() {
        return protectedDirections;
    }

    public double getBaseFirePower() {
        return baseFirePower;
    }

    public int getBasePropulsionPower() {
        return basePropulsionPower;
    }

    public int calcTotalPropulsionPower(int usedBatteries) {
        return -1;
    }

    public int calcTotalFirePower(int usedBatteries) {
        return -1;
    }

    public void updateBaseFirePower(double i) {
        baseFirePower = baseFirePower + i;
    }

    public void updateBasePropulsionPower(int i) {
        basePropulsionPower = basePropulsionPower + i;
    }

    public void updateExposedConnectors(){
        int cont = 0;
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

    public void addProtectedDirections(Set<Direction> dir) {
        protectedDirections.addAll(dir);
    }

    public boolean isShipLegal() {

        // for all tiles, existing neighbouring tiles must have matching connector
        for (int i = 0; i < shipHeight; i++) {
            for (int j = 0; j < shipWidth; j++) {

                if (tilesMatrix[i][j] != null) {
                    Tile currTile = tilesMatrix[i][j];

                    // check you don't have a tile directly under a propulsor and check propulsor is pointing down
                    if (currTile instanceof PropulsorTile && (currTile.getConnection(Direction.DOWN) != Connection.PROPULSOR  || (i != shipHeight - 1 && tilesMatrix[i+1][j] != null))) {
                        return false;
                    }

                    // check that you don't have a tile directly in front of the gun
                    if (currTile instanceof LaserTile && (i!=0 && tilesMatrix[i-1][j] == null && currTile.getConnection(Direction.UP) == Connection.GUN)
                            || (i != shipHeight - 1 && tilesMatrix[i+1][j] == null && currTile.getConnection(Direction.DOWN) == Connection.GUN)
                            || (j != 0 && tilesMatrix[i][j-1] == null && currTile.getConnection(Direction.LEFT) == Connection.GUN)
                            || (j != shipWidth - 1 && tilesMatrix[i][j+1] == null) && currTile.getConnection(Direction.RIGHT) == Connection.GUN) {
                        return false;
                    }

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
        return isShipConnectedBFS();
    }

    // todo: testing
    public boolean isShipConnectedBFS(){
        int totalTiles = 0;
        int visitedTiles = 0;
        boolean[][] visited = new boolean[shipHeight][shipWidth];

        // find a tile to start
        int startX = -1;
        int startY = -1;
        for(int i = 0; i<shipHeight; i++) {
            for(int j = 0; j<shipWidth; j++) {
                if (tilesMatrix[i][j] != null) {
                    totalTiles++;
                    if(startX == -1 && startY == -1) {
                        startX = i;
                        startY = j;
                    }
                }
            }
        }

        // il bro ha una nave con zero componenti e non si è ancora ritirato
        if (totalTiles == 0) { return true; }

        // count reachable tiles
        List<int[]> list = new ArrayList<>();
        list.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!list.isEmpty()) {
            int[] pos = list.removeFirst();
            int x = pos[0];
            int y = pos[1];
            visitedTiles++;

            Tile currTile = tilesMatrix[x][y];

            for(Direction direction: Direction.values()) {
                int newX = x;
                int newY = y;

                switch(direction) {
                    case UP -> newX--;
                    case DOWN -> newX++;
                    case LEFT -> newY--;
                    case RIGHT -> newY++;
                }

                if(newX >= 0 && newX < shipWidth && newY >= 0 && newY < shipHeight) {
                    if(!visited[newX][newY] && tilesMatrix[newX][newY] != null && currTile.isValidConnection(direction, tilesMatrix[newX][newY])) {
                        list.add(new int[]{newX, newY});
                        visited[newX][newY] = true;
                    }
                }
            }
        }
        return visitedTiles == totalTiles;
    }

    // // todo: metodo non fininto, fare distinzioni tra LIGHT e HEAVY e tipo di GAME
    // the method should return true if tile was broken, so state of the ship can be updated
    // k is height/width of the attack depending on dir
    public boolean handleMeteor(Direction dir, Meteor meteor, int k) {
        if(k < 0 || k > shipWidth){
            return false;
        }

        if (dir == Direction.UP) {
            for (int i = 0; i < shipHeight; i++) {
                if(tilesMatrix[i][k] != null) {
                    breakTile(i, k);
                    return true;
                }
            }
        }
        if (dir == Direction.LEFT) {
            for (int i = 0; i < shipWidth; i++) {
                if(tilesMatrix[k][i] != null) {
                    breakTile(k, i);
                    return true;
                }
            }
        }
        if (dir == Direction.RIGHT) {
            for (int i = 0; i < shipWidth; i++) {
                if(tilesMatrix[k][shipWidth-1-i] != null) {
                    breakTile(k, shipWidth-1-i);
                    return true;
                }
            }
        }
        if (dir == Direction.DOWN) {
            for (int i = 0; i < shipHeight; i++) {
                if(tilesMatrix[shipHeight-1-i][k] != null) {
                    breakTile(shipHeight-1-i, k);
                    return true;
                }
            }
        }

        // todo: non penso serva ritornare valori visto come abbiamo implementato breakTile
        return false;
    }

    // todo: metodo non fininto, fare distinzioni tra LIGHT e HEAVY
    // the method should return true if tile was broken, so state of the ship can be updated
    // k is height/width of the attack depending on dir
    public boolean handleShot(Direction dir, Shot shot, int k) {
        if(k < 0 || k > shipWidth){
            return false;
        }

        if (dir == Direction.UP) {
            for (int i = 0; i < shipHeight; i++) {
                if(tilesMatrix[i][k] != null) {
                    breakTile(i, k);
                    return true;
                }
            }
        }
        if (dir == Direction.LEFT) {
            for (int i = 0; i < shipWidth; i++) {
                if(tilesMatrix[k][i] != null) {
                    breakTile(k, i);
                    return true;
                }
            }
        }
        if (dir == Direction.RIGHT) {
            for (int i = 0; i < shipWidth; i++) {
                if(tilesMatrix[k][shipWidth-1-i] != null) {
                    breakTile(k, shipWidth-1-i);
                    return true;
                }
            }
        }
        if (dir == Direction.DOWN) {
            for (int i = 0; i < shipHeight; i++) {
                if(tilesMatrix[shipHeight-1-i][k] != null) {
                    breakTile(shipHeight-1-i, k);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {

        // formatted grid for tilesMatrix with coordinates and tile details
        StringBuilder tilesMatrixGrid = new StringBuilder();
        tilesMatrixGrid.append("TilesMatrix Grid:\n");

        // print the coordinate grid
        for (int y = 0; y < tilesMatrix.length; y++) {
            for (int x = 0; x < tilesMatrix[y].length; x++) {
                if (this.validSlots[y][x]) {
                    tilesMatrixGrid.append(String.format("(%d,%d) ", x, y));
                }
                else {
                    tilesMatrixGrid.append("(X,X) ");
                }
            }
            tilesMatrixGrid.append("\n");
        }

        // print the tile details for each coordinate
        tilesMatrixGrid.append("\nTile Details:\n");
        for (int y = 0; y < tilesMatrix.length; y++) {
            for (int x = 0; x < tilesMatrix[y].length; x++) {
                if (tilesMatrix[y][x] != null) {
                    tilesMatrixGrid.append(String.format("(%d,%d): %s\n", x, y, tilesMatrix[y][x]));
                }
            }
        }

        return "Ship{" +
                "level=" + level +
                ", color=" + color +
                ", shipHeight=" + shipHeight +
                ", shipWidth=" + shipWidth +
                ", tilesBuffer=" + tilesBuffer +
                ", \n" + tilesMatrixGrid +
                "\nShip parameters:" +
                " numBatteries=" + numBatteries +
                ", numExposedConnectors=" + numExposedConnectors +
                ", numBrokenTiles=" + numBrokenTiles +
                ", baseFirePower=" + baseFirePower +
                ", basePropulsionPower=" + basePropulsionPower +
                ", crewMap=" + crewMap +
                ", boxes=" + boxes +
                ", protectedDirections=" + protectedDirections +
                '}';
    }
}
