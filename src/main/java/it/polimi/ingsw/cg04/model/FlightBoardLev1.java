package it.polimi.ingsw.cg04.model;

import java.util.ArrayList;
import java.util.HashMap;

public class FlightBoardLev1 extends FlightBoard{
    public FlightBoardLev1(){
        super();
        this.path = new Player[18];
        this.pathSize = 18;
        this.startingPosition = new HashMap<Integer, Integer>();
        this.startingPosition.put(1, 4);
        this.startingPosition.put(2, 2);
        this.startingPosition.put(3, 1);
        this.startingPosition.put(4, 0);
        this.bank = new Bank(9999, 9999, 9999, 9999, null);
        this.endGameCredits = new HashMap<Integer, Integer>();
        this.endGameCredits.put(1, 4);
        this.endGameCredits.put(2, 3);
        this.endGameCredits.put(3, 2);
        this.endGameCredits.put(4, 1);
        this.mostBeautifulShipCredits = 2;
    }



}
