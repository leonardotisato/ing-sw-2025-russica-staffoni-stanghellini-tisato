package it.polimi.ingsw.cg04.model;

import java.util.ArrayList;

public class Game{
    private int maxPlayers;
    private int numPlayers;
    private List<Player> players;
    private flightBoard board;
    private State state;
    private Bank bank;

    private Game(){
        this.maxPlayers = 0;
        this.numPlayers = 0;
        this.players = new ArrayList<Player>();
        this.board = new flightBoard();
        this.state = State.START;
    }

    public void setNumPlayers(int num){
        this.numPlayers = num;
        this.maxPlayers = num;
    }

    private boolean isNameTaken(String name){
        for (Player p : players){
            if(p.name.equals(name)) return true;
        }
        return false;
    }

    public void addPlayer(String name, Color color) throws NameAlreadyTakenException{
        // check name alreadytaken
        if(this.isNameTaken(name)) throw new NameAlreadyTakenException();
        this.players.add(new Player(name,color));
    }

    public void removePlayer(String name){
        for (Player p : this.players){
            if(p.name.equals(name)){
                this.players.remove(p);
            }
        }
    }

    public void setBoard(flightBoard board){
        this.board = board;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public flightBoard getBoard() {
        return board;
    }

    public Player getPlayer(String name){
        for (Player p : players){
            if(p.name.equals(name)) return p;
        }
        return null;
    }

    public Player getPlayer(int position){
        for (Player p : players){
            if(p.position == position) return p;
        }
        return null;
    }

    public void startBuildPhase(){
        this.state = State.BUILDING;
    }

    public void startFLIGHT(){
        this.state = State.FLIGHT;
    }

    public void checkShips(){

    }

    public void calculateBestShip(){

    }

    public void handleEndGame(){

    }



}