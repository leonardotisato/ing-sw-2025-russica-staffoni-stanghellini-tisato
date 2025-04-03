package it.polimi.ingsw.cg04.model.utils;
import java.util.List;
import java.util.Objects;

public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean equals(Coordinates other) {
        if (this == other) return true;
        return this.x == other.x && this.y == other.y;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean isIn(List<Coordinates> coordinates) {
        for (Coordinates c : coordinates) {
            if (equals(c)) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

