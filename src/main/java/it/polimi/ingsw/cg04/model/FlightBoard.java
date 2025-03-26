package it.polimi.ingsw.cg04.model;

import java.util.Map;

public abstract class FlightBoard {
    protected Player[] path;
    protected int pathSize;
    protected Map<Integer, Integer> startingPosition;
    protected Bank bank;
    protected Map<Integer,Integer> endGameCredits;
    protected int mostBeautifulShipCredits;


    public FlightBoard(){
        path = null;
        pathSize = 0;
        startingPosition = null;
        bank = null;
        endGameCredits = null;
        mostBeautifulShipCredits = 0;
    }

    public int getPathSize() {
        return pathSize;
    }

    public Player[] getPath() {
        return path;
    }

    public Player getCell(int idx) {
        return path[idx];
    }

    public void occupyCell(int cell, Player player) {
        int mapIndex = cell % pathSize;
        path[mapIndex] = player;
    }

    public int getStartingPosition(int idx) {
        return startingPosition.get(idx);
    }

    public void freeCell(int cell) {
        int mapIndex = cell % pathSize;
        path[mapIndex] = null;
    }

    public int move(Player player, int delta) {

        int oldPlayerCell = -1;
        for (int i = 0; i < pathSize; i++) {
            if (path[i] != null && path[i].equals(player)) {
                oldPlayerCell = i;
                break;
            }
        }
        assert oldPlayerCell != -1;

        int newCell = oldPlayerCell;
        int stepsTaken = 0;

        if (delta > 0) {
            while (stepsTaken < delta) {
                newCell = (newCell + 1) % pathSize;
                if (path[newCell] == null) {
                    stepsTaken++;
                }
            }
        } else if (delta < 0) {
            while (stepsTaken > delta) {
                newCell = (newCell - 1 + pathSize) % pathSize;
                if (path[newCell] == null) {
                    stepsTaken--;
                }
            }
        }

        freeCell(oldPlayerCell);
        occupyCell(newCell, player);

        return newCell;
    }

    public int giveMostBeautifulShipCredits(){
        return mostBeautifulShipCredits;
    }

}
