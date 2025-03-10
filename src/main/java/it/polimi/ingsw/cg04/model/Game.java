package it.polimi.ingsw.cg04.model;
import it.polimi.ingsw.cg04.model.enumerations.GameState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Game{
    private int maxPlayers;
    private int numPlayers;
    private List<Player> players;
    private FlightBoard board;
    private GameState gameState;
    private Bank bank;

    public Game(int level){
        this.maxPlayers = 0;
        this.numPlayers = 0;
        this.players = new ArrayList<Player>();
        if(level == 1) this.board = new FlightBoardLev1();
        else if (level == 2) this.board = new FlightBoardLev2();
        this.gameState = GameState.START;
    }

    public void setNumPlayers(int num){
        this.numPlayers = num;
        this.maxPlayers = num;
    }

    private boolean isNameTaken(String name){
        for (Player p : players){
            if(p.getName().equals(name)) return true;
        }
        return false;
    }

    public void addPlayer(String name, PlayerColor color) throws NameAlreadyTakenException{
        // check name already taken
        if(this.isNameTaken(name)) throw new NameAlreadyTakenException();
        this.players.add(new Player(name, color, this));
    }

    public void removePlayer(String name){
        this.players.removeIf(p -> p.getName().equals(name));
    }

    public void setBoard(FlightBoard board){
        this.board = board;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public FlightBoard getBoard() {
        return board;
    }

    public Player getPlayer(String name){
        for (Player p : players){
            if(p.getName().equals(name)) return p;
        }
        return null;
    }

    public Player getPlayer(int position){
        for (Player p : players){
            if(p.getPosition() == position) return p;
        }
        return null;
    }

    public int rollDices(){
        Random rand = new Random();
        int dice1 = rand.nextInt(1, 7);
        int dice2 = rand.nextInt(1, 7);
        return dice1 + dice2;
    }

    public void startBuildPhase(){
        this.gameState = GameState.BUILDING;
    }

    public void startFLIGHT(){
        this.gameState = GameState.FLIGHT;
    }

    public void checkShips(){

    }

    public void calculateBestShip(){
        int minConnectors = players.stream()
                .mapToInt(p -> p.getShip().getNumExposedConnectors())
                .min()
                .orElse(0);

        List<Player> minPlayers = players.stream()
                .filter(p -> p.getShip().getNumExposedConnectors() == minConnectors)
                .collect(Collectors.toList());

        for(Player p : minPlayers){
            p.addCredits(this.board.getMostBeautifulShipCredits());
        }
    }

    public void giveEndCredits(){
        for (Player p : this.players){
            p.addCredits(this.board.endGameCredits.get(p.getPosition()));
        }
    }

    public void handleEndGame(){
        this.giveEndCredits();
        this.calculateBestShip();
    }



}