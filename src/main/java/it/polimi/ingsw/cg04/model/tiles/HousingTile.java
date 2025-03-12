package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.*;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.Map;
import java.util.Set;

public class HousingTile extends Tile {

    private int numCrew;
    private CrewType hostedCrewType;
    private Set<CrewType> supportedCrewType;

    private boolean isCentralTile;
    private PlayerColor color;

    public HousingTile(Map<Direction, Connection> connectionMap, boolean isCentralTile, PlayerColor color) {
        super(connectionMap);
        this.isCentralTile = isCentralTile;
        this.color = color;
    }

    public void updateSupportedCrewTypes() {
        // if (!isCentralTile || no supports) { supportedCrewType = set(HUMANS)
        // else supportedCrewType can host some aliens...
    }

    public Set<CrewType> getSupportedCrewType() {
        return supportedCrewType;
    }

    public void addCrew(CrewType type) {
        // if type in supported add 1 alien or 2 humans
    }

    public int getNumCrew() {
        return numCrew;
    }

    public CrewType getHostedCrewType() {
        return hostedCrewType;
    }

    public void removeCrewMember() {
        // if numCrew > 0: numCrew--;
    }

    @Override
    public void broken(Ship ship){
        if (hostedCrewType.equals(CrewType.HUMAN)) {
            for(int i = numCrew; i > 0; i--) {
                ship.removeCrewByType(CrewType.HUMAN);
            }
        } else ship.removeCrewByType(hostedCrewType);
    }
}
