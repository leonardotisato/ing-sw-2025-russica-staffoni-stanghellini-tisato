package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.FlightBoard;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.model.utils.Shipyard;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.util.*;

public class BuildState extends GameState {
    Map<String, BuildPlayerState> playerState;
    Game game;
    Map<String, Integer> isLookingPile;
    Shipyard shipyard = new Shipyard(); // used for faster building

    public BuildState(Game game) {
        playerState = new HashMap<>();
        this.game = game;
        for (Player player : game.getPlayers()) {
            playerState.put(player.getName(), BuildPlayerState.BUILDING);
        }
        isLookingPile = new HashMap<>();
        for (Player player : game.getPlayers()) {
            isLookingPile.put(player.getName(), 0);
        }
    }

    public void triggerNextState() {
        game.setGameState(new LoadCrewState(game));
        game.setCurrentAdventureCard(null);
    }

    /**
     * Places a tile at the specified coordinates for the given player.
     * Throws an exception if the player is not in the correct state to place a tile.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to place the tile.
     * @param x The x-coordinate where the tile is placed.
     * @param y The y-coordinate where the tile is placed.
     * @throws InvalidStateException If the player is not in the BUILDING state.
     */
    @Override
    public void placeTile(Player player, int x, int y) throws InvalidStateException {
        // the only state in which a player can place a tile is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant place tile now");
        }
        player.placeTile(x, y);
        this.addLog(player.getName() + " placed a tile in " + x + ", " + y);
    }

    @Override
    public void setShip(Player player, int x, int y) throws InvalidStateException {
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant place tile now");
        }
        switch (x) {
            case 103 -> player.setShip(shipyard.createShip3());
            case 104 -> player.setShip(shipyard.createShip4());
            case 105 -> player.setShip(shipyard.createShip5());
        }
        this.addLog(player.getName() + " set his ship using code " + x + ".");
    }

    /**
     * Rotates the tile held by the specified player in the given direction.
     * The rotation can be to the left, right, or down, as specified by the type.
     * Throws an exception if the player is not in the correct state to perform the rotation.
     * See the relative action for more exceptions and context
     *
     * @param player The player holding the tile to be rotated.
     * @param type The direction of rotation ("LEFT", "RIGHT", or "DOWN").
     * @throws InvalidStateException If the player is not in the BUILDING state.
     */
    @Override
    public void rotateTile(Player player, String type) throws InvalidStateException {

        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant rotate held tile now");
        }

        if (type.equalsIgnoreCase("LEFT")) {
            player.getHeldTile().rotate90sx();
        } else if (type.equalsIgnoreCase("RIGHT")) {
            player.getHeldTile().rotate90dx();
        }
        else if (type.equalsIgnoreCase("DOWN")) {
            player.getHeldTile().rotate90dx();
            player.getHeldTile().rotate90dx();
        }
    }

    /**
     * Allows the specified player to place a tile in the buffer if conditions are met.
     * The player must be in the BUILDING state, and the game level must be 2.
     * Throws an exception if these conditions are not satisfied.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to place a tile in the buffer.
     * @throws InvalidStateException If the player is not in the BUILDING state
     *                               or if the game level is not 2.
     */
    @Override
    public void placeInBuffer(Player player) throws InvalidStateException {
        // the only state in which a player can place a tile in the buffer is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant place tile in buffer now");
        }
        // player can use tiles buffer only in games of level 2
        if (game.getLevel() != 2) {
            throw new InvalidStateException("cant place tile in buffer in a game of level " + game.getLevel());
        }

        player.addTileInBuffer();
        this.addLog(player.getName() + " placed a tile in the buffer.");
    }

    /**
     * Allows the specified player to choose a tile from the list of face-up tiles.
     * The player must be in the correct state to perform this action.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to choose a tile.
     * @param tileID The ID of the tile the player intends to choose from the face-up tiles.
     * @throws InvalidStateException If the player is not in the BUILDING or SHOWING_FACE_UP state.
     */
    @Override
    public void chooseTile(Player player, int tileID) throws InvalidStateException {
        // the only states in which a player can choose a tile are BUILDING and SHOWING_FACE_UP
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING && playerState.get(player.getName()) != BuildPlayerState.SHOWING_FACE_UP) {
            throw new InvalidStateException("cant choose tile now");
        }

        player.setHeldTile(game.getTileById(game.getFaceUpTiles().get(tileID)));
        this.addLog(player.getName() + " choose tile " + tileID + " from face up tiles");
        game.getFaceUpTiles().remove(tileID);
    }

    /**
     * Allows the specified player to choose a tile from their buffer.
     * The player must be in the BUILDING state to perform this action.
     * See the relative action for more exceptions and context
     *
     * @param player The player who is attempting to choose a tile from their buffer.
     * @param idx The index of the tile in the buffer that the player wants to pick.
     * @throws InvalidStateException If the player is not in the BUILDING state.
     */
    @Override
    public void chooseTileFromBuffer(Player player, int idx) throws InvalidStateException {
        // the only state in which a player can pick tile from buffer is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant show face up now");
        }

        player.setHeldTile(player.getShip().getTilesBuffer().get(idx));
        this.addLog(player.getName() + " choose tile " + idx + " from the buffer");
        player.getShip().getTilesBuffer().remove(idx);
    }

    /**
     * Allows the specified player to view the face-up tiles available during their turn.
     * The player must be in the BUILDING state to perform this action; otherwise, an exception is thrown.
     * The method changes the player's state to SHOWING_FACE_UP and prints the list of face-up tiles to the console.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to view the face-up tiles.
     * @throws InvalidStateException If the player is not in the BUILDING state.
     */
    @Override
    public void showFaceUp(Player player) throws InvalidStateException {
        // the only state in which a player can see face-up tiles is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant show face up now");
        }

        playerState.put(player.getName(), BuildPlayerState.SHOWING_FACE_UP);
        int j = 1;
        for (Integer i : player.getGame().getFaceUpTiles()) {
            System.out.println(j + player.getGame().getTileById(i).toString() + "\n");
            j++;
        }
    }

    /**
     * Closes the face-up tiles for the specified player.
     * This action is only allowed if the player is in the SHOWING_FACE_UP state.
     * If the player is not in the correct state, an InvalidStateException is thrown.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to close the face-up tiles.
     * @throws InvalidStateException If the player is not in the SHOWING_FACE_UP state.
     */
    @Override
    public void closeFaceUpTiles(Player player) throws InvalidStateException {
        // the only state in which a player can close face-up tiles is SHOWING_FACE_UP
        if (playerState.get(player.getName()) != BuildPlayerState.SHOWING_FACE_UP) {
            throw new InvalidStateException("cant close face up tiles now");
        }

        playerState.put(player.getName(), BuildPlayerState.BUILDING);

        //todo: what to do here???
    }

    /**
     * Allows the specified player to draw a face-down tile during the BUILDING phase.
     * This action is only permitted when the player is in the BUILDING state.
     * If the player is not in the correct state, an InvalidStateException is thrown.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to draw a face-down tile.
     * @throws InvalidStateException If the player is not in the BUILDING state.
     */
    @Override
    public void drawFaceDown(Player player) throws InvalidStateException {
        // the only state in which a player can draw faced down tiles is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant draw face down now");
        }

        Tile drawnTile = player.getGame().drawFaceDownTile();
        player.setHeldTile(drawnTile);
        this.addLog(player.getName() + " draws a face down tile");
    }

    /**
     * Allows the specified player to return a held tile during the BUILDING phase.
     * This action is only permitted if the player is in the BUILDING state.
     * If the player attempts to return a tile while in an invalid state, an exception is thrown.
     * See the relative action for more exceptions and context
     *
     * @param player The player who is attempting to return their held tile.
     * @throws InvalidStateException If the player is not in the BUILDING state.
     */
    @Override
    public void returnTile(Player player) throws InvalidStateException {
        // the only state in which a player can return tiles is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant return tile now");
        }

        Tile currTile = player.getHeldTile();
        player.getGame().getFaceUpTiles().add(currTile.getId());
        this.addLog(player.getName() + " returned a tile ");
        player.setHeldTile(null);
    }

    /**
     * Allows the specified player to pick a pile during their turn in the BUILDING phase.
     * The player must be in the BUILDING state to perform this action. If the pile is
     * already being viewed by another player, or the player is not in the correct state,
     * an InvalidStateException is thrown.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to pick the pile.
     * @param pileIndex The index of the pile the player wants to pick.
     * @throws InvalidStateException If the player is not in the BUILDING state
     *                               or the pile is already being viewed by another player.
     */
    @Override
    public void pickPile(Player player, int pileIndex) throws InvalidStateException {
        // if player is not in BUILDING state
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant pick pile now");
        }

        // if someone is already looking at that pile
        for (int i : isLookingPile.values()) {
            if (i == pileIndex) {
                throw new InvalidStateException("someone is already looking at pile" + pileIndex);
            }
        }

        isLookingPile.put(player.getName(), pileIndex);
        playerState.put(player.getName(), BuildPlayerState.SHOWING_PILE);
        this.addLog(player.getName() + " picked pile " + pileIndex);

    }

    /**
     * Allows the specified player to return a pile they were viewing during their turn.
     * This action is only permitted if the player is in the SHOWING_PILE state.
     * If the player is not in the correct state, an InvalidStateException is thrown.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to return the pile.
     * @throws InvalidStateException If the player is not in the SHOWING_PILE state.
     */
    @Override
    public void returnPile(Player player) throws InvalidStateException {
        // player is not in building phase or is in show face-up
        if (playerState.get(player.getName()) != BuildPlayerState.SHOWING_PILE) {
            throw new InvalidStateException("cant return a pile if you are not looking at one");
        }

        this.addLog(player.getName() + " returned pile " + isLookingPile.get(player.getName()));
        isLookingPile.put(player.getName(), 0);
        playerState.put(player.getName(), BuildPlayerState.BUILDING);
    }

    /**
     * Ends the building phase for a player by allowing them to select a starting position
     * if all preconditions are met. This method checks the player's current state and validates
     * the chosen position. If the player's state or position is invalid, an exception is thrown.
     * If all players have completed the building phase, it triggers the next game state.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to end the building phase.
     * @param position The starting position the player chooses.
     * @throws InvalidStateException If the player is not in a valid state to end the building phase,
     *                               if the position is invalid, or if the position is already taken.
     */
    @Override
    public void endBuilding(Player player, int position) throws InvalidStateException {
        // player is not in building phase or is looking af face-up tiles or is looking a pile
        if (playerState.get(player.getName()) == BuildPlayerState.READY || playerState.get(player.getName()) == BuildPlayerState.FIXING) {
            throw new InvalidStateException("cant end building now");
        }
        if(playerState.get(player.getName()) == BuildPlayerState.SHOWING_PILE) {
            throw new InvalidStateException("Return the pile before ending the building phase!");
        }
        if (position > playerState.size()) {
            throw new InvalidStateException("There are only" + playerState.size() + " players! Can't choose position " + position);
        }
        FlightBoard board = game.getBoard();
        if (board.getCell(board.getStartingPosition(position)) != null) {
            throw new InvalidStateException("Position " + position + " is already taken!");
        }

        player.move(board.getStartingPosition(position));
        playerState.put(player.getName(), player.getShip().isShipLegal() ? BuildPlayerState.READY : BuildPlayerState.FIXING);
        //if he needs to fix his ship, is it right to move him to the start position?
        if (player.getShip().isShipLegal()) {
            this.addLog(player.getName() + " is done building and he choose to start at position " + position);
        } else {
            this.addLog(player.getName() + " is done building but his ship is not legal. He will need to fix it");
        }
        if (playerState.values().stream().allMatch(state -> state == BuildPlayerState.READY)) {
            triggerNextState();
        }
    }

    /**
     * Fixes the ship of the specified player by breaking the tiles at the given coordinates.
     * Updates the player's state to READY if the ship becomes legal after fixing.
     * If all players are READY, the next game state is triggered.
     * See the relative action for more exceptions and context
     *
     * @param player The player who is attempting to fix their ship.
     * @param coordinatesList The list of coordinates for the tiles to be broken on the ship.
     * @throws InvalidStateException If the player is not in the FIXING state.
     */
    @Override
    public void fixShip(Player player, List<Coordinates> coordinatesList) throws InvalidStateException {
        if (playerState.get(player.getName()) != BuildPlayerState.FIXING) {
            throw new InvalidStateException("cant fix ship now");
        }

        for (Coordinates coordinates : coordinatesList) {
            player.getShip().breakTile(coordinates.getX(), coordinates.getY());
        }
        if(game.getLevel() == 1) {
            player.getShip().updateNumBrokenTiles(coordinatesList.size());
        }
        if (player.getShip().isShipLegal()) {
            playerState.put(player.getName(), BuildPlayerState.READY);
            this.addLog(player.getName() + " is done fixing his ship!");
            //where will he start?
        }
        if (playerState.values().stream().allMatch(state -> state == BuildPlayerState.READY)) {
            triggerNextState();
        }
    }

    /**
     * Starts the game timer for the specified player if the conditions are met.
     * The timer can only be started if the player is not in the BUILDING state
     * and the game is at level 2. If these conditions are not satisfied, an
     * InvalidStateException is thrown.
     * See the relative action for more exceptions and context
     *
     * @param player The player attempting to start the timer.
     * @throws InvalidStateException If the player is in the BUILDING state or if
     *                               the game level is not 2.
     */
    @Override
    public void startTimer(Player player) throws InvalidStateException {
        if (playerState.get(player.getName()) == BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant start timer now");
        }

        if (game.getLevel() != 2) {
            throw new InvalidStateException("cant start timer in a game of level " + game.getLevel());
        }
        game.getBoard().startTimer();
        this.addLog(player.getName() + " started the timer!");
    }

    @Override
    public String render(String nickname) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append(TuiDrawer.renderPlayersByColumn(game.getPlayers()));
        if (playerState.get(nickname) == BuildPlayerState.FIXING) {
            stringBuilder.append("Your ship:").append("\n").append("\n");
            stringBuilder.append(game.getPlayer(nickname).getShip().draw()).append("\n").append("\n");
            stringBuilder.append("Your ship is not legal, fix it by removing tiles until you can properly fly!");
        } else {
            if (playerState.get(nickname) == BuildPlayerState.SHOWING_PILE) {
                stringBuilder.append(renderKFigures(5, isLookingPile.get(nickname) - 1, "piles")).append("\n").append("\n");
            } else {
                stringBuilder.append(renderPilesBackside(29, 11)).append("\n").append("\n");
            }
            stringBuilder.append("Your ship:").append("\n").append("\n");
            stringBuilder.append(game.getPlayer(nickname).getShip().drawWithBuffer()).append("\n").append("\n");
            if (playerState.get(nickname) == BuildPlayerState.BUILDING) {
                stringBuilder.append(game.getPlayer(nickname).getHeldTile() != null ? ("Held tile: \n" + game.getPlayer(nickname).getHeldTile().draw()) : "Pick a tile!").append("\n").append("\n");
            }
            if (playerState.get(nickname) == BuildPlayerState.READY) {
                stringBuilder.append("You're done building the ship! Wait for the other players to finish the building").append("\n");
            }
            if (playerState.get(nickname) != BuildPlayerState.SHOWING_FACE_UP) {
                stringBuilder.append("Face up tiles: send x to show more tiles!").append("\n").append("\n");
                stringBuilder.append(game.getFaceUpTiles().isEmpty() ? "No face up tiles at the moment" : renderKFigures(10, null, "tiles")).append("\n").append("\n");
            } else {
                stringBuilder.append(renderKFigures(game.getFaceUpTiles().size(), null, "tiles")).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public String renderPilesBackside(int width, int height) {
        List<List<String>> pileLines = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<String> singlePile = new ArrayList<>();
            singlePile.add(TuiDrawer.drawTopBoundary(width));

            int paddingLines = height - 3;
            for (int j = 0; j < paddingLines / 2; j++) {
                singlePile.add(TuiDrawer.drawEmptyRow(width));
            }

            // Riga con ID e stato
            singlePile.add(TuiDrawer.drawCenteredRow("#pile: " + (i + 1), width));
            singlePile.add(TuiDrawer.drawCenteredRow(isLookingPile.containsValue(i + 1) ? "Held" : "Free", width));

            for (int j = 0; j < paddingLines - paddingLines / 2; j++) {
                singlePile.add(TuiDrawer.drawEmptyRow(width));
            }

            singlePile.add(TuiDrawer.drawBottomBoundary(width));
            pileLines.add(singlePile);
        }

        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < height + 1; row++) {
            for (int p = 0; p < pileLines.size(); p++) {
                sb.append(pileLines.get(p).get(row));
                if (p < pileLines.size() - 1) {
                    sb.append("  "); // spazio tra pile
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public String renderKFigures(int k, Integer pileId, String typeFigure) {
        List<List<String>> tileLines = new ArrayList<>();
        List<Integer> figures = typeFigure.equals("tiles") ? game.getFaceUpTiles() : game.getPreFlightPiles().get(pileId);

        for (int i = 0; i < k && i < figures.size(); i++) {
            String[] lines = typeFigure.equals("tiles") ? game.getTileById(figures.get(i)).draw().split("\n") :
                    game.getCardById(figures.get(i)).draw().split("\n");
            tileLines.add(Arrays.asList(lines));
        }

        if (tileLines.isEmpty()) return "";

        int tileHeight = tileLines.getFirst().size();
        int tilesPerRow = 10;
        int totalTiles = tileLines.size();
        int numRowsOfTiles = (int) Math.ceil((double) totalTiles / tilesPerRow);

        StringBuilder sb = new StringBuilder();

        for (int rowBlock = 0; rowBlock < numRowsOfTiles; rowBlock++) {
            int start = rowBlock * tilesPerRow;
            int end = Math.min(start + tilesPerRow, totalTiles);

            // stampa le righe delle tile
            for (int line = 0; line < tileHeight; line++) {
                for (int i = start; i < end; i++) {
                    sb.append(tileLines.get(i).get(line));
                    if (i < end - 1) sb.append("  ");
                }
                sb.append('\n');
            }

            // stampa la riga con gli indici
            if (typeFigure.equals("tiles")) {
                for (int i = start; i < end; i++) {
                    int tileWidth = 14; // larghezza della tile
                    String label = "[" + i + "]";
                    int padLeft = (tileWidth - label.length()) / 2;
                    int padRight = tileWidth - label.length() - padLeft;
                    sb.append(" ".repeat(Math.max(0, padLeft)))
                            .append(label)
                            .append(" ".repeat(Math.max(0, padLeft)));
                    if (i < end - 1) sb.append("   ");
                }
            }
            sb.append("\n\n");
        }

        return sb.toString();
    }

    // only used for testing
    public Map<String, BuildPlayerState> getPlayerState() {
        return playerState;
    }
}
