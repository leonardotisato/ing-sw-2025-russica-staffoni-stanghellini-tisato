package it.polimi.ingsw.cg04.model.utils;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Coordinates implements Serializable {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinates other = (Coordinates) obj;
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

