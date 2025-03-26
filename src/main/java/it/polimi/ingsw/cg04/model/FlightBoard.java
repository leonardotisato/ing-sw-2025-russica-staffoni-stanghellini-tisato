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
        player.setCurrentCell(mapIndex);
    }

    public int getStartingPosition(int idx) {
        return startingPosition.get(idx);
    }

    public void freeCell(int cell) {
        int mapIndex = cell % pathSize;
        path[mapIndex] = null;
    }

    public int move(Player player, int delta) {

        int oldPlayerCell = player.getCurrentCell();

        int newCell = oldPlayerCell;
        int stepsTaken = 0;

        if (delta > 0) {
            while (stepsTaken < delta) {
                newCell = (newCell + 1) % pathSize;
                if (path[newCell] == null) {
                    stepsTaken++;
                }
            }

            // if newCell < oldPlayerCell --> player.addLoop()
            if (newCell < oldPlayerCell) {
                player.addLoop();
            }

        } else if (delta < 0) {
            while (stepsTaken > delta) {
                newCell = (newCell - 1 + pathSize) % pathSize;
                if (path[newCell] == null) {
                    stepsTaken--;
                }
            }

            // if newCell > oldPlayerCell --> player.removeLoop()
            if (newCell > oldPlayerCell) {
                player.removeLoop();
            }
        }

        freeCell(oldPlayerCell);
        occupyCell(newCell, player);

        return newCell;
    }

    public int getMostBeautifulShipCredits(){
        return mostBeautifulShipCredits;
    }

}
