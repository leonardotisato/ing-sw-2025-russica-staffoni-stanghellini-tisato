package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StorageTile extends Tile {
    private final boolean isSpecialStorageTile;
    private final Integer maxBoxes;
    private Map<BoxType, Integer> boxes;

    public StorageTile(Integer maxBoxes, boolean isSpecialStorageTile) {
        this.maxBoxes = maxBoxes;
        this.isSpecialStorageTile = isSpecialStorageTile;
        this.boxes = new HashMap<>();
        boxes.put(BoxType.BLUE, 0);
        boxes.put(BoxType.RED, 0);
        boxes.put(BoxType.YELLOW, 0);
        boxes.put(BoxType.GREEN, 0);
    }

    @Override
    public Boolean isSpecialStorageTile() {
        return isSpecialStorageTile;
    }

    @Override
    public Integer getMaxBoxes() {
        return maxBoxes;
    }

    @Override
    public Map<BoxType, Integer> getBoxes() {
        return boxes;
    }

    @Override
    public void addBox(BoxType boxType){
        boxes.put(boxType, boxes.get(boxType) + 1);
    }

    @Override
    public void removeBox(BoxType boxType){
        boxes.put(boxType, boxes.get(boxType) - 1);
    }
}
