package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.Ship;

import java.util.HashMap;
import java.util.Map;

public class StorageTile extends Tile {
    private boolean isSpecialStorageTile;
    private Integer maxBoxes;
    private Map<BoxType, Integer> boxes;

    public StorageTile() {
        super();
        this.boxes = new HashMap<>();
        boxes.put(BoxType.BLUE, 0);
        boxes.put(BoxType.RED, 0);
        boxes.put(BoxType.YELLOW, 0);
        boxes.put(BoxType.GREEN, 0);

        if (isSpecialStorageTile) {
            this.shortName = "SS";
        } else {
            this.shortName = "NS";
        }
    }

    @Override
    public Boolean isSpecialStorageTile() {
        return isSpecialStorageTile;
    }

    public void setSpecialStorageTile(boolean isSpecialStorageTile) {
        this.isSpecialStorageTile = isSpecialStorageTile;
    }

    @Override
    public Integer getMaxBoxes() {
        return maxBoxes;
    }
    public void setMaxBoxes(Integer maxBoxes) {
        this.maxBoxes = maxBoxes;
    }

    @Override
    public Map<BoxType, Integer> getBoxes() {
        return boxes;
    }
    public void setBoxes(Map<BoxType, Integer> boxes) {
        this.boxes = boxes;
    }

    @Override
    public void addBox(BoxType boxType, int num){
        if(!this.isSpecialStorageTile() && boxType == BoxType.RED) {
            throw new RuntimeException("Illegal Operation!");
        }
        int totalBoxes = boxes.values().stream().mapToInt(Integer::intValue).sum();
        if (totalBoxes >= maxBoxes) { throw new RuntimeException("Illegal Operation! Max capacity exceeded!"); }

        boxes.put(boxType, boxes.get(boxType) + num);
    }

    @Override
    public void removeBox(BoxType boxType, int num){
        int totalBoxes = boxes.values().stream().mapToInt(Integer::intValue).sum();
        if (totalBoxes == 0) { throw new RuntimeException("Illegal Operation! No boxes found!"); }

        if (!boxes.containsKey(boxType)) { throw new RuntimeException("Illegal Operation! No boxes of type " + boxType + " found!"); }
        if (boxes.get(boxType) == 0) { throw new RuntimeException("Illegal Operation! boxes.get(boxType) == 0 found!"); }

        boxes.put(boxType, boxes.get(boxType) - 1);
    }

    /**
     *  empties this tile storage and updates ship map {@cod boxes}
     *
     * @param ship
     * @param x
     * @param y
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        // rimuove tutti i box un tipo alla volta
        Map <BoxType, Integer> emptyStorage = new HashMap<>();
        emptyStorage.put(BoxType.BLUE, 0);
        emptyStorage.put(BoxType.RED, 0);
        emptyStorage.put(BoxType.YELLOW, 0);
        emptyStorage.put(BoxType.GREEN, 0);

        ship.setBoxes(emptyStorage, x, y);
    }

    @Override
    void drawContent(StringBuilder sb) {
        StringBuilder storage = new StringBuilder();
        storage.append("r".repeat(boxes.get(BoxType.RED)));
        storage.append("y".repeat(boxes.get(BoxType.YELLOW)));
        storage.append("b".repeat(boxes.get(BoxType.BLUE)));
        storage.append("g".repeat(boxes.get(BoxType.GREEN)));
        storage.append("-".repeat(maxBoxes - boxes.values().stream().mapToInt(Integer::intValue).sum()));

        sb.append("│ ").append(centerText(storage.toString(), boxWidth - 4)).append(" │").append("\n");
    }
}
