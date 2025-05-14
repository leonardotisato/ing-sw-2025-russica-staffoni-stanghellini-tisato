package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.*;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class HousingTile extends Tile {

    private int numCrew;
    private CrewType hostedCrewType = null;
    private final List<CrewType> supportedCrewType = new ArrayList<>();

    @Expose
    private boolean isCentralTile;
    private PlayerColor color;

    public HousingTile() {
        super();
        this.shortName = "Housing";
    }

    public HousingTile(PlayerColor playerColor) {
        super(playerColor);
        color = playerColor;
        isCentralTile = true;
        this.shortName = "Housing";

        switch (playerColor) {
            case RED:
                this.tileColor = "\u001B[31m";
                break;
            case GREEN:
                this.tileColor = "\u001B[32m";
                break;
            case BLUE:
                this.tileColor = "\u001B[34m";
                break;
            case YELLOW:
                this.tileColor = "\u001B[38;5;220m";
                break;
        }
    }

    public Boolean isCentralTile() {
        return isCentralTile;
    }

    public void setIsCentralTile(boolean isCentralTile) {
        this.isCentralTile = isCentralTile;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public void updateSupportedCrewTypes() {
        // if (!isCentralTile || no supports) { supportedCrewType = set(HUMANS)
        // else supportedCrewType can host some aliens...
    }

    public void addSupportedCrewType(CrewType crewType) {
        if (crewType == CrewType.HUMAN || !isCentralTile) {
            supportedCrewType.add(crewType);
        }
    }

    public void removeSupportedCrewType(CrewType crewType) {
        supportedCrewType.remove(crewType);
    }

    public List<CrewType> getSupportedCrewType() {
        return supportedCrewType;
    }

    public void addCrew(CrewType type) {
        if (type == null) {
            throw new RuntimeException("CrewType cannot be null");
        }

        if (!supportedCrewType.contains(type)) {
            throw new RuntimeException("Crew type:" + type + " is not supported");
        }

        if ((hostedCrewType == CrewType.HUMAN && numCrew == 2) || numCrew == 1) {
            throw new RuntimeException("housing tile already filled");
        }

        hostedCrewType = type;
        if (hostedCrewType == CrewType.HUMAN) {
            numCrew = 2;
        } else {
            numCrew = 1;
        }

    }

    public Integer getNumCrew() {
        return numCrew;
    }

    public CrewType getHostedCrewType() {
        return hostedCrewType;
    }

    public CrewType removeCrewMember() {
        if (numCrew <= 0) {
            throw new RuntimeException("Not enough crew members");
        }
        numCrew = numCrew - 1;
        CrewType removed = hostedCrewType;
        if (numCrew == 0) {
            hostedCrewType = null;
        }
        return removed;
    }

    /**
     * removes aliens from ship's {@code crewMap} attribute
     * removes this tile from connected AlienSupportTile's list {@code adjacentHousingTiles}
     *
     * @param ship
     * @param x    int
     * @param y    int
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        // may occur that HousingTile is empty when this methode is called
        if (hostedCrewType != null) {
            ship.removeCrew(hostedCrewType, x, y, numCrew);
        }

        // remove this tile from adjacentHousingTile in relative AlienSupportTile
        Tile[][] tilesMatrix = ship.getTilesMatrix();
        List<Coordinates> alienSupportTilesIdx = ship.getTilesMap().get("AlienSupportTile");

        for (int i = 0; i < tilesMatrix[0].length - 1; i++) {
            for (int j = 0; j < tilesMatrix.length - 1; j++) {
                if (new Coordinates(i, j).isIn(alienSupportTilesIdx)
                        && tilesMatrix[i][j].getAdjacentHousingTiles().contains(this)) {
                    tilesMatrix[i][j].removeAdjacentHousingTile(this);
                }
            }
        }

        // clear in case of future replacement
        supportedCrewType.clear();
    }

    /**
     * adds HUMANS to this tile {@code supportedCrewType},
     * adds 2 HUMANS to this tile and ship attribute {@code crewMap}
     * if this tile is connected to a {@code AlienSupportTile} removes the humans,
     * adds the adjacent {@code AlienSupportTile}'s {@code supportedAlienColor} attribute to {@code this.supportedCrewType}
     * adds this tile to the connected {@code AlienSupportTile} list {@code adjacentHousingTiles}
     *
     * @param ship
     * @param x    int
     * @param y    int
     */
    @Override
    public void place(Ship ship, int x, int y) {
        // every HousingTile supports humans
        this.addSupportedCrewType(CrewType.HUMAN);
        hostedCrewType = CrewType.HUMAN;

        ship.addCrew(CrewType.HUMAN, x, y); // adds crew members both in this tile and ship

        int shipHeight = ship.getTilesMatrix().length;
        int shipWidth = ship.getTilesMatrix()[0].length;
        Tile[][] tilesMatrix = ship.getTilesMatrix();
        List<Coordinates> alienSupportTilesIdx = ship.getTilesMap().get("AlienSupportTile");

        for (int i = 0; i < shipHeight; i++) {
            for (int j = 0; j < shipWidth; j++) {
                if (tilesMatrix[i][j] != null && tilesMatrix[i][j].equals(this)) {

                    // look UP -> check tile type and connection
                    if (i > 0 && new Coordinates(i - 1, j).isIn(alienSupportTilesIdx)
                            && this.isValidConnection(Direction.UP, tilesMatrix[i - 1][j])
                            && this.getConnection(Direction.UP) != Connection.EMPTY) {
                        tilesMatrix[i - 1][j].addAdjacentHousingTile(this);
                        // remove humans, leave the choice to the player

                        // UNCOMMENT THE FOLLOWING LINE TO APLY THE LOGGIC THAT REMOVES HUMANS
                        // IN CASE A HOUSING SUPPORT TILE SUPPORTS ONE OR MORE ALIEN TYPES
                        //ship.removeCrew(CrewType.HUMAN, x, y, 2); // removes humans in both ship and tile attributes
                        this.addSupportedCrewType(tilesMatrix[i - 1][j].getSupportedAlienColor());
                    }

                    // look LEFT -> check tile type and connection
                    if (j > 0 && new Coordinates(i, j - 1).isIn(alienSupportTilesIdx)
                            && this.isValidConnection(Direction.LEFT, tilesMatrix[i][j - 1])
                            && this.getConnection(Direction.LEFT) != Connection.EMPTY) {
                        tilesMatrix[i][j - 1].addAdjacentHousingTile(this);
                        // remove humans, leave the choice to the player

                        // UNCOMMENT THE FOLLOWING LINE TO APLY THE LOGGIC THAT REMOVES HUMANS
                        // IN CASE A HOUSING SUPPORT TILE SUPPORTS ONE OR MORE ALIEN TYPES
                        //ship.removeCrew(CrewType.HUMAN, x, y, 2);
                        this.addSupportedCrewType(tilesMatrix[i][j - 1].getSupportedAlienColor());
                    }

                    // look RIGHT -> check tile type and connection
                    if (j < shipWidth - 1 && new Coordinates(i, j + 1).isIn(alienSupportTilesIdx)
                            && this.isValidConnection(Direction.RIGHT, tilesMatrix[i][j + 1])
                            && this.getConnection(Direction.RIGHT) != Connection.EMPTY) {
                        tilesMatrix[i][j + 1].addAdjacentHousingTile(this);
                        // remove humans, leave the choice to the player

                        // UNCOMMENT THE FOLLOWING LINE TO APLY THE LOGGIC THAT REMOVES HUMANS
                        // IN CASE A HOUSING SUPPORT TILE SUPPORTS ONE OR MORE ALIEN TYPES
                        //ship.removeCrew(CrewType.HUMAN, x, y, 2);
                        this.addSupportedCrewType(tilesMatrix[i][j + 1].getSupportedAlienColor());
                    }

                    // look DOWN -> check tile type and connection
                    if (i < shipHeight - 1 && new Coordinates(i + 1, j).isIn(alienSupportTilesIdx)
                            && this.isValidConnection(Direction.DOWN, tilesMatrix[i + 1][j])
                            && this.getConnection(Direction.DOWN) != Connection.EMPTY) {
                        tilesMatrix[i + 1][j].addAdjacentHousingTile(this);
                        // remove humans, leave the choice to the player

                        // UNCOMMENT THE FOLLOWING LINE TO APLY THE LOGGIC THAT REMOVES HUMANS
                        // IN CASE A HOUSING SUPPORT TILE SUPPORTS ONE OR MORE ALIEN TYPES
                        //ship.removeCrew(CrewType.HUMAN, x, y, 2);
                        this.addSupportedCrewType(tilesMatrix[i + 1][j].getSupportedAlienColor());
                    }
                    return;
                }
            }
        }
    }

    void drawContent(StringBuilder sb) {
        StringBuilder housing = new StringBuilder();

        if (hostedCrewType == null) {
            housing.append("--");
        } else if (hostedCrewType == CrewType.HUMAN) {
            housing.append("h".repeat(numCrew));
        } else if (hostedCrewType == CrewType.BROWN_ALIEN) {
            housing.append("b");
        } else if (hostedCrewType == CrewType.PINK_ALIEN) {
            housing.append("p");
        }

        sb.append("│ ").append(centerText(housing.toString(), boxWidth - 4)).append(" │").append("\n");
    }
}
