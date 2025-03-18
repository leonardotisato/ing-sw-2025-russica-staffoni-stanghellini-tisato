package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StorageTile extends Tile {
    private final boolean isSpecialStorageTile;
    private final Integer maxBoxes;
    private final Map<BoxType, Integer> boxes;

    public StorageTile(Map<Direction, Connection> connectionMap, Integer maxBoxes, boolean isSpecialStorageTile) {
        super();
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
        if(!this.isSpecialStorageTile() && boxType == BoxType.RED) {
            throw new RuntimeException("Illegal Operation!");
        }
        int totalBoxes = boxes.values().stream().mapToInt(Integer::intValue).sum();
        if (totalBoxes >= maxBoxes) { throw new RuntimeException("Illegal Operation! Max capacity exceeded!"); }

        boxes.put(boxType, boxes.get(boxType) + 1);
    }

    @Override
    public void removeBox(BoxType boxType){
        int totalBoxes = boxes.values().stream().mapToInt(Integer::intValue).sum();
        if (totalBoxes == 0) { throw new RuntimeException("Illegal Operation! No boxes found!"); }

        if (!boxes.containsKey(boxType)) { throw new RuntimeException("Illegal Operation! No boxes of type " + boxType + " found!"); }
        if (boxes.get(boxType) == 0) { throw new RuntimeException("Illegal Operation! boxes.get(boxType) == 0 found!"); }

        boxes.put(boxType, boxes.get(boxType) - 1);
    }

    @Override
    public void broken(Ship ship) {
        //ship.removeBoxes(getBoxes());
        for(BoxType boxType : BoxType.values()) {
            if(boxes.get(boxType) > 0) {
                ship.removeBoxes(boxType, boxes.get(boxType));
            }
        }
    }
}
