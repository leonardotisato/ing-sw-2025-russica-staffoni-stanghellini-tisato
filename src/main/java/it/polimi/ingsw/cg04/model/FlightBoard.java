package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import java.awt.*;
import java.util.List;
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


    public int giveMostBeautifulShipCredits(){
        return mostBeautifulShipCredits;
    }


    private void createPreFlightPiles(){
    }

    private void createAdventureCardsDeck(){
    }

}
