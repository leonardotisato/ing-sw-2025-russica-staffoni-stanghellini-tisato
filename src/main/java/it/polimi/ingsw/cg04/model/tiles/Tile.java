package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.*;
import it.polimi.ingsw.cg04.model.Ship;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class Tile {
    @Expose
    String type;
    @Expose
    Map<Direction, Connection> connections;
    @Expose
    int id;

    // Tile generic methods
    public Tile() {}


    public Tile(PlayerColor playerColor) {
        this.type = "HousingTile";

        connections = new HashMap<>();
        connections.put(Direction.UP, Connection.UNIVERSAL);
        connections.put(Direction.RIGHT, Connection.UNIVERSAL);
        connections.put(Direction.DOWN, Connection.UNIVERSAL);
        connections.put(Direction.LEFT, Connection.UNIVERSAL);

        switch (playerColor) {
            case BLUE:
                id = 33;
                break;
            case GREEN:
                id = 34;
                break;
            case RED:
                id = 52;
                break;
            case YELLOW:
                id = 61;
                break;
        }
    }

    public String getType() {
        return type;
    }

    // serve???
//    public void setType(String type) {
//        this.type = type;
//    }

    public Integer getId() {
        return id;
    }

    public void place(Ship ship, int x, int y) {
        // todo: implement each tile with specific tile-ship attributes updates
    }

    public void broken(Ship ship, int x, int y) {
        // todo: implement in each specific tile
        // todo: count connectors?
    }

    /**
     * Rotates the Tile 90 degrees clockwise.
     * <p>
     * This method rotates the Tile’s connections in a clockwise direction, updating the direction of
     * each connection as follows:
     * <ul>
     *     <li>UP → RIGHT</li>
     *     <li>RIGHT → DOWN</li>
     *     <li>DOWN → LEFT</li>
     *     <li>LEFT → UP</li>
     * </ul>
     */
    public void rotate90dx() {

        Map<Direction, Direction> rotationMap = Map.of(
                Direction.UP, Direction.RIGHT,
                Direction.RIGHT, Direction.DOWN,
                Direction.DOWN, Direction.LEFT,
                Direction.LEFT, Direction.UP
        );

        Map<Direction, Connection> newConnections = new HashMap<>();
        for (Direction dir : connections.keySet()) {
            newConnections.put(rotationMap.get(dir), connections.get(dir));
        }
        connections = newConnections;
    }

    /**
     * Rotates the Tile 180 degrees by performing two 90 degrees clockwise rotations.
     */
    public void rotate180() {
        rotate90dx();
        rotate90dx();
    }

    /**
     * Rotates the Tile 90 degrees counterclockwise.
     */
    public void rotate90sx() {
        rotate90dx();
        rotate90dx();
        rotate90dx();
    }

    /**
     * Retrieves the connection associated with the specified direction.
     *
     * @param dir The direction for which the connection is to be retrieved.
     *            Must be one of the valid directions defined in the {@link Direction} enum.
     *
     * @return The {@link Connection} associated with the specified direction.
     */
    public Connection getConnection(Direction dir) {
        return connections.get(dir);
    }

//    public void setConnections(Map<Direction, Connection> connections) {
//        this.connections = connections;
//    }

    // return true if connection between this && otherTile is valid. eg: dir=RIGHT ==> this >--< otherTile
    public boolean isValidConnection(Direction dir, Tile otherTile) {

        if(/*this instanceof PropulsorTile*/ this.isDoublePropulsor() != null && this.getConnection(Direction.DOWN) != Connection.PROPULSOR) {
            return false;
        }

        // if otherTile does not exist the connection is valid
        if (otherTile == null) {
            return true;
        }

        Connection c1, c2;
        // set up connections to consider to validate
        switch (dir) {
            case UP:
                c1 = this.getConnection(Direction.UP);
                c2 = otherTile.getConnection(Direction.DOWN);
                break;
            case RIGHT:
                c1 = this.getConnection(Direction.RIGHT);
                c2 = otherTile.getConnection(Direction.LEFT);
                break;
            case DOWN:
                c1 = this.getConnection(Direction.DOWN);
                c2 = otherTile.getConnection(Direction.UP);
                break;
            case LEFT:
                c1 = this.getConnection(Direction.LEFT);
                c2 = otherTile.getConnection(Direction.RIGHT);
                break;
            default:
                c1 = null;
                c2 = null;
                assert false;
                break;
        }

        // if c1 universal only single, double and universal are allowed for c2
        if (c1 == Connection.UNIVERSAL) {
            return c2 == Connection.UNIVERSAL || c2 == Connection.SINGLE || c2 == Connection.DOUBLE;
        }

        // if c1 single, only single and universal are allowed for c2
        if (c1 == Connection.SINGLE) {
            return c2 == Connection.SINGLE || c2 == Connection.UNIVERSAL;
        }

        // if c1 double, only double and universal are allowed for c2
        if (c1 == Connection.DOUBLE) {
            return c2 == Connection.DOUBLE || c2 == Connection.UNIVERSAL;
        }

        // if c1 empty, only empty is allowed for c2
        if (c1 == Connection.EMPTY) {
            return c2 == Connection.EMPTY;
        }

        // i restanti casi per c1 sono PROPULSOR e GUN, ma dovrebbero ritornare tutti false...

        return false;
    }

    // shieldTile methods
    public Set<Direction> getProtectedDirections() {
        return null;
    }

    // batteryTile methods
    public Integer getMaxBatteryCapacity() {
        return null;
    }

    // todo: check if this is ok
    public Integer getNumBatteries() {
        return null;
    }

    public void removeBatteries(Integer numBatteries) {

        if (!(this instanceof BatteryTile)) {
            System.out.println("Illegal Operation!");
            throw new RuntimeException("Illegal Operation!");
        }
    }

    // storageTile methods
    public Boolean isSpecialStorageTile() {
        return null;
    }

    public Integer getMaxBoxes() {
        return null;
    }

    public Map<BoxType, Integer> getBoxes() {
        return null;
    }

    public void addBox(BoxType boxType, int num) {
        throw new RuntimeException("Illegal Operation!");
    }

    public void removeBox(BoxType boxType, int num) {
        throw new RuntimeException("Illegal Operation!");
    }

    // propulsorTile methods
    public Boolean isDoublePropulsor() {
        return null;
    }

    public void setDoublePropulsor(Boolean doublePropulsor) {
    }

    // laserTile methods
    public Boolean isDoubleLaser() {
        return null;
    }

    public Direction getShootingDirection() {
        return null;
    }

    // alienSupportTile methods
    public CrewType getSupportedAlienColor() {
        return null;
    }

    public void addAdjacentHousingTile(Tile tile){ }

    public void removeAdjacentHousingTile(Tile tile){ }

    public Set<Tile> getAdjacentHousingTiles(){ return null; }
    // housingTile methods
    // this one is tricky... housingUnit can be central, and if so have a specific color
    // and can host only humans if its central, and host aliens of a specific color only if a alienSupportTile is neighbouring...
    public Boolean isCentralTile() {
        return null;
    }

    public void updateSupportedCrewTypes() {

    }

    public void addSupportedCrewType(CrewType crewType) {
    }

    public void removeSupportedCrewType(CrewType crewType) {
    }

    public List<CrewType> getSupportedCrewType() {
        return null;
    }

    public Integer getNumCrew() {
        return null;
    }

    public void addCrew(CrewType crewType) {

    }

    public CrewType removeCrewMember() {
        return null;
    }

    public CrewType getHostedCrewType() {
        return null;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tile{");
        sb.append("type='").append(type).append("', ");
        sb.append("connections=").append(connections);

        if (getProtectedDirections() != null)
            sb.append(", protectedDirections=").append(getProtectedDirections());

        if (getMaxBatteryCapacity() != null)
            sb.append(", maxBatteryCapacity=").append(getMaxBatteryCapacity());

        if (getNumBatteries() != null)
            sb.append(", numBatteries=").append(getNumBatteries());

        if (getBoxes() != null)
            sb.append(", boxes=").append(getBoxes());

        if (getMaxBoxes() != null)
            sb.append(", maxBoxes=").append(getMaxBoxes());

        if (isSpecialStorageTile() != null)
            sb.append(", specialStorageTile=").append(isSpecialStorageTile());

        if (isDoublePropulsor() != null)
            sb.append(", doublePropulsor=true");

        if (isDoubleLaser() != null)
            sb.append(", doubleLaser=true");

        if (getSupportedAlienColor() != null)
            sb.append(", supportedAlienColor=").append(getSupportedAlienColor());

        if (getAdjacentHousingTiles() != null)
            sb.append(", adjacentHousingTiles=").append(getAdjacentHousingTiles());

        if (getSupportedCrewType() != null)
            sb.append(", supportedCrewType=").append(getSupportedCrewType());

        if (isCentralTile() != null)
            sb.append(", centralTile=true");

        if (getNumCrew() != null)
            sb.append(", numCrew=").append(getNumCrew());

        sb.append("}");

        return sb.toString();
    }

}
