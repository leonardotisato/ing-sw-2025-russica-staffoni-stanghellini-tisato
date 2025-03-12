package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.AlienColor;

public class AlienSupportTile extends Tile {

    private final AlienColor supportedAlienColor;

    public AlienSupportTile(AlienColor supportedAlienColor) {
        this.supportedAlienColor = supportedAlienColor;
    }

    @Override
    public AlienColor getSupportedAlienColor() {
        return supportedAlienColor;
    }
}
