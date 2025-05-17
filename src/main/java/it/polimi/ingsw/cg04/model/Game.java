package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.GameStates.LobbyState;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.CardLoader;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import it.polimi.ingsw.cg04.model.utils.TileLoader;

import static java.lang.Math.ceil;

public class Game implements Serializable {

    private final int maxPlayers;
    private final int level;
    private final List<Player> players = new ArrayList<>();
    private final List<Player> retired = new ArrayList<>();
    private final List<Player> disconnected = new ArrayList<>();
    private FlightBoard board;
    private GameState gameState;
    private AdventureCard currentAdventureCard;
    private final List<List<Integer>> preFlightPiles;
    private final List<Integer> level1Cards = new ArrayList<>();
    private final List<Integer> level2Cards = new ArrayList<>();
    private final Map<Integer, AdventureCard> adventureCardsMap;
    private List<Integer> adventureCardsDeck = new ArrayList<>();
    private final Map<Integer, Tile> tilesDeckMap;
    private final List<Integer> faceDownTiles = new ArrayList<>();
    private final List<Integer> faceUpTiles = new ArrayList<>();

    private boolean hasStarted = false;
    private final int id;

    // for testing purposes
    private final Random rand = new Random();

    public Game(int level, int maxPlayers, int id, String playerNickName, PlayerColor playerColor) {
        this.level = level;
        this.maxPlayers = maxPlayers;
        this.id = id;

        if (level == 1) {
            this.board = new FlightBoardLev1();
        }
        else if (level == 2) {
            this.board = new FlightBoardLev2();
        }

        // set up adventure cards
        String PATH_TO_CARDS = "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json";
        this.adventureCardsMap = CardLoader.loadCardsFromJson(PATH_TO_CARDS, this.level1Cards, this.level2Cards);
        this.preFlightPiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            preFlightPiles.add(new ArrayList<>());
        }

        createAdventureDeck();

        // set up tiles
        String PATH_TO_TILES = "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json";
        this.tilesDeckMap = TileLoader.loadTilesFromJson(PATH_TO_TILES, this.faceDownTiles);


        // add first player
        this.addPlayer(playerNickName, playerColor);
        // set initial gameState
        this.gameState = new LobbyState(this);
    }

    public Game(int level, String jsonFilePathCards, String jsonFilePathTiles) {
        this.maxPlayers = 4;
        this.level = level;
        if (level == 1) this.board = new FlightBoardLev1();
        else if (level == 2) this.board = new FlightBoardLev2();
        this.adventureCardsMap = CardLoader.loadCardsFromJson(jsonFilePathCards, this.level1Cards, this.level2Cards);
        this.preFlightPiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            preFlightPiles.add(new ArrayList<>());
        }
        this.adventureCardsDeck = new ArrayList<>();
        createAdventureDeck();
        this.tilesDeckMap = TileLoader.loadTilesFromJson(jsonFilePathTiles, this.faceDownTiles);
        this.id = 0;
        this.gameState = new LobbyState(this);
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getId() {
        return id;
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

    public List<Player>getRetiredPlayers() {
        return retired;
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
    @Deprecated
//    public void startBuildPhase() {
//        this.setGameState(ExGameState.BUILDING);
//        for (Player p : players) {
//            p.setState(ExPlayerState.BUILDING);
//        }
//    }

//    /**
//     * Initiates the flight phase of the game.
//     * <p>
//     * This method sets the game's state to `FLIGHT` and updates each player's
//     * state to `FLIGHT`, transitioning them from the building phase to the
//     * flight phase.
//     */

//    public void startFlightPhase() {
//        this.setGameState(ExGameState.FLIGHT);
//        for (Player p : players) {
//            p.setState(ExPlayerState.FLIGHT);
//        }
//    }

    // todo: test
//    public void checkShips() {
//        for (Player p : players) {
//            if (!p.getShip().isShipLegal()) p.setState(ExPlayerState.FIX_SHIP);
//            else p.setState(ExPlayerState.FLIGHT);
//        }
//    }

    // cards and tiles handling

    /**
     * Builds the adventure cards deck.
     * <p>
     * This method creates the deck basing on the dynamic type of the board,
     * distinguishing between level 2 and test match.
     */
    public void createAdventureDeck() {
        this.adventureCardsDeck = this.board.createAdventureCardsDeck(this);
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

    public List<List<Integer>> getPreFlightPiles() {
        return this.preFlightPiles;
    }

    public Map<Integer, AdventureCard> getAdventureCardsMap() { return this.adventureCardsMap; }

    public List<Integer> getAdventureCardsDeck() { return this.adventureCardsDeck; }

    public AdventureCard getCardById(Integer id) {
        return adventureCardsMap.get(id);
    }

    public AdventureCard getNextAdventureCard() {
        if (this.adventureCardsDeck.isEmpty()) return null;
        this.currentAdventureCard = this.adventureCardsMap.get(adventureCardsDeck.removeFirst());
        return this.currentAdventureCard;
    }

    public AdventureCard getCurrentAdventureCard() {
        return this.currentAdventureCard;
    }

    public void setCurrentAdventureCard(AdventureCard card) {
        this.currentAdventureCard = card;
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
        int tileId;

        do {
            if (faceDownTiles.isEmpty()) {
                throw new RuntimeException("No face down tiles available");
            }
            tileId = faceDownTiles.removeFirst();
        } while (tileId == 33 || tileId == 34 || tileId == 52 || tileId == 61);
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
        int minConnectors = players.stream()
                .mapToInt(p -> p.getShip().getNumExposedConnectors())
                .min()
                .orElse(0);

        List<Player> minPlayers = players.stream()
                .filter(p -> p.getShip().getNumExposedConnectors() == minConnectors)
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
        List<Player> survivedPlayersByPosition = this.getSortedPlayers().stream().
                sorted(Comparator.comparingInt(Player::getPosition).reversed()).
                toList();

        for (int i = 0; i < survivedPlayersByPosition.size(); i++) {
            survivedPlayersByPosition.get(i).updateCredits(this.board.endGameCredits.get(i));
        }
    }

    private void sellBoxes() {
        double delta;
        for(Player p : players) {
            delta = p.getShip().getBoxes().get(BoxType.RED) * 4 + p.getShip().getBoxes().get(BoxType.YELLOW) * 3
                    + p.getShip().getBoxes().get(BoxType.GREEN) * 2 + p.getShip().getBoxes().get(BoxType.BLUE);
            p.updateCredits(delta);
        }
        for (Player p : retired) {
            delta = (p.getShip().getBoxes().get(BoxType.RED) * 4 + p.getShip().getBoxes().get(BoxType.YELLOW) * 3
                    + p.getShip().getBoxes().get(BoxType.GREEN) * 2 + p.getShip().getBoxes().get(BoxType.BLUE));
            delta = ceil(delta / 2);
            p.updateCredits(delta);
        }
    }

    private void calculateLostPieces() {
        int lostPieces;
        for (Player p : players) {
            lostPieces = p.getShip().getNumBrokenTiles() + p.getShip().getTilesBuffer().size();
            p.updateCredits(-lostPieces);
        }
        for (Player p : retired) {
            lostPieces = p.getShip().getNumBrokenTiles() + p.getShip().getTilesBuffer().size();
            p.updateCredits(-lostPieces);
        }
    }

    public void handleEndGame() {
        this.sellBoxes();
        this.calculateLostPieces();
        this.giveEndCredits();
        this.calculateBestShip();
    }

    // check no player is lapped, lapped players are removed from players and put only their nick is stored in eliminated
    public void flagLapped() {
        for (Player p : players) {
            if (p.wasLapped()) {
                retired.add(p);
                players.remove(p);
                p.setRetired(true);
            }
        }
    }

    // check crew is positive
    public void flagNoHumans() {
        for (Player p : players) {
            if (!p.getShip().hasEnoughHumans()) {
                retired.add(p);
                players.remove(p);
                p.setRetired(true);
            }
        }
    }

    public void disconnect(Player p) {
        disconnected.add(p);
        players.remove(p);
    }

    public String render(String nickname){
        return this.gameState.render(nickname);
    }

    public Game deepCopy()  {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Game) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}