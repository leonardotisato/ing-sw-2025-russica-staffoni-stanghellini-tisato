package it.polimi.ingsw.cg04.model;
import com.google.gson.*;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.GameState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;
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
    private List<Tile> faceDownComponents;
    private List<Tile> faceUpComponents;

    public Game(int level, String jsonFilePath){
        this.maxPlayers = 0;
        this.numPlayers = 0;
        this.players = new ArrayList<Player>();
        if(level == 1) this.board = new FlightBoardLev1();
        else if (level == 2) this.board = new FlightBoardLev2();
        this.level1Cards = new ArrayList<>();
        this.level2Cards = new ArrayList<>();
        this.adventureCardsMap = loadCardsFromJson(jsonFilePath);
        this.preFlightPiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            preFlightPiles.add(new ArrayList<>());
        }
        buildPiles();
        this.adventureCardsDeck = new ArrayList<>();
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
            p.addCredits(this.board.giveMostBeautifulShipCredits());
        }
    }

    // todo: fare che solo i players che arrivano alla fine, quindi non PlayerState == RETIRED or LOST, ricevano le ricompense
    public void giveEndCredits(){

        List<Player> playersByPosition = this.getPlayersByPosition();

        for (int i = 0; i < playersByPosition.size(); i++) {
            playersByPosition.get(i).addCredits(this.board.endGameCredits.get(i));
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

    public void addCredits(Player player, int credits) {
        player.addCredits(credits);
    }

    public void removeCredits(Player player, int credits) {
        player.removeCredits(credits);
    }

    public AdventureCard getAdventureCard(){
        if (this.adventureCardsDeck.isEmpty()) return null;
        this.currentAdventureCard = this.adventureCardsMap.get(adventureCardsDeck.removeFirst());
        return this.currentAdventureCard;
    }

    private AdventureCard createCardFromJson(JsonObject jsonObject) {
        String type = jsonObject.get("type").getAsString();
        return switch (type) {
            case "AbandonedShip" -> new Gson().fromJson(jsonObject, AbandonedShip.class);
            case "AbandonedStation" -> new Gson().fromJson(jsonObject, AbandonedStation.class);
            case "Epidemic" -> new Gson().fromJson(jsonObject, Epidemic.class);
            case "MeteorsRain" -> new Gson().fromJson(jsonObject, MeteorsRain.class);
            case "OpenSpace" -> new Gson().fromJson(jsonObject, OpenSpace.class);
            case "Pirates" -> new Gson().fromJson(jsonObject, Pirates.class);
            case "Planets" -> new Gson().fromJson(jsonObject, Planets.class);
            case "Slavers" -> new Gson().fromJson(jsonObject, Slavers.class);
            case "Smugglers" -> new Gson().fromJson(jsonObject, Smugglers.class);
            case "Stardust" -> new Gson().fromJson(jsonObject, Stardust.class);
            case "WarZone" -> new Gson().fromJson(jsonObject, WarZone.class);
            default -> throw new JsonParseException("Tipo sconosciuto: " + type);
        };
        }

    private Map<Integer, AdventureCard> loadCardsFromJson(String jsonFilePath) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(jsonFilePath)) {
            Type mapType = new TypeToken<Map<String, JsonObject>>() {}.getType();
            Map<String, JsonObject> tempMap = gson.fromJson(reader, mapType);

            Map<Integer, AdventureCard> cardMap = new HashMap<>();
            for (Map.Entry<String, JsonObject> entry : tempMap.entrySet()) {
                int id = Integer.parseInt(entry.getKey());
                AdventureCard card = createCardFromJson(entry.getValue());
                cardMap.put(id, card);
                if (id == 1) this.level1Cards.add(id);
                else if (id == 2) this.level2Cards.add(id);
            }
            return cardMap;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void buildPiles() {
        Collections.shuffle(this.level1Cards);
        Collections.shuffle(this.level2Cards);
        for (List<Integer> pile : this.preFlightPiles){
            pile.add(this.level2Cards.removeFirst());
            pile.add(this.level2Cards.removeFirst());
            pile.add(this.level1Cards.removeFirst());
            //da mettere in board?
        }
    }










}