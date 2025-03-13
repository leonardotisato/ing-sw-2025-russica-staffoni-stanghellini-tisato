package it.polimi.ingsw.cg04.model;
import com.google.gson.*;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.GameState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.enumerations.PlayerState;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.CardLoader;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.cg04.model.utils.TileLoader;

import java.lang.reflect.Type;

public class Game{
    private int maxPlayers;
    private int numPlayers;
    private List<Player> players;
    private FlightBoard board;
    private GameState gameState;
    private AdventureCard currentAdventureCard;
    private Bank bank;
    private List<List<Integer>>preFlightPiles;
    private List<Integer> level1Cards;
    private List<Integer> level2Cards;
    private Map<Integer, AdventureCard> adventureCardsMap;
    private List<Integer> adventureCardsDeck;
    private Map<Integer, Tile> TilesDeckMap;
    private List<Integer> faceDownComponents;
    private List<Integer> faceUpComponents;

    public Game(int level, String jsonFilePathCards, String jsonFilePathTiles) {
        this.maxPlayers = 0;
        this.numPlayers = 0;
        this.players = new ArrayList<Player>();
        if(level == 1) this.board = new FlightBoardLev1();
        else if (level == 2) this.board = new FlightBoardLev2();
        this.level1Cards = new ArrayList<>();
        this.level2Cards = new ArrayList<>();
        this.adventureCardsMap = CardLoader.loadCardsFromJson(jsonFilePathCards, this.level1Cards, this.level2Cards);
        this.preFlightPiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            preFlightPiles.add(new ArrayList<>());
        }
        this.adventureCardsDeck = new ArrayList<>();
        this.faceDownComponents = new ArrayList<>();
        this.faceUpComponents = new ArrayList<>();
        this.TilesDeckMap = TileLoader.loadTilesFromJson(jsonFilePathTiles, this.faceDownComponents);
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

    public void addPlayer(String name, PlayerColor color) throws IllegalArgumentException{
        // check name already taken
        if(this.isNameTaken(name)) throw new IllegalArgumentException();
        this.players.add(new Player(name, color, this));
    }

    public void removePlayer(String name){
        this.players.removeIf(p -> p.getName().equals(name));
    }

    public void setBoard(FlightBoard board){
        this.board = board;
    }

    public void createAdventureDeck(){
        this.adventureCardsDeck = new ArrayList<>();
        for (List<Integer> pile : preFlightPiles){
            this.adventureCardsDeck.addAll(pile);
        }
        Collections.shuffle(this.adventureCardsDeck);
    }

    public List<Player> getPlayers(){
        return players;
    }

    public List<Player> getPlayersByPosition() {
        return players.stream().sorted(Comparator.comparingInt(Player::getPosition).reversed()).collect(Collectors.toList());
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

    public Player getPlayer(int ranking){

        List<Player> playersByPosition = this.getPlayersByPosition();

        return playersByPosition.get(ranking);
    }

    public AdventureCard getCardById(Integer id){
        return adventureCardsMap.get(id);
    }

    public Tile getTileById(Integer id){
        return TilesDeckMap.get(id);
    }

    public List<Integer> getFaceDownComponents(){
        return faceDownComponents;
    }

    public List<Integer> getFaceUpComponents(){
        return faceUpComponents;
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
        for (Player p : players){
            if (!p.getShip().isShipLegal()) p.setState(PlayerState.SHIP_CORRECTION);
            else p.setState(PlayerState.FLIGHT);
        }
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
            p.updateCredits(this.board.giveMostBeautifulShipCredits());
        }
    }

    // todo: fare che solo i players che arrivano alla fine, quindi non PlayerState == RETIRED or LOST, ricevano le ricompense
    public void giveEndCredits(){

        List<Player> playersByPosition = this.getPlayersByPosition();

        for (int i = 0; i < playersByPosition.size(); i++) {
            playersByPosition.get(i).updateCredits(this.board.endGameCredits.get(i));
        }
    }

    public void handleEndGame(){
        this.giveEndCredits();
        this.calculateBestShip();
    }

    public void movePlayer(Player player, int steps){
        player.move(steps);
    }

    public void placeTile(Player player, Tile tile, int x, int y){
        player.placeTile(tile, x, y);
    }

    public void chooseFaceUpTile(Player player, int index) {
      player.chooseFaceUpTile(index);
    }

    public void pickFaceDownTile(Player player, Tile tile) {
        player.pickFaceDownTile();
    }

    public void updateCredits(Player player, int delta) {
        player.updateCredits(delta);
    }

    public AdventureCard getAdventureCard(){
        if (this.adventureCardsDeck.isEmpty()) return null;
        this.currentAdventureCard = this.adventureCardsMap.get(adventureCardsDeck.removeFirst());
        return this.currentAdventureCard;
    }


    public void buildPiles() {
        Collections.shuffle(this.level1Cards);
        Collections.shuffle(this.level2Cards);
        for (List<Integer> pile : this.preFlightPiles){
            pile.add(this.level2Cards.removeFirst());
            pile.add(this.level2Cards.removeFirst());
            pile.add(this.level1Cards.removeFirst());
        }
    }










}