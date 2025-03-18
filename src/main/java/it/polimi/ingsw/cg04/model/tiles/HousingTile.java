package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.*;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HousingTile extends Tile {

    private int numCrew;
    private CrewType hostedCrewType = null;
    private List<CrewType> supportedCrewType;

    private boolean isCentralTile;
    private PlayerColor color;

    public HousingTile(Map<Direction, Connection> connectionMap, boolean isCentralTile, PlayerColor color) {
        super(connectionMap);
        this.isCentralTile = isCentralTile;
        this.color = color;
    }

    public Boolean isCentralTile() {
        return isCentralTile;
    }

    public void updateSupportedCrewTypes() {
        // if (!isCentralTile || no supports) { supportedCrewType = set(HUMANS)
        // else supportedCrewType can host some aliens...
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

    public void removeCrewMember() {
        if (numCrew <= 0) {
            throw new RuntimeException("Crew member is zero or negative");
        }

        numCrew--;

        if (numCrew == 0) {
            hostedCrewType = null;
        }
    }

    @Override
    public void broken(Ship ship) {
        if (hostedCrewType.equals(CrewType.HUMAN)) {
            for (int i = numCrew; i > 0; i--) {
                ship.removeCrewByType(CrewType.HUMAN);
            }
        } else ship.removeCrewByType(hostedCrewType);

        // remove this tile from adjacentHousingTile in relative AlienSupportTile
        Tile[][] tilesMatrix = ship.getTilesMatrix();
        for (int i = 0; i < tilesMatrix[0].length - 1; i++) {
            for (int j = 0; j < tilesMatrix.length - 1; j++) {
                if (tilesMatrix[i][j] instanceof AlienSupportTile && tilesMatrix[i][j].getAdjacentHousingTiles().contains(this)) {
                    tilesMatrix[i][j].removeAdjacentHousingTile(this);
                }
            }
        }

    }
}
