package it.polimi.ingsw.cg04.model.enumerations;

public enum BoxType {
    RED(1, 4),
    YELLOW(2, 3),
    GREEN(3, 2),
    BLUE(4, 1);

    private final int priority;
    private final int value;

    BoxType(int priority, int value) {
        this.priority = priority;
        this.value = value;
    }

    public int getPriority() {
        return priority;
    }

    public int getValue() {
        return value;
    }
}

