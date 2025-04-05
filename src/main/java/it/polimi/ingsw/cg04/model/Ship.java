package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.*;

import it.polimi.ingsw.cg04.model.tiles.*;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.HashSet;

import java.util.*;

public class Ship {

    private final int level;
    private final PlayerColor color;
    private final int shipHeight;
    private final int shipWidth;
    private final Tile[][] tilesMatrix;
    private final boolean[][] validSlots;

    private final Set<Connection> connectors = new HashSet<>(Set.of(Connection.SINGLE, Connection.DOUBLE, Connection.UNIVERSAL));

    private int numBatteries = 0;
    private int numBrokenTiles = 0;
    private int numExposedConnectors;
    private double baseFirePower = 0;
    private int basePropulsionPower = 0;

    private final Map<String, List<Coordinates>> tilesMap;
    private final Map<CrewType, Integer> crewMap;
    private final Map<BoxType, Integer> boxes;
    private final List<Direction> protectedDirections;
    private final List<Tile> tilesBuffer;


    public Ship(int lev, PlayerColor playerColor) {
        this.level = lev;
        this.color = playerColor;
        assert (level == 1 || level == 2);

        tilesMap = new HashMap<>();
        tilesMap.put("AlienSupportTile", new ArrayList<>());
        tilesMap.put("BatteryTile", new ArrayList<>());
        tilesMap.put("HousingTile", new ArrayList<>());
        tilesMap.put("LaserTile", new ArrayList<>());
        tilesMap.put("PropulsorTile", new ArrayList<>());
        tilesMap.put("ShieldTile", new ArrayList<>());
        tilesMap.put("StorageTile", new ArrayList<>());
        tilesMap.put("StructuralTile", new ArrayList<>());

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
        validSlots[0][shipWidth - 2] = false;
        validSlots[1][shipWidth - 1] = false;

        validSlots[shipHeight - 1][shipWidth / 2] = false;
        if (level == 2) {
            validSlots[0][3] = false;
        }

        tilesMatrix = new Tile[shipHeight][shipWidth];

        placeTile(new HousingTile(playerColor), shipHeight / 2, shipWidth / 2);

    }

    public int getLevel() {
        return level;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    // ship structure and tiles management
    public Tile[][] getTilesMatrix() {
        return tilesMatrix;
    }

    public boolean[][] getValidSlots() {
        return validSlots;
    }

    public int getNumBrokenTiles() {
        return numBrokenTiles;
    }

    public int getShipWidth(){
        return shipWidth;
    }

    public int getShipHeight(){
        return shipHeight;
    }

    public Map<String, List<Coordinates>> getTilesMap(){
        return tilesMap;
    }

    /**
     * checks if the indexes are inside the matrix
     *
     * @param x row
     * @param y column
     * @return {@code true} if indexes out of bounds {@code false} if inbound
     */
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= this.shipHeight || y >= this.shipWidth;
    }

    /**
     * returns Tile in slot {@code x, y} and {@code null} if no tile is found in (x,y)
     *
     * @param x row
     * @param y column
     * @return the Tile in position {@code x, y} if present, {@code null} otherwise
     * @throws IllegalArgumentException if the indexes are not valid
     */
    public Tile getTile(int x, int y) {

        if (isOutOfBounds(x, y)) {
            throw new IllegalArgumentException("Requested slot is out of bounds!");
        }

        if (!validSlots[x][y]) {
            throw new IllegalArgumentException("Requested slot is not a valid slot!");
        }

        return tilesMatrix[x][y];
    }

    /**
     * places a tile in a specific slot and calls {@code tile.place(...)}
     * which takes care of updating ship and tile attributes
     * lastly it updates exposedConnectors
     *
     *
     * @param tile tile to place
     * @param x row
     * @param y column
     * @return {@code true} if the slot {@code x, y} is valid and empty and the tile is not {@code null},
     * @throws IllegalArgumentException if the {@code x, y} indexes are out of bounds
     * {@code false} if slot is invalid or already taken or the selected tile is {@code null}
     */
    public boolean placeTile(Tile tile, int x, int y) {
        if (isOutOfBounds(x, y)) {
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

        tilesMatrix[x][y] = tile;   // place tile in the ship
        tile.place(this, x, y);   // update resources and tiles params
        this.addTileToMap(tile.getType(), x, y); // adds tile to tile's map
        updateExposedConnectors();  // update exposedConnectors attribute

        return true;
    }

    /**
     * breaks all the tiles of the ship, done for simplify testing
     */
    public void breakAllTiles(){
        for (int i = 0; i < tilesMatrix.length; i++) {
            for (int j = 0; j < tilesMatrix[i].length; j++) {
                if(tilesMatrix[i][j] != null){
                    breakTile(i, j);
                }
            }
        }
    }

    /**
     * removes the tile in the {@code x, y} slot and calls {@code tile.broken(...)}
     * which takes care of updating ship and tile attributes
     * lastly it updates exposedConnectors
     *
     * @param x row
     * @param y column
     * @throws IllegalArgumentException if hte slot is not valid or if there is not a tile in the slot
     */
    public void breakTile(int x, int y) {
        if (isOutOfBounds(x, y)) { throw new IllegalArgumentException("Requested slot is out of bounds!"); }
        if (!validSlots[x][y]) { throw new IllegalArgumentException("Requested slot is out of bounds!"); }
        if(tilesMatrix[x][y] == null) { throw new IllegalArgumentException("Requested slot is already empty!"); }

        this.getTile(x, y).broken(this, x, y);
        this.removeTileFromMap(getTile(x, y).getType(), x, y);
        this.tilesMatrix[x][y] = null;
        this.updateExposedConnectors();
        this.numBrokenTiles++;
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

    /**
     * adds tile's coordinates of a  specific type to the specific list
     *
     * @param type key of the {@code tilesMap} Map
     * @param x row
     * @param y column
     */
    public void addTileToMap(String type, int x, int y) {
        // add coords to correct list
        tilesMap.get(type).add(new Coordinates(x, y));
    }

    /**
     * removes tile's coordinates of a specific type to the specific list
     *
     * @param type key of the {@code tilesMap} Map
     * @param x row
     * @param y column
     */
    public void removeTileFromMap(String type, int x, int y) {
        List<Coordinates> list = tilesMap.get(type);
        list.removeIf(tile -> tile.equals(new Coordinates(x, y)));
    }
    /**
     * removes {@code int lostBatteries} from ship attribute {@code numBatteries}
     *
     * @param lostBatteries number of batteries to remove
     */
    public void removeBatteries(int lostBatteries) {
        assert this.getNumBatteries() - lostBatteries >= 0;
        numBatteries -= lostBatteries;
    }

    /**
     * removes {@code int lostBatteries} from ship attribute {@code numBatteries}
     * and from tile attribute {@code numBatteries}
     *
     * @param usedBatteries number of batteries to remove
     * @param x row
     * @param y column
     */
    public void removeBatteries(int usedBatteries, int x, int y) {
        if (numBatteries < usedBatteries) {
            throw new IllegalArgumentException("non enough batteries!");
        }
        numBatteries -= usedBatteries;
        getTile(x, y).removeBatteries(usedBatteries);
    }

    /**
     * adds {@code int newBatteries} to ship attribute {@code numBatteries}
     *
     * @param newBatteries number of batteries to add
     */
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

    /**
     * remove {@code int lostBoxes} boxes of type {@code type} from ship attribute {@code boxes}
     *
     * @param type the type of box to remove
     * @param lostBoxes the number of boxes to remove
     * @return the number of boxes removed
     */
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

    /**
     * removes a single box of type {@code boxType} from ship attribute {@code boxes}
     * and from tile attribute {@code boxes}
     *
     * @param boxType type of the box to remove
     * @param x row
     * @param y column
     * @throws RuntimeException if there are no boxes to be removed from {@code ship.boxes}
     */
    public void removeBox(BoxType boxType, int x, int y) {
        // exception are already in tile methode
        getTile(x, y).removeBox(boxType, 1);   // remove box from tile's map

        if(boxes.get(boxType) <= 0) {
            throw new RuntimeException("Illegal Operation! No boxes to remove!");
        }
        boxes.put(boxType, boxes.get(boxType) - 1); // remove from ship's map
    }

    /**
     * adds a single box of type {@code boxType} to ship attribute {@code boxes}
     * and to tile attribute {@code boxes}
     *
     * @param boxType type of the box to add
     * @param x row
     * @param y column
     */
    public void addBox(BoxType boxType, int x, int y) {
        // exception are already in tile methode
        getTile(x, y).addBox(boxType, 1);  // add box to tile's map

        boxes.put(boxType, boxes.get(boxType) + 1); // add box to ship's map
    }

    /**
     * updates {@code ship.boxes} and {@code tile.boxes} of the tile at position {@code x, y}
     * to new configuration in the specific tile in position {@code x, y}
     *
     * @param newBoxes map of the new StorageTile configuration
     * @param x row
     * @param y columns
     * @throws RuntimeException if the selected tile is not a StorageTile
     */
    public void setBoxes(Map<BoxType, Integer> newBoxes, int x, int y) {
        Tile currTile = getTile(x, y);

        Map <BoxType, Integer> oldBoxes;
        oldBoxes = currTile.getBoxes();

        for(BoxType type : BoxType.values()) {
            if(newBoxes.get(type) < oldBoxes.get(type)) {
                int delta = oldBoxes.get(type) - newBoxes.get(type);
                currTile.removeBox(type, delta);
                this.boxes.put(type, this.boxes.get(type) - delta);
            }
        }

        for(BoxType type : BoxType.values()) {
            if(newBoxes.get(type) > oldBoxes.get(type)) {
                int delta = newBoxes.get(type) - oldBoxes.get(type);
                currTile.addBox(type, delta);
                this.boxes.put(type, this.boxes.get(type) + delta);
            }
        }
    }

    /**
     * removes a single crewMember of type {@code type} from {@code this.crewMap}
     *
     * @param type type of crewMember to remove
     */
    public void removeCrewByType(CrewType type) {

        // non bellissimo ma potrebbe dover essere necessario in caso di rimozione tile
        if(type == null) { return; }

        // todo: handle this gracefully
        assert crewMap.containsKey(type);
        assert crewMap.get(type) > 0;

        crewMap.put(type, this.getNumCrewByType(type) - 1);
    }

    /**
     * removes {@code num} crewMembers of type {@code type} from the HousingTile at position {@code x, y}
     * and from {@code this.crewMap}
     *
     * @param type type of crewMembers to be removed
     * @param x row
     * @param y column
     * @param num number of crewMembers to be removed
     * @throws RuntimeException if there are not enough members to be removed in {@code this.crewMap}
     */
    public void removeCrew(CrewType type, int x, int y, int num) {

        if(type == null) { return; }

        // remove crewMembers from tile
        for(int i = 0; i<num; i++) {
            getTile(x, y).removeCrewMember();
        }

        // remove crewMembers from ship
        if (crewMap.get(type) < num) {
            throw new RuntimeException("not enough crewMembers to remove");
        }
        crewMap.put(type, this.getNumCrewByType(type) - num);
    }

    /**
     * adds a single crewMember of type {@code type} to {@code this.crewMap}
     *
     * @param type type of crewMember to add
     */
    public void addCrewByType(CrewType type) {
        crewMap.put(type, this.getNumCrewByType(type) + 1);
    }

    /**
     * fills the HousingTile in position {@code x, y} with crewMember/s of type {@code type} and adds them to {@code this.crewMap}
     * if the type is {@code HUMAN} adds 2, otherwise adds 1
     *
     * @param type type of crewMember to be added
     * @param x row
     * @param y column
     * @throws RuntimeException if the argument type is not support by the specific HousingTile
     */
    public void addCrew(CrewType type, int x, int y) {
        if(type == CrewType.HUMAN){
            crewMap.put(type, this.getNumCrewByType(type) + 2);
            getTile(x, y).addCrew(type);
            //getTile(x, y).addCrew(type);
            return;
        }

        if(!getTile(x, y).getSupportedCrewType().contains(type)) {
            throw new RuntimeException("this alien type is not supported in tile:" + x + y);
        }
        crewMap.put(type, this.getNumCrewByType(type) + 1);
        getTile(x, y).addCrew(type);
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

    /**
     * updates {@code this.numExposedConnectors}
     * it's called after each placeTile and BreakTile
     *
     */
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

    /**
     * remove all the directions contained in {@code dir} from the list {@code this.protectedDirections}
     *
     * @param dir set of direction to be removed from protectedDirections list
     */
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

    /**
     * adds all the directions contained in {@code dir} to the list {@code this.protectedDirections}
     *
     * @param dir set of direction to be added to protectedDirections list
     */
    public void addProtectedDirections(Set<Direction> dir) {
        protectedDirections.addAll(dir);
    }

    /**
     * @return {@code true} if the ship is legal and connected, {@code false} otherwise
     *
     */
    public boolean isShipLegal() {

        // for all tiles, existing neighbouring tiles must have matching connector
        for (int i = 0; i < shipHeight; i++) {
            for (int j = 0; j < shipWidth; j++) {

                if (tilesMatrix[i][j] != null) {
                    Tile currTile = tilesMatrix[i][j];

                    if (currTile != null && i != 0 &&!currTile.isValidConnection(Direction.UP, tilesMatrix[i - 1][j])) {
                        return false;
                    }

                    if (currTile!= null && j != 0 && !currTile.isValidConnection(Direction.LEFT, tilesMatrix[i][j - 1])) {
                        return false;
                    }

                    if (currTile != null && i != shipHeight - 1 && !currTile.isValidConnection(Direction.DOWN, tilesMatrix[i + 1][j])) {
                        return false;
                    }

                    if (currTile != null && j != shipWidth - 1 && !currTile.isValidConnection(Direction.RIGHT, tilesMatrix[i][j + 1])) {
                        return false;
                    }
                }
            }
        }
        return isShipConnectedBFS();
    }

    /**
     * @return {@code true} if the ship is connected, {@code false} otherwise
     *
     */
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
                    if(startX == -1) {
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

                if(newX >= 0 && newX < shipHeight && newY >= 0 && newY < shipWidth) {
                    if(!visited[newX][newY] && tilesMatrix[newX][newY] != null && currTile.isValidConnection(direction, tilesMatrix[newX][newY])) {
                        list.add(new int[]{newX, newY});
                        visited[newX][newY] = true;
                    }
                }
            }
        }
        return visitedTiles == totalTiles;
    }

    // attackType = 1 -> shot
    // attackType = 0 -> meteor

    /**
     * calls checkMeteor or checkAttack depending on {@code attackType} argument
     *
     * @param dir direction of attack
     * @param attack LIGHT or HEAVY
     * @param k dices result (shifted to match matrix indexing)
     * @param attackType 0 for Meteor, 1 for Attack
     * @return see return values of checkMeteor and checkAttack
     */
    public int checkHit(Direction dir, Attack attack, int k, int attackType){
        return attackType == 0 ? checkMeteor(dir, attack, k) : checkAttack(dir, attack, k);
    }

    /**
     * this methode checks if the ship is hit by an attack and if the player can defend the ship through shields
     *
     * @param dir attack direction
     * @param attack HEAVY or LIGHT
     * @param k dices result (shifted to match matrix indexing)
     * @return {@code -1} if the attack does not hit the ship<br>
     *            {@code 0} if player can defend the ship with a shield<br>
     *            {@code 2} if the ship is hit and the player can not do anything about it
     */
    public int checkAttack(Direction dir, Attack attack, int k){
        boolean flag = false;
        if((k < 0 || k >= shipWidth) && (dir == Direction.UP || dir == Direction.DOWN)) {
            return -1;
        }
        if((k < 0 || k >= shipHeight) && (dir == Direction.LEFT || dir == Direction.RIGHT)) {
            return -1;
        }

        if(dir == Direction.UP) {
            for(int i = 0; i<shipHeight; i++) {
                if (tilesMatrix[i][k] != null) {
                    flag = true;
                    if (attack == Attack.LIGHT && getProtectedDirections().contains(dir)) {
                        return 0;
                    }
                    if (attack == Attack.HEAVY){
                        return 2;
                    }
                }
            }
        }

        if(dir == Direction.LEFT) {
            for(int i = 0; i<shipWidth; i++) {
                if (tilesMatrix[k][i] != null) {
                    flag = true;
                    if (attack == Attack.LIGHT && getProtectedDirections().contains(dir)) {
                        return 0;
                    }
                    if (attack == Attack.HEAVY){
                        return 2;
                    }
                }
            }
        }

        if(dir == Direction.RIGHT) {
            for(int i = 0; i<shipWidth; i++) {
                if (tilesMatrix[k][shipWidth-i-1] != null) {
                    flag = true;
                    if (attack == Attack.LIGHT && getProtectedDirections().contains(dir)) {
                        return 0;
                    }
                    if (attack == Attack.HEAVY){
                        return 2;
                    }
                }
            }
        }

        if(dir == Direction.DOWN) {
            for(int i = 0; i<shipHeight; i++) {
                if (tilesMatrix[shipHeight-i-1][k] != null) {
                    flag = true;
                    if (attack == Attack.LIGHT && getProtectedDirections().contains(dir)) {
                        return 0;
                    }
                    if (attack == Attack.HEAVY){
                        return 2;
                    }
                }
            }
        }

        return flag ? 2 : -1;
    }

    /**
     * this methode checks if the ship is hit by a meteorite and if the player can use cannons or shields to defend teh ship
     *
     * @param dir attack direction
     * @param meteor HEAVY or LIGHT
     * @param k dices result (shifted to match matrix indexing)
     * @return {@code -1} if the attack does not hit the ship<br>
     *            {@code 0} if player can defend the ship with a shield<br>
     *            {@code 1} if the player can defend the ship with a double cannon<br>
     *            {@code 2} if the ship is hit and the player can not do anything about it
     */
    public int checkMeteor(Direction dir, Attack meteor, int k) {
        Set<Connection> exposed = new HashSet<>(Set.of(Connection.SINGLE, Connection.DOUBLE, Connection.UNIVERSAL));
        boolean flag = false;

        if((k < 0 || k >= shipWidth) && (dir == Direction.UP || dir == Direction.DOWN)) {
            return -1;
        }
        if((k < 0 || k >= shipHeight) && (dir == Direction.LEFT || dir == Direction.RIGHT)) {
            return -1;
        }
        if(dir == Direction.UP){
            // not exposed connection
            for(int i = 0; i<shipHeight; i++) {
                if (tilesMatrix[i][k] != null) {
                    flag = true;
                    if ((!exposed.contains(tilesMatrix[i][k].getConnection(dir)) && meteor != Attack.HEAVY)) {
                        return -1; // non fa nulla
                    }break;
                }
            }
            if(!flag){ return -1;}
            // cannone singolo ti salva
            for(int i = 0; i<shipHeight; i++) {
                if(tilesMatrix[i][k] != null && tilesMatrix[i][k].getConnection(dir) == Connection.GUN && tilesMatrix[i][k].isDoubleLaser() != null
                        && !tilesMatrix[i][k].isDoubleLaser()) {
                    return -1;
                }
            }
            // shield ti salva
            for(int i = 0; i<shipHeight; i++) {
                if(tilesMatrix[i][k] != null && meteor != Attack.HEAVY && getProtectedDirections().contains(dir)) {
                    return 0;
                }
            }
            // cannone doppio ti salva
            for(int i = 0; i<shipHeight; i++) {
                if(tilesMatrix[i][k] != null && tilesMatrix[i][k].getConnection(dir) == Connection.GUN && tilesMatrix[i][k].isDoubleLaser() != null
                        && tilesMatrix[i][k].isDoubleLaser()){
                    return 1;
                }
            }
        }
        if(dir == Direction.LEFT){
            for(int i = 0; i<shipWidth; i++) {
                if (tilesMatrix[k][i] != null) {
                    flag = true;
                    if ((!exposed.contains(tilesMatrix[k][i].getConnection(dir)) && meteor != Attack.HEAVY)) {
                        return -1; // non fa nulla
                    }break;
                }
            }
            if(!flag){ return -1;}
            // cannone singolo ti salva
            for(int i = 0; i<shipWidth; i++) {
                if(tilesMatrix[k][i] != null && tilesMatrix[k][i].getConnection(dir) == Connection.GUN && tilesMatrix[k][i].isDoubleLaser() != null
                        && !tilesMatrix[k][i].isDoubleLaser()) {
                    return -1;
                }
            }
            // shield ti salva
            for(int i = 0; i<shipWidth; i++) {
                if(tilesMatrix[k][i] != null && meteor != Attack.HEAVY && getProtectedDirections().contains(dir)) {
                    return 0;
                }
            }
            // cannone doppio ti salva
            for(int i = 0; i<shipWidth; i++) {
                if(tilesMatrix[k][i] != null && tilesMatrix[k][i].getConnection(dir) == Connection.GUN && tilesMatrix[k][i].isDoubleLaser() != null
                        && tilesMatrix[k][i].isDoubleLaser()){
                    return 1;
                }
            }
        }
        if(dir == Direction.RIGHT){
            for(int i = 0; i<shipWidth; i++) {
                if (tilesMatrix[k][shipWidth-1-i] != null) {
                    flag = true;
                    if ((!exposed.contains(tilesMatrix[k][shipWidth-1-i].getConnection(dir)) && meteor != Attack.HEAVY)) {
                        return -1; // non fa nulla
                    }break;
                }
            }
            if(!flag){ return -1;}
            // cannone singolo ti salva
            for(int i = 0; i<shipWidth; i++) {
                if(tilesMatrix[k][shipWidth-1-i] != null && tilesMatrix[k][shipWidth-1-i].getConnection(dir) == Connection.GUN && tilesMatrix[k][shipWidth-1-i].isDoubleLaser() != null
                        && !tilesMatrix[k][shipWidth-1-i].isDoubleLaser()) {
                    return -1;
                }
            }
            // shield ti salva
            for(int i = 0; i<shipWidth; i++) {
                if(tilesMatrix[k][shipWidth-1-i] != null && meteor != Attack.HEAVY && getProtectedDirections().contains(dir)) {
                    return 0;
                }
            }
            // cannone doppio ti salva
            for(int i = 0; i<shipWidth; i++) {
                if(tilesMatrix[k][shipWidth-1-i] != null && tilesMatrix[k][shipWidth-1-i].getConnection(dir) == Connection.GUN && tilesMatrix[k][shipWidth-1-i].isDoubleLaser() != null
                        && tilesMatrix[k][shipWidth-1-i].isDoubleLaser()){
                    return 1;
                }
            }
        }
        if(dir == Direction.DOWN){
            // not exposed connection
            for(int i = 0; i<shipHeight; i++) {
                if (tilesMatrix[shipHeight-1-i][k] != null) {
                    flag = true;
                    if ((!exposed.contains(tilesMatrix[shipHeight-1-i][k].getConnection(dir)) && meteor != Attack.HEAVY)) {
                        return -1; // non fa nulla
                    }break;
                }
            }
            if(!flag){ return -1;}
            // cannone singolo ti salva
            for(int i = 0; i<shipHeight; i++) {
                if(tilesMatrix[shipHeight-1-i][k] != null && tilesMatrix[shipHeight-1-i][k].getConnection(dir) == Connection.GUN && tilesMatrix[shipHeight-1-i][k].isDoubleLaser() != null
                        && !tilesMatrix[shipHeight-1-i][k].isDoubleLaser()) {
                    return -1;
                }
            }
            // shield ti salva
            for(int i = 0; i<shipHeight; i++) {
                if(tilesMatrix[shipHeight-1-i][k] != null && meteor != Attack.HEAVY && getProtectedDirections().contains(dir)) {
                    return 0;
                }
            }
            // cannone doppio ti salva
            for(int i = 0; i<shipHeight; i++) {
                if(tilesMatrix[shipHeight-1-i][k] != null && tilesMatrix[shipHeight-1-i][k].getConnection(dir) == Connection.GUN && tilesMatrix[shipHeight-1-i][k].isDoubleLaser() != null
                        && tilesMatrix[shipHeight-1-i][k].isDoubleLaser()){
                    return 1;
                }
            }
        }
        return flag ? 2 : -1;
    }

    /**
     * if the ship is hit destroy the target tile <br>
     * IMPORTANT!!! this methode does not check if the player can use batteries to defend the ship
     * or if the hit causes the tile to break therefore it should only be called after {@code checkHit}
     * if and only if a tile would actually be broken
     *
     * @param dir direction of the attack
     * @param meteor attack type, LIGHT or HEAVY
     * @param k dices result (shifted to match matrix indexing)
     * @return {@code true} if hit<br>{@code false} if not hit
     */
    public boolean handleMeteor(Direction dir, Attack meteor, int k) {
        if((k < 0 || k >= shipWidth) && (dir == Direction.UP || dir == Direction.DOWN)) {
            return false;
        }

        if((k < 0 || k >= shipHeight) && (dir == Direction.LEFT || dir == Direction.RIGHT)) {
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

    // todo serve???
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
        for (int y = 0; y < shipHeight; y++) {
            for (int x = 0; x < shipWidth; x++) {
                if (this.validSlots[y][x]) {
                    tilesMatrixGrid.append(String.format("(%d,%d) ", y, x));
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
                    tilesMatrixGrid.append(String.format("(%d,%d): %s\n", y, x, tilesMatrix[y][x]));
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
