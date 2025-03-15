package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.ships.Ship;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class Tile {
    protected Map<Direction, Connection> connections;

    // Tile generic methods
    public Tile(Map<Direction, Connection> connections) {
        this.connections = connections;
    }

    public void broken(Ship ship){
        // todo: implement in each specific tile
        // todo: count connectors?
        return;
    }

    // clockwise rotation of the Tile
    public void rotate90dx() {

        Map<Direction, Direction> rotationMap = Map.of(
                Direction.UP, Direction.RIGHT,
                Direction.RIGHT, Direction.DOWN,
                Direction.DOWN, Direction.LEFT,
                Direction.LEFT, Direction.UP
        );

        Map<Direction, Connection> newConnections = new HashMap<Direction, Connection>();
        for (Direction dir : connections.keySet()) {
            newConnections.put(rotationMap.get(dir), connections.get(dir));
        }
        connections = newConnections;
    }

    public void rotate180() {
        rotate90dx();
        rotate90dx();
    }

    // counterclockwise rotation of the Tile
    public void rotate90sx() {
        rotate90dx();
        rotate90dx();
        rotate90dx();
    }

    public Connection getConnection(Direction dir) {
        return connections.get(dir);
    }

    // return true if connection between this && otherTile is valid. eg: dir=RIGHT ==> this >--< otherTile
    public boolean isValidConnection(Direction dir, Tile otherTile) {

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
        return 0;
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

    public void addBox(BoxType boxType) {
        if (!(this instanceof StorageTile)) {
            System.out.println("Illegal Operation!");
            throw new RuntimeException("Illegal Operation!");
        }
    }

    public void removeBox(BoxType boxType) {
        if (!(this instanceof StorageTile)) {
            System.out.println("Illegal Operation!");
            throw new RuntimeException("Illegal Operation!");
        }
    }

    // propulsorTile methods
    public Boolean isDoublePropulsor() {
        return null;
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

    public Set<Tile> getAdjacentHousingTile(){ return null; }
    // housingTile methods
    // this one is tricky... housingUnit can be central, and if so have a specific color
    // and can host only humans if its central, and host aliens of a specific color only if a alienSupportTile is neighbouring...
    public Boolean isCentralTile() {
        return null;
    }

    public void updateSupportedCrewTypes() {

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

    public void removeCrewMember() {

    }

    public CrewType getHostedCrewType() {
        return null;
    }

    // todo: discuss how to model this better...

}
