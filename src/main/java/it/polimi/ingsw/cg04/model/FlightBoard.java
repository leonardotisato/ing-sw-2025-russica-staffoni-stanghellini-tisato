package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.advetureCards.AdventureCard;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.util.Random;
import java.util.List;
import java.util.Map;

public abstract class FlightBoard {
    private Player[] path;
    private int pathSize;
    private Map<Integer, Integer> startingPosition;
    private Bank bank;
    private List<Component> faceDownComponents;
    private List<Component> faceUpComponents;
    private AdventureCard[][] preFlightPiles;
    private List<AdventureCard> AdventureCardsDeck;
    private int dices;
    private Map<Integer,Integer> endGameCredits;


    private FlightBoard(){
        path = null;
        pathSize = 0;
        startingPosition = null;
        bank = null;
        faceDownComponents = null;
        faceUpComponents = null;
        preFlightPiles = null;
        AdventureCardsDeck = null;
        dices = 0;
        endGameCredits = null;
    }
    public List<Component> getFaceDownComponents(){
        return faceDownComponents;
    }

    public List<Component> getFaceUpComponents(){
        return faceUpComponents;
    }

    public AdventureCard[] getPreFlightsPiles(int num){
        return preFlightPiles[num];
    }

    public AdventureCard getAdventureCard(){
        if (this.AdventureCardsDeck.isEmpty()) return null;
        return AdventureCardsDeck.remove(this.AdventureCardsDeck.size() - 1);
    }

    public int rollDices(){
        Random rand = new Random();
        int dice1 = Random.nextInt(1, 7);
        int dice2 = rand.nextInt(1, 7);
        return dice1 + dice2;
    }

    private void createPreFlightPiles(){
    }

    private void createAdventureCardsDeck(){
    }




}
