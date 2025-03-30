package it.polimi.ingsw.cg04.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public List<Integer> createAdventureCardsDeck(Game game) {
        List<Integer> adventureCardsDeck = new ArrayList<>(List.of(1, 3, 4, 8, 12, 15, 17, 18));
        Collections.shuffle(adventureCardsDeck, rand);
        return adventureCardsDeck;
    }
}
