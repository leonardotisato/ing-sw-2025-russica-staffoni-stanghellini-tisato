package it.polimi.ingsw.cg04.model.enumerations;

public enum BoxType {
    RED(1),
    YELLOW(2),
    GREEN(3),
    BLUE(4);

    private final int priority;

    BoxType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

