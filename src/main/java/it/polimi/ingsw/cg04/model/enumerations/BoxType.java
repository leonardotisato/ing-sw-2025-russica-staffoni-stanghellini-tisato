package it.polimi.ingsw.cg04.model.enumerations;

public enum BoxType {
    RED(4),
    YELLOW(3),
    GREEN(2),
    BLUE(1);

    private final int priority;

    BoxType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

