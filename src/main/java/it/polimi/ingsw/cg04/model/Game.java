package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.GameStates.LobbyState;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.CardLoader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.cg04.model.utils.Coordinates;
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
    private transient PropertyChangeListener listener;

    // for testing purposes
    private final Random rand = new Random();

    public Game(int level, int maxPlayers, int id, String playerNickName, PlayerColor playerColor) {
        this.level = level;
        this.maxPlayers = maxPlayers;
        this.id = id;

        if (level == 1) {
            this.board = new FlightBoardLev1(this);
        } else if (level == 2) {
            this.board = new FlightBoardLev2(this);
        }

        // set up adventure cards
        String PATH_TO_CARDS = "jsons/AdventureCardsFile.json";
        this.adventureCardsMap = CardLoader.loadCardsFromJson(PATH_TO_CARDS, this.level1Cards, this.level2Cards);
        this.preFlightPiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            preFlightPiles.add(new ArrayList<>());
        }

        createAdventureDeck();

        // set up tiles
        String PATH_TO_TILES = "jsons/TilesFile.json";
        this.tilesDeckMap = TileLoader.loadTilesFromJson(PATH_TO_TILES, this.faceDownTiles);


        // add first player
        this.addPlayer(playerNickName, playerColor);
        // set initial gameState
        this.gameState = new LobbyState(this);
    }

    // todo: only used for testing... trying to delete but it was tedious so i gave up atm...
    public Game(int level) {
        this.maxPlayers = 4;
        this.level = level;
        if (level == 1) this.board = new FlightBoardLev1(this);
        else if (level == 2) this.board = new FlightBoardLev2(this);

        String PATH_TO_CARDS = "jsons/AdventureCardsFile.json";
        this.adventureCardsMap = CardLoader.loadCardsFromJson(PATH_TO_CARDS, this.level1Cards, this.level2Cards);
        this.preFlightPiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            preFlightPiles.add(new ArrayList<>());
        }
        this.adventureCardsDeck = new ArrayList<>();
        createAdventureDeck();

        String PATH_TO_TILES = "jsons/TilesFile.json";
        this.tilesDeckMap = TileLoader.loadTilesFromJson(PATH_TO_TILES, this.faceDownTiles);

        this.id = 0;
        this.gameState = new LobbyState(this);
    }

    /**
     * Sets the listener {@code GamesController} for property change events in the game.
     *
     * @param listener the {@code PropertyChangeListener} to be notified of property changes.
     */
    public void setListener(PropertyChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Notifies the registered listener of a property change event.
     *
     * @param evt the {@code PropertyChangeEvent} containing information about the change.
     */
    public void notifyListener(PropertyChangeEvent evt) {
        listener.propertyChange(evt);
    }

    /**
     * Checks if the game has started.
     *
     * @return {@code true} if the game has started, {@code false} otherwise.
     */
    public boolean hasStarted() {
        return hasStarted;
    }

    /**
     * Sets the game's current start status.
     *
     * @param hasStarted a boolean indicating if the game has started.
     *                   {@code true} if the game has started, {@code false} otherwise.
     */
    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    /**
     * @return the maximum number of players as an integer.
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return the identifier of the game as an integer.
     */
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

        if (getNumPlayers() == this.maxPlayers) setHasStarted(true);

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
     * @return a list of {@code Player} objects representing the retired players.
     */
    public List<Player> getRetiredPlayers() {
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
        for (Player p : retired) {
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

    // cards and tiles handling

    /**
     * Builds the adventure cards deck.
     * <p>
     * This method creates the deck basing on the dynamic type of the board,
     * distinguishing between level 2 and test match.
     */
    public void createAdventureDeck() {
        this.adventureCardsDeck = this.board.createAdventureCardsDeck(this);
        this.adventureCardsDeck.set(0, 7);
        this.adventureCardsDeck.set(1, 8);
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

    /**
     * @return a list of lists where each inner list represents a specific pre-flight pile
     * containing integer that represent adventure cards IDs
     */
    public List<List<Integer>> getPreFlightPiles() {
        return this.preFlightPiles;
    }

    /**
     * @return a map where the keys are integers representing card IDs
     * and the values are {@code AdventureCard} objects.
     */
    public Map<Integer, AdventureCard> getAdventureCardsMap() {
        return this.adventureCardsMap;
    }

    /**
     * @return a list of integers representing the IDs of the adventure cards in the deck.
     */
    public List<Integer> getAdventureCardsDeck() {
        return this.adventureCardsDeck;
    }

    /**
     * Retrieves an {@code AdventureCard} by its unique identifier.
     *
     * @param id the unique identifier of the adventure card to retrieve.
     * @return the {@code AdventureCard} associated with the specified ID
     */
    public AdventureCard getCardById(Integer id) {
        return adventureCardsMap.get(id);
    }

    /**
     * Retrieves the next adventure card from the deck. If the deck is empty,
     * the method returns {@code null}. The current adventure card is updated
     * to the retrieved card before being returned.
     *
     * @return the next {@code AdventureCard} from the deck, or {@code null} if the deck is empty.
     */
    public AdventureCard getNextAdventureCard() {
        if (this.adventureCardsDeck.isEmpty()) return null;
        this.currentAdventureCard = this.adventureCardsMap.get(adventureCardsDeck.removeFirst());
        return this.currentAdventureCard;
    }

    /**
     * Retrieves the current adventure card set in the game.
     *
     * @return the current {@code AdventureCard} instance
     */
    public AdventureCard getCurrentAdventureCard() {
        return this.currentAdventureCard;
    }

    /**
     * Sets the current adventure card for the game.
     *
     * @param card the {@code AdventureCard} to set as the current adventure card.
     */
    public void setCurrentAdventureCard(AdventureCard card) {
        this.currentAdventureCard = card;
    }

    /**
     * Retrieves a tile from the tiles deck map using the specified ID.
     *
     * @param id the unique identifier of the tile to be retrieved
     * @return the Tile object corresponding to the given ID, or null if no tile is found
     */
    public Tile getTileById(Integer id) {
        return tilesDeckMap.get(id);
    }

    /**
     * Retrieves the list of tiles that are currently face up.
     *
     * @return a list of integers representing the face-up tiles
     */
    public List<Integer> getFaceUpTiles() {
        return faceUpTiles;
    }

    /**
     * Retrieves the map of tiles associated with their respective identifiers.
     *
     * @return a map where the key is an integer representing the tile identifier
     * and the value is the corresponding Tile object
     */
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

    // give end game bonuses

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
            survivedPlayersByPosition.get(i).updateCredits(this.board.endGameCredits.get(i + 1));
        }
    }

    /**
     * Processes the selling of boxes for active and retired players
     * and updates their credits accordingly based on the types and
     * quantities of boxes they have on their ships.
     * <p>
     * The method leverages the Player's `getShip()` method to access
     * the ship's box inventory and the `updateCredits(double)` method
     * to update the player's credit balance.
     */
    public void sellBoxes() {
        double delta;
        for (Player p : players) {
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

    /**
     * Calculates the total number of lost pieces for each player and updates their credits accordingly.
     * This method processes all active and retired players, taking into account the broken tiles
     * and buffered tiles on their respective ships.
     */
    public void calculateLostPieces() {
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

    /**
     * Handles the end-of-game process by performing a series of operations
     * that finalize the game state and provide game-ending details.
     */
    public void handleEndGame() {
        this.sellBoxes();
        this.calculateLostPieces();
        this.giveEndCredits();
        this.calculateBestShip();
    }

    /**
     * Flags and handles the scenario when a player has been lapped during the game.
     * If the player is lapped, they are marked as retired, removed from the current players list,
     * and a log message is returned indicating their elimination.
     *
     * @param p The player to check for being lapped.
     * @return A log message indicating the player's elimination if they were lapped, or an empty string otherwise.
     */
    public String flagLapped(Player p) {
        String log = "";
        if (p.wasLapped()) {
            retired.add(p);
            players.remove(p);
            p.setRetired(true);
            log = "Player " + p.getName() + " has been eliminated because he has been lapped!";
        }
        return log;
    }

    /**
     * Flags a player whose ship does not have enough human crew members,
     * retires them from the game, removes them from active players,
     * and returns a log message about the removal.
     *
     * @param p the player to check for human crew members
     * @return a log message indicating the player has been retired due to having no human crew members
     */
    public String flagNoHumans(Player p) {
        String log = "";
        if (!p.getShip().hasEnoughHumans()) {
            if(!retired.contains(p)) {
                retired.add(p);
                players.remove(p);
                p.setRetired(true);
                log = "Player " + p.getName() + " has been eliminated because he has 0 crew members!";
            }
        }
        return log;
    }

    /**
     * Mark a player as disconnected by removing it from the players list and adding it in the disconnected players
     *
     * @param p the player to be disconnected
     */
    public void disconnect(Player p) {
        disconnected.add(p);
        players.remove(p);
    }

    public void composeNewBoxesMap(String nickname, List<Coordinates> c, List<Map<BoxType, Integer>> newBoxes) {
        Ship s = getPlayer(nickname).getShip();
        for (Coordinates c1 : s.getTilesMap().get("StorageTile")) {
            if (!c1.isIn(c)) {
                c.add(c1);
                newBoxes.add(s.getTile(c1.getX(), c1.getY()).getBoxes());
            }
        }
    }

    // todo: questo viene usato nel vecchio pattern, usages solo in tests vecchi...
    public String render(String nickname) {
        return this.gameState.render(nickname);
    }

    /**
     * Create a toString of the ships of all players into a single string.
     *
     * @return A string containing the rendered representation of all players' ships.
     */
    public String renderShips() {
        StringBuilder sb = new StringBuilder();

        for (Player p : players) {
            sb.append(p.getName()).append("'s ship: \n");
            sb.append(p.getShip().draw());
            sb.append("\n");
        }

        return sb.toString();
    }


    /**
     * Creates a deep copy of the current Game object by using serialization.
     *
     * @return A new Game object that is a deep copy of the current instance or
     * null if an error occurs during the deep copy process.
     */
    public Game deepCopy() {
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

    /**
     * Retrieves the information about the current game, including game ID, the maximum number of players allowed,
     * a mapping of players' names to their respective colors, and the game level. Used to provide info about the game to players in preLobby
     *
     * @return a GameInfo object containing details about the current game.
     */
    public GameInfo getGameInfo() {
        Map<String, String> playerWithColor = new HashMap<>();
        for (Player p : players) {
            playerWithColor.put(p.getName(), p.getColor().toString());
        }
        return new GameInfo(this.id, this.maxPlayers, playerWithColor, this.level);
    }

    /**
     * Represents information about a game, including its ID, maximum number of players,
     * player-color associations, and game level.
     */
    public record GameInfo(int id, int maxPlayers, Map<String, String> playerWithColor, int gameLevel) implements Serializable {
        public String gameInfoToColumn() {
            StringBuilder builder = new StringBuilder();
            builder.append("ID: ").append(id).append("\n");
            builder.append("Level: ").append(gameLevel).append("\n");
            builder.append("Players ").append(playerWithColor.size()).append("/").append(maxPlayers).append("\n");
            for (String s : playerWithColor.keySet()) {
                builder.append(s).append(": ").append(playerWithColor.get(s)).append("\n");
            }
            return builder.toString();
        }
    }
}