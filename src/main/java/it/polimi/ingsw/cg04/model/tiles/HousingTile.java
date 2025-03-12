package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;

import java.util.Set;

public class HousingTile extends Tile {

    private int numCrew;
    private CrewType hostedCrewType;
    private Set<CrewType> supportedCrewType;

    private boolean isCentralTile;
    private PlayerColor color;

    public HousingTile(boolean isCentralTile, PlayerColor color) {

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
}
