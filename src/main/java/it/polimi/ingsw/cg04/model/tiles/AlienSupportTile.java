package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.CrewType;

public class AlienSupportTile extends Tile {

    private final CrewType supportedAlienColor;

    public AlienSupportTile(CrewType supportedAlienColor) {
        this.supportedAlienColor = supportedAlienColor;
    }

    @Override
    public CrewType getSupportedAlienColor() {
        return supportedAlienColor;
    }
}
