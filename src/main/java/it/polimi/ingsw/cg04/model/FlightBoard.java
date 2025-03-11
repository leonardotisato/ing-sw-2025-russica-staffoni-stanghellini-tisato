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
    protected List<Component> faceDownComponents;
    protected List<Component> faceUpComponents;
    protected AdventureCard[][] preFlightPiles;
    protected List<AdventureCard> adventureCardsDeck;
    protected Map<Integer,Integer> endGameCredits;
    protected int mostBeautifulShipCredits;


    public FlightBoard(){
        path = null;
        pathSize = 0;
        startingPosition = null;
        bank = null;
        faceDownComponents = null;
        faceUpComponents = null;
        preFlightPiles = null;
        adventureCardsDeck = null;
        endGameCredits = null;
        mostBeautifulShipCredits = 0;
    }
    public List<Component> getFaceDownComponents(){
        return faceDownComponents;
    }

    public List<Component> getFaceUpComponents(){
        return faceUpComponents;
    }

    public int giveMostBeautifulShipCredits(){
        return mostBeautifulShipCredits;
    }

    public AdventureCard[] getPreFlightsPiles(int num){
        return preFlightPiles[num];
    }


    private void createPreFlightPiles(){
    }

    private void createAdventureCardsDeck(){
    }

}
