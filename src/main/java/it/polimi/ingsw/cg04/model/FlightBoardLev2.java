package it.polimi.ingsw.cg04.model;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlightBoardLev2 extends FlightBoard {
    private float timer;
    private int timerState;
    private int numStates;

    public FlightBoardLev2(){
        this.path = new Player[24];
        this.pathSize = 24;
        this.startingPosition = new HashMap<Integer, Integer>();
        this.startingPosition.put(1, 6);
        this.startingPosition.put(2, 3);
        this.startingPosition.put(3, 1);
        this.startingPosition.put(4, 0);
        this.bank = new Bank(9999, 9999, 9999, 9999, null);
        this.faceDownComponents = new ArrayList<>();
        this.faceUpComponents = new ArrayList<>();
        this.preFlightPiles = null;
        this.adventureCardsDeck = new ArrayList();
        this.endGameCredits = new HashMap<Integer, Integer>();
        this.endGameCredits.put(1, 8);
        this.endGameCredits.put(2, 6);
        this.endGameCredits.put(3, 4);
        this.endGameCredits.put(4, 2);
        this.timer = 0;
        this.timerState = 0;
        this.numStates = 3;
        this.mostBeautifulShipCredits = 4;
    }

    public void startTimer(){
    }

    public int getTimerState() {
        return timerState;
    }






}
