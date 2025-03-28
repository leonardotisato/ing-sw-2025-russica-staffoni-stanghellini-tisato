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

public class Game {
    private final int maxPlayers;
    private final int level;
    private final List<Player> players;
    private FlightBoard board;
    private GameState gameState;
    private AdventureCard currentAdventureCard;
    private Bank bank;
    private List<List<Integer>> preFlightPiles;
    private final List<Integer> level1Cards;
    private final List<Integer> level2Cards;
    private final Map<Integer, AdventureCard> adventureCardsMap;
    private List<Integer> adventureCardsDeck;
    private final Map<Integer, Tile> tilesDeckMap;
    private final List<Integer> faceDownTiles;
    private final List<Integer> faceUpTiles;

    // for testing purposes
    private final Random rand = new Random();

    public Game(int level, String jsonFilePathCards, String jsonFilePathTiles) {
        this.maxPlayers = 4;
        this.level = level;
        this.players = new ArrayList<Player>();
        if (level == 1) this.board = new FlightBoardLev1();
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

    /**
     * @return the level of the game.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Checks if a given player name is already taken.
     *
     * @param name the name to check.
     * @return {@code true} if the name is taken, {@code false} otherwise.
     */
    private boolean isNameTaken(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * Checks if a given player color is already taken.
     *
     * @param color the color to check.
     * @return {@code true} if the color is taken, {@code false} otherwise.
     */
    private boolean isColorTaken(PlayerColor color) {
        for (Player p : players) {
            if (p.getColor().equals(color)) return true;
        }
        return false;
    }

    /**
     * This method creates and adds a new {@code Player} with the specified name and color.
     * It ensures that the maximum number of players is not exceeded, and that neither
     * the name nor the color is already taken.
     *
     * @param name  the name of the new player.
     * @param color the color assigned to the new player.
     * @return the newly created {@code Player} object.
     * @throws RuntimeException         if the maximum number of players is reached.
     * @throws IllegalArgumentException if the name or color is already taken.
     */
    public Player addPlayer(String name, PlayerColor color) {
        if (players.size() == this.maxPlayers) {
            throw new RuntimeException("Max number of players reached!");
        }
        if (this.isNameTaken(name)) throw new IllegalArgumentException("Name already taken!");
        if (this.isColorTaken(color)) throw new IllegalArgumentException("Color already taken!");

        Player newPlayer = new Player(name, color, this);
        this.players.add(newPlayer);

        return newPlayer;
    }

    /**
     * Removes a player from the game by name.
     *
     * @param name the name of the player to remove.
     * @throws IllegalArgumentException if the name is {@code null} or the player does not exist.
     */
    public void removePlayer(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }

        boolean removed = players.removeIf(p -> p.getName().equals(name));

        if (!removed) {
            throw new IllegalArgumentException("Player with name " + name + " does not exist!");
        }
    }

    /**
     * Retrieves the list of all players currently in the game.
     *
     * @return a list of {@code Player} objects.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Retrieves the list of players sorted by their position in descending order.
     *
     * @return a sorted list of {@code Player} objects based on their position.
     */
    public List<Player> getSortedPlayers() {
        return players.stream().sorted(Comparator.comparingInt(Player::getPosition).reversed()).collect(Collectors.toList());
    }

    /**
     * Returns the number of players currently in the game.
     *
     * @return the number of players.
     */
    public int getNumPlayers() {
        return players.size();
    }

    /**
     * Retrieves the game board.
     *
     * @return the {@code FlightBoard} associated with the game.
     */
    public FlightBoard getBoard() {
        return board;
    }

    /**
     * Retrieves a player by their name.
     *
     * @param name the name of the player to find.
     * @return the {@code Player} object with the specified name.
     * @throws RuntimeException if no player with the given name exists.
     */
    public Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) return p;
        }
        throw new RuntimeException("Player not found!");
    }

    /**
     * Retrieves a player by their ranking position.
     * <p>
     * The ranking is determined based on the players' positions in descending order.
     *
     * @param ranking the ranking position (0-based index).
     * @return the {@code Player} at the specified ranking.
     * @throws IllegalArgumentException if the ranking is out of range.
     */
    public Player getPlayer(int ranking) {
        List<Player> playersByPosition = this.getSortedPlayers();

        if (ranking < 0 || ranking >= playersByPosition.size()) {
            throw new IllegalArgumentException("Invalid ranking: " + ranking);
        }

        return playersByPosition.get(ranking);
    }

    /**
     * Rolls two six-sided dice and returns their sum.
     *
     * @return the sum of two randomly rolled dice (2 to 12).
     */
    public int rollDices() {
        int dice1 = rand.nextInt(1, 7);
        int dice2 = rand.nextInt(1, 7);
        return dice1 + dice2;
    }

    /**
     * Retrieves the current state of the game.
     *
     * @return the {@code GameState} representing the current state of the game.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Sets the current state of the game.
     *
     * @param state the new {@code GameState} to set.
     */
    public void setGameState(GameState state) {
        this.gameState = state;
    }

    // state management

    /**
     * Initiates the build phase of the game.
     * <p>
     * This method sets the game's state to `BUILDING` and updates each player's
     * state to `BUILDING`.
     */
    public void startBuildPhase() {
        this.setGameState(GameState.BUILDING);
        for (Player p : players) {
            p.setState(PlayerState.BUILDING);
        }
    }

    /**
     * Initiates the flight phase of the game.
     * <p>
     * This method sets the game's state to `FLIGHT` and updates each player's
     * state to `FLIGHT`, transitioning them from the building phase to the
     * flight phase.
     */

    public void startFlightPhase() {
        this.setGameState(GameState.FLIGHT);
        for (Player p : players) {
            p.setState(PlayerState.FLIGHT);
        }
    }

    // todo: test
    public void checkShips() {
        for (Player p : players) {
            if (!p.getShip().isShipLegal()) p.setState(PlayerState.SHIP_CORRECTION);
            else p.setState(PlayerState.FLIGHT);
        }
    }

    // ridondante! esiste già startBuildPhase...
    public void beginGame() {
        this.setGameState(GameState.BUILDING);
    }

    public void endGame() {
        this.setGameState(GameState.END);
    }


    // cards and tiles handling

    public void createAdventureDeck() {
        this.adventureCardsDeck = new ArrayList<>();
        for (List<Integer> pile : preFlightPiles) {
            this.adventureCardsDeck.addAll(pile);
        }
        Collections.shuffle(this.adventureCardsDeck, rand);
    }

    public void buildPiles() {
        Collections.shuffle(this.level1Cards, rand);
        Collections.shuffle(this.level2Cards, rand);
        for (List<Integer> pile : this.preFlightPiles) {
            pile.add(this.level2Cards.removeFirst());
            pile.add(this.level2Cards.removeFirst());
            pile.add(this.level1Cards.removeFirst());
        }
    }

    public AdventureCard getCardById(Integer id) {
        return adventureCardsMap.get(id);
    }

    public AdventureCard getNextAdventureCard() {
        if (this.adventureCardsDeck.isEmpty()) return null;
        this.currentAdventureCard = this.adventureCardsMap.get(adventureCardsDeck.removeFirst());
        return this.currentAdventureCard;
    }

    public Tile getTileById(Integer id) {
        return tilesDeckMap.get(id);
    }

    public List<Integer> getFaceDownTiles() {
        return faceDownTiles;
    }

    public List<Integer> getFaceUpTiles() {
        return faceUpTiles;
    }

    public Map<Integer, Tile> getTilesDeckMap() {
        return tilesDeckMap;
    }

    /**
     * Removes and returns the first tile from the face-down tile deck.
     * If the deck is empty, it throws a RuntimeException.
     *
     * @return the first {@code Tile} from the face-down deck.
     * @throws RuntimeException if no face-down tiles are available.
     */
    public Tile drawFaceDownTile() {
        if (faceDownTiles.isEmpty()) {
            throw new RuntimeException("No face down tiles available");
        }

        int tileId = faceDownTiles.removeFirst();
        return getTileById(tileId);
    }

    /**
     * Removes and returns a face-up tile from the specified index.
     * <p>
     * This method removes the tile at the given index from the face-up tile deck
     * and returns the corresponding {@code Tile} object.
     * If the index is out of bounds, an {@code IndexOutOfBoundsException} is thrown.
     *
     * @param index the position of the tile to remove from the face-up deck.
     * @return the {@code Tile} removed from the specified index.
     * @throws IndexOutOfBoundsException if the index is not within the valid range.
     */
    public Tile removeFaceUpTile(int index) {
        if (index < 0 || index >= this.faceUpTiles.size()) {
            throw new IndexOutOfBoundsException();
        }

        int selectedTileId = faceUpTiles.remove(index);
        return tilesDeckMap.get(selectedTileId);
    }


    // give end game bonuses

    // todo: test

    /**
     * Calculates the best ship based on the minimum number of exposed connectors
     * among players who are currently in the flight phase and give bonus to best ships
     * <p>
     * This method filters players whose state is {@code PlayerState.FLIGHT} and identifies
     * the player(s) with the least number of exposed connectors on their ship.
     * The identified players are then awarded credits based on the most beautiful ship
     * according to the board.
     * <p>
     * Assumptions:
     * - The state of players that are still "alive" is assumed to be {@code PlayerState.FLIGHT}.
     */
    public void calculateBestShip() {

        // assumes state of players that are still "alive" is PlayerState.FLIGHT

        int minConnectors = players.stream()
                .filter(p -> p.getState() == PlayerState.FLIGHT)
                .mapToInt(p -> p.getShip().getNumExposedConnectors())
                .min()
                .orElse(0);

        List<Player> minPlayers = players.stream()
                .filter(p -> p.getShip().getNumExposedConnectors() == minConnectors && p.getState() == PlayerState.FLIGHT)
                .toList();

        for (Player p : minPlayers) {
            p.updateCredits(this.board.getMostBeautifulShipCredits());
        }
    }

    // todo: test

    /**
     * Distributes end game credits to players based on their position.
     * <p>
     * This method assumes that players who are still "alive" are in the {@code PlayerState.FLIGHT} state.
     * It sorts the surviving players by their position in descending order and assigns them end game credits
     * from {@code board.endGameCredits} based on their ranking.
     * <p>
     * The credits are given in the order of player positions, with the highest-ranked player receiving the first
     * credit from the list.
     */
    public void giveEndCredits() {

        // assumes state of players that are still "alive" is PlayerState.FLIGHT

        List<Player> survivedPlayersByPosition = this.getSortedPlayers().stream().
                filter(p -> p.getState() == PlayerState.FLIGHT).
                sorted(Comparator.comparingInt(Player::getPosition).reversed()).
                toList();

        for (int i = 0; i < survivedPlayersByPosition.size(); i++) {
            survivedPlayersByPosition.get(i).updateCredits(this.board.endGameCredits.get(i));
        }
    }

    public void handleEndGame() {
        this.giveEndCredits();
        this.calculateBestShip();
    }

    // tutti metodi da controller?

    public void movePlayer(Player player, int steps) {
        player.move(steps);
    }

    public void placeTile(Player player, int x, int y) {
        player.placeTile(x, y);
    }

    public void chooseFaceUpTile(Player player, int index) {
        Tile selectedTile = this.removeFaceUpTile(index);
        player.setHeldTile(selectedTile);
    }

    public void pickFaceDownTile(Player player) {
        Tile tile = this.drawFaceDownTile();
        player.setHeldTile(tile);
    }

    public void updateCredits(Player player, int delta) {
        player.updateCredits(delta);
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
        player.loadResource(x, y, box);
    }

    public void removeResource(Player player, int x, int y, BoxType box) {
        player.removeResource(x, y, box);
    }

    public void removeCrew(Player player, int x, int y) {
        player.removeCrew(x, y);
    }

    public void useBattery(Player player, int x, int y) {
        player.useBattery(x, y);
    }

    public void setPlayerState(Player player, PlayerState state) {
        player.setState(state);
    }

}