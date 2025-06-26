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
    }

    @Override
    public String getName() {
        if (isSpecialStorageTile) {
            return "S. Storage";
        } else {
            return "N. Storage";
        }
    }

    @Override
    public String getTileColor() {
        if (isSpecialStorageTile) {
            return "\u001B[31m";
        } else {
            return "\u001B[36m";
        }
    }

    /**
     * @return {@code true} if the tile is a special storage tile; {@code false} otherwise.
     */
    @Override
    public Boolean isSpecialStorageTile() {
        return isSpecialStorageTile;
    }

    public void setSpecialStorageTile(boolean isSpecialStorageTile) {
        this.isSpecialStorageTile = isSpecialStorageTile;
    }

    /**
     * @return the maximum number of boxes as an {@code Integer}
     */
    @Override
    public Integer getMaxBoxes() {
        return maxBoxes;
    }

    public void setMaxBoxes(Integer maxBoxes) {
        this.maxBoxes = maxBoxes;
    }

    /**
     * @return a {@code Map} where keys are {@code BoxType} and values are their respective counts as {@code Integer}
     */
    @Override
    public Map<BoxType, Integer> getBoxes() {
        return boxes;
    }

    public void setBoxes(Map<BoxType, Integer> boxes) {
        this.boxes = boxes;
    }

    /**
     * Adds a specified number of boxes of a given type to the storage.
     *
     * @param boxType the type of box to be added, represented as {@code BoxType}
     * @param num     the number of boxes to be added, represented as an integer
     * @throws RuntimeException if the operation is illegal
     */
    @Override
    public void addBox(BoxType boxType, int num) {
        if (!this.isSpecialStorageTile() && boxType == BoxType.RED) {
            throw new RuntimeException("Illegal Operation!");
        }
        int totalBoxes = boxes.values().stream().mapToInt(Integer::intValue).sum();
        if (totalBoxes >= maxBoxes) {
            throw new RuntimeException("Illegal Operation! Max capacity exceeded!");
        }

        boxes.put(boxType, boxes.get(boxType) + num);
    }

    /**
     * Removes a specified number of boxes of a given type from the storage.
     *
     * @param boxType the type of box to be removed, represented as {@code BoxType}
     * @param num     the number of boxes to be removed, represented as an integer
     * @throws RuntimeException if the operation is illegal
     */
    @Override
    public void removeBox(BoxType boxType, int num) {
        int totalBoxes = boxes.values().stream().mapToInt(Integer::intValue).sum();
        if (totalBoxes == 0) {
            throw new RuntimeException("Illegal Operation! No boxes found!");
        }

        if (!boxes.containsKey(boxType)) {
            throw new RuntimeException("Illegal Operation! No boxes of type " + boxType + " found!");
        }
        if (boxes.get(boxType) == 0) {
            throw new RuntimeException("Illegal Operation! boxes.get(boxType) == 0 found!");
        }

        boxes.put(boxType, boxes.get(boxType) - 1);
    }

    /**
     * empties this tile storage and updates ship map {@code boxes}
     *
     * @param ship player's {@code Ship}
     * @param x    {@code int} coordinate
     * @param y    {@code int} coordinate
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        // remove all boxes
        Map<BoxType, Integer> emptyStorage = new HashMap<>();
        emptyStorage.put(BoxType.BLUE, 0);
        emptyStorage.put(BoxType.RED, 0);
        emptyStorage.put(BoxType.YELLOW, 0);
        emptyStorage.put(BoxType.GREEN, 0);

        ship.setBoxes(emptyStorage, x, y);
    }

    @Override
    void drawContent(StringBuilder sb) {
        String storage = "r".repeat(boxes.get(BoxType.RED)) +
                "y".repeat(boxes.get(BoxType.YELLOW)) +
                "b".repeat(boxes.get(BoxType.BLUE)) +
                "g".repeat(boxes.get(BoxType.GREEN)) +
                "-".repeat(maxBoxes - boxes.values().stream().mapToInt(Integer::intValue).sum());

        sb.append("│ ").append(centerText(storage, boxWidth - 4)).append(" │").append("\n");
    }
}
