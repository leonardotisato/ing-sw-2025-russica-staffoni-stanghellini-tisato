package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.*;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.Ship;

import java.util.ArrayList;
import java.util.List;

public class HousingTile extends Tile {

    private int numCrew;
    private CrewType hostedCrewType = null;
    private final List<CrewType> supportedCrewType;

    @Expose
    private boolean isCentralTile;
    private PlayerColor color;

    public HousingTile() {
        super();
        supportedCrewType = new ArrayList<>();
    }

    public Boolean isCentralTile() {
        return isCentralTile;
    }
    public void setIsCentralTile(boolean isCentralTile) {
        this.isCentralTile = isCentralTile;
    }

    public PlayerColor getColor() {
        return color;
    }
    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public void updateSupportedCrewTypes() {
        // if (!isCentralTile || no supports) { supportedCrewType = set(HUMANS)
        // else supportedCrewType can host some aliens...
    }
    public void addSupportedCrewType(CrewType crewType) {
        supportedCrewType.add(crewType);
    }

    public void removeSupportedCrewType(CrewType crewType) {
        supportedCrewType.remove(crewType);
    }

    public List<CrewType> getSupportedCrewType() {
        return supportedCrewType;
    }

    public void addCrew(CrewType type) {
        if (type == null) {
            throw new RuntimeException("CrewType cannot be null");
        }

        if (!supportedCrewType.contains(type)) {
            throw new RuntimeException("Crew type:" + type + " is not supported");
        }

        hostedCrewType = type;
        if (hostedCrewType == CrewType.HUMAN) {
            numCrew = 2;
        } else {
            numCrew = 1;
        }

    }

    public Integer getNumCrew() {
        return numCrew;
    }

    public CrewType getHostedCrewType() {
        return hostedCrewType;
    }

    public CrewType removeCrewMember() {
        if (numCrew <= 0) {
            throw new RuntimeException("Crew member is zero or negative");
        }

        numCrew--;
        CrewType removed = hostedCrewType;
        if (numCrew == 0) {
            hostedCrewType = null;
        }
        return removed;
    }


    // remove all the crewMembers and remove this from all the AlienSupportTiles Adjacent list
    @Override
    public void broken(Ship ship) {
        // may occur that HousingTile is empty this methode is called
        if(hostedCrewType != null) {
            if (hostedCrewType == CrewType.HUMAN) {
                for (int i = numCrew; i > 0; i--) {
                    ship.removeCrewByType(CrewType.HUMAN);
                }
            } else ship.removeCrewByType(hostedCrewType);
            hostedCrewType = null;
        }

        // remove this tile from adjacentHousingTile in relative AlienSupportTile
        Tile[][] tilesMatrix = ship.getTilesMatrix();
        for (int i = 0; i < tilesMatrix[0].length - 1; i++) {
            for (int j = 0; j < tilesMatrix.length - 1; j++) {
                if (tilesMatrix[i][j] != null && tilesMatrix[i][j] instanceof AlienSupportTile && tilesMatrix[i][j].getAdjacentHousingTiles().contains(this)) {
                    tilesMatrix[i][j].removeAdjacentHousingTile(this);
                }
            }
        }

        // clear in case of future replacement
        supportedCrewType.clear();
    }

    // add human to supportedCrewType than adds 2 humans
    // check every adjacent tile, if there is a AlienSupportTile add this to it's adjacentList, update this.supportedCrewTypes
    // lastly remove humans and leave the choice about the crew to the player after building the ship
    @Override
    public void place(Ship ship) {
        // every HousingTile supports humans
        this.addSupportedCrewType(CrewType.HUMAN);

        // initially add 2 humans, remove them if there is a LifeSupportTile adjacent and leave the choice of hostedCrewType to the player
        this.addCrew(CrewType.HUMAN);
        ship.addCrewByType(CrewType.HUMAN);
        ship.addCrewByType(CrewType.HUMAN);

        int shipHeight = ship.getTilesMatrix().length;
        int shipWidth = ship.getTilesMatrix()[0].length;
        Tile[][] tilesMatrix = ship.getTilesMatrix();
        for(int i=0; i<shipHeight; i++){
            for(int j=0; j<shipWidth; j++){
                if(tilesMatrix[i][j] != null && tilesMatrix[i][j].equals(this)) {

                    // look UP -> check tile type and connection
                    if (i > 0 && tilesMatrix[i - 1][j] instanceof AlienSupportTile
                            && this.isValidConnection(Direction.UP, tilesMatrix[i - 1][j])
                            && this.getConnection(Direction.UP) != Connection.EMPTY) {
                        tilesMatrix[i - 1][j].addAdjacentHousingTile(this);
                        // remove humans, leave the choice to the player
                        this.removeCrewMember();
                        this.removeCrewMember();
                        ship.removeCrewByType(CrewType.HUMAN);
                        ship.removeCrewByType(CrewType.HUMAN);
                        this.addSupportedCrewType(tilesMatrix[i - 1][j].getSupportedAlienColor());
                    }

                    // look LEFT -> check tile type and connection
                    if (j > 0 && tilesMatrix[i][j - 1] instanceof AlienSupportTile
                            && this.isValidConnection(Direction.LEFT, tilesMatrix[i][j - 1])
                            && this.getConnection(Direction.LEFT) != Connection.EMPTY) {
                        tilesMatrix[i][j - 1].addAdjacentHousingTile(this);
                        // remove humans, leave the choice to the player
                        this.removeCrewMember();
                        this.removeCrewMember();
                        ship.removeCrewByType(CrewType.HUMAN);
                        ship.removeCrewByType(CrewType.HUMAN);
                        this.addSupportedCrewType(tilesMatrix[i][j - 1].getSupportedAlienColor());
                    }

                    // look RIGHT -> check tile type and connection
                    if (j < shipWidth - 1 && tilesMatrix[i][j + 1] instanceof AlienSupportTile
                            && this.isValidConnection(Direction.RIGHT, tilesMatrix[i][j + 1])
                            && this.getConnection(Direction.RIGHT) != Connection.EMPTY) {
                        tilesMatrix[i][j + 1].addAdjacentHousingTile(this);
                        // remove humans, leave the choice to the player
                        this.removeCrewMember();
                        this.removeCrewMember();
                        ship.removeCrewByType(CrewType.HUMAN);
                        ship.removeCrewByType(CrewType.HUMAN);
                        this.addSupportedCrewType(tilesMatrix[i][j + 1].getSupportedAlienColor());
                    }

                    // look DOWN -> check tile type and connection
                    if (i < shipHeight - 1 && tilesMatrix[i + 1][j] instanceof AlienSupportTile
                            && this.isValidConnection(Direction.DOWN, tilesMatrix[i + 1][j])
                            && this.getConnection(Direction.DOWN) != Connection.EMPTY) {
                        tilesMatrix[i + 1][j].addAdjacentHousingTile(this);
                        // remove humans, leave the choice to the player
                        this.removeCrewMember();
                        this.removeCrewMember();
                        ship.removeCrewByType(CrewType.HUMAN);
                        ship.removeCrewByType(CrewType.HUMAN);
                        this.addSupportedCrewType(tilesMatrix[i + 1][j].getSupportedAlienColor());
                    }
                    return;
                }
            }
        }
    }
}
