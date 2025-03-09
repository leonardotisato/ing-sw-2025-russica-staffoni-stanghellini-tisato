package it.polimi.ingsw.cg04.model;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.advetureCards.AdventureCard;
import java.util.Random;
import java.util.List;
import java.util.Map;

public abstract class FlightBoard {
    private Player[] path;
    private int pathSize;
    private Map<Integer, Integer> startingPosition;
    private Bank bank;
    private List<Tile> faceDownTiles;
    private List<Tile> faceUpTiles;
    private AdventureCard[][] preFlightPiles;
    private List<AdventureCard> AdventureCardsDeck;
    private int dices;
    private Map<Integer,Integer> endGameCredits;


    private FlightBoard(){
        path = null;
        pathSize = 0;
        startingPosition = null;
        bank = null;
        faceDownTiles = null;
        faceUpTiles = null;
        preFlightPiles = null;
        AdventureCardsDeck = null;
        dices = 0;
        endGameCredits = null;
    }
    public List<Tile> getFaceDownTiles(){
        return faceDownTiles;
    }

    public List<Tile> getFaceUpTiles(){
        return faceUpTiles;
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
        int dice1 = rand.nextInt(1, 7);
        int dice2 = rand.nextInt(1, 7);
        return dice1 + dice2;
    }

    private void createPreFlightPiles(){
    }

    private void createAdventureCardsDeck(){
    }




}
