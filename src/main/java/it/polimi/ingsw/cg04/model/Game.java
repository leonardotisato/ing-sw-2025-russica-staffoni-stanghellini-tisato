package it.polimi.ingsw.cg04.model;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.GameState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.enumerations.PlayerState;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.CardLoader;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.cg04.model.utils.TileLoader;

public class Game{
    private int maxPlayers;
    private int numPlayers;
    private int level;
    private List<Player> players;
    private FlightBoard board;
    private GameState gameState;
    private AdventureCard currentAdventureCard;
    private Bank bank;
    private List<List<Integer>> preFlightPiles;
    private List<Integer> level1Cards;
    private List<Integer> level2Cards;
    private Map<Integer, AdventureCard> adventureCardsMap;
    private List<Integer> adventureCardsDeck;
    private Map<Integer, Tile> tilesDeckMap;
    private List<Integer> faceDownTiles;
    private List<Integer> faceUpTiles;

    // for testing purposes
    private final Random seed =  new Random(42);

    public Game(int level, String jsonFilePathCards, String jsonFilePathTiles) {
        this.maxPlayers = 0;
        this.numPlayers = 0;
        this.level = level;
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
        this.faceDownTiles = new ArrayList<>();
        this.faceUpTiles = new ArrayList<>();
        this.tilesDeckMap = TileLoader.loadTilesFromJson(jsonFilePathTiles, this.faceDownTiles);
        this.gameState = GameState.START;
    }

    public int getLevel() {
        return this.level;
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

    // todo: a che serve?
    public void setBoard(FlightBoard board){
        this.board = board;
    }

    public void createAdventureDeck(){
        this.adventureCardsDeck = new ArrayList<>();
        for (List<Integer> pile : preFlightPiles){
            this.adventureCardsDeck.addAll(pile);
        }
        Collections.shuffle(this.adventureCardsDeck, seed);
    }

    public List<Player> getPlayers(){
        return players;
    }

    public List<Player> getSortedPlayers() {
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

        List<Player> playersByPosition = this.getSortedPlayers();

        return playersByPosition.get(ranking);
    }

    public AdventureCard getCardById(Integer id){
        return adventureCardsMap.get(id);
    }

    public Tile getTileById(Integer id){
        return tilesDeckMap.get(id);
    }

    public List<Integer> getFaceDownTiles(){
        return faceDownTiles;
    }

    public List<Integer> getFaceUpTiles(){
        return faceUpTiles;
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

    // todo: also considers players that lost or retired!!
    public void calculateBestShip(){
        int minConnectors = players.stream()
                .mapToInt(p -> p.getShip().getNumExposedConnectors())
                .min()
                .orElse(0);

        List<Player> minPlayers = players.stream()
                .filter(p -> p.getShip().getNumExposedConnectors() == minConnectors)
                .collect(Collectors.toList());

        for(Player p : minPlayers){
            p.updateCredits(this.board.getMostBeautifulShipCredits());
        }
    }

    // todo: fare che solo i players che arrivano alla fine, quindi non PlayerState == RETIRED or LOST, ricevano le ricompense
    public void giveEndCredits(){

        List<Player> playersByPosition = this.getSortedPlayers();

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

    public void placeTile(Player player, int x, int y){
        player.placeTile(x, y);
    }

    public void chooseFaceUpTile(Player player, int index) {
        if (index < 0 || index >= this.faceUpTiles.size()) {
            throw new IndexOutOfBoundsException();
        }

        int selectedTileId = faceUpTiles.remove(index);
        Tile selectedTile = tilesDeckMap.get(selectedTileId);
        player.setHeldTile(selectedTile);
    }

    public void pickFaceDownTile(Player player) {
        if (faceDownTiles.isEmpty()) {
            throw new RuntimeException("No face down tiles available");
        }

        int tileId = faceDownTiles.removeFirst();
        Tile tile = getTileById(tileId);
        player.setHeldTile(tile);
    }

    public void updateCredits(Player player, int delta) {
        player.updateCredits(delta);
    }

    public AdventureCard getNextAdventureCard(){
        if (this.adventureCardsDeck.isEmpty()) return null;
        this.currentAdventureCard = this.adventureCardsMap.get(adventureCardsDeck.removeFirst());
        return this.currentAdventureCard;
    }


    public void buildPiles() {
        Collections.shuffle(this.level1Cards, seed);
        Collections.shuffle(this.level2Cards, seed);
        for (List<Integer> pile : this.preFlightPiles){
            pile.add(this.level2Cards.removeFirst());
            pile.add(this.level2Cards.removeFirst());
            pile.add(this.level1Cards.removeFirst());
        }
    }


    public void bookTile(Player player) {
        player.bookTile();
    }

    public void returnTile(Player player) {
        Integer collectedTileId = player.returnTile();
        faceUpTiles.add(collectedTileId);
    }

    public void chooseBookedTile(Player player, int idx) {
        player.chooseBookedTile(idx);
    }

    public void showFaceUpTiles(Player player) {
        player.showFaceUpTiles();
    }

    public void showPile(Player player, int idx) {
        player.showPile(idx);
    }

    public void returnPile(Player player) {
        player.returnPile();
    }

    public void loadResource(Player player, int x, int y, BoxType box) {
        player.loadResource(x,y, box);
    }

    public void removeResource(Player player, int x, int y, BoxType box) {
        player.removeResource(x,y,box);
    }

    public void removeCrew(Player player, int x, int y) {
        player.removeCrew(x,y);
    }

    public void useBattery(Player player, int x, int y) {
        player.useBattery(x,y);
    }

    public void setPlayerState(Player player, PlayerState state) {
        player.setState(state);
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public void beginGame() {
        this.setGameState(GameState.BUILDING);
    }

    public void endGame() {
        this.setGameState(GameState.END);
    }
}