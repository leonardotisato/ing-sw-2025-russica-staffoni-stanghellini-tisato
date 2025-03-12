package it.polimi.ingsw.cg04.model.tiles;

public class PropulsorTile extends Tile {

    private final boolean isDoublePropulsor;

    public PropulsorTile(boolean isDoublePropulsor) {
        this.isDoublePropulsor = isDoublePropulsor;
    }

    @Override
    public Boolean isDoublePropulsor() {
        return isDoublePropulsor;
    }
}
