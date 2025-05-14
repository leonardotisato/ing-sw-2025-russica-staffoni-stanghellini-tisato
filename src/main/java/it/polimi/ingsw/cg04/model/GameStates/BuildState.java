package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.FlightBoard;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.util.*;

public class BuildState extends GameState {
    // players states are stored in a map
    // when a player finishes building it goes on FixShip or ShipReady defending on isShipLegal()
    // when all players are in ShipReady they are all moved to AddingCrew
    // where they can place aliens or humans in the HousingTiles which supports both -> (this phase still needs to be designed properly)
    // player-states:
    // building
    // fixing
    // ready
    // showing_face_up
    // showing_pile
    Map<String, BuildPlayerState> playerState;
    Game game;
    Map<String, Integer> isLookingPile;
    Map<String, Integer> isLookingFaceUpTiles;

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
    public void rotateTile(Player player, String type) throws InvalidStateException {

        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new InvalidStateException("cant rotate held tile now");
        }

        if (type.equalsIgnoreCase("LEFT")) {
            player.getHeldTile().rotate90sx();
        } else if (type.equalsIgnoreCase("RIGHT")) {
            player.getHeldTile().rotate90dx();
        }
    }

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

    @Override
    public void closeFaceUpTiles(Player player) throws InvalidStateException {
        // the only state in which a player can close face-up tiles is SHOWING_FACE_UP
        if (playerState.get(player.getName()) != BuildPlayerState.SHOWING_FACE_UP) {
            throw new InvalidStateException("cant close face up tiles now");
        }

        playerState.put(player.getName(), BuildPlayerState.BUILDING);

        //todo: what to do here???
    }

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

    @Override
    public void endBuilding(Player player, int position) throws InvalidStateException {
        // player is not in building phase or is looking af face-up tiles or is looking a pile
        if (playerState.get(player.getName()) == BuildPlayerState.READY || playerState.get(player.getName()) == BuildPlayerState.FIXING) {
            throw new InvalidStateException("cant end building now");
        }
        if (position > playerState.size()) {
            throw new InvalidStateException("there are " + playerState.size() + "cant choose position " + position);
        }
        FlightBoard board = game.getBoard();
        if (board.getCell(board.getStartingPosition(position)) != null) {
            throw new InvalidStateException("already a player in position " + position);
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

    @Override
    public void fixShip(Player player, List<Coordinates> coordinatesList) throws InvalidStateException {
        if (playerState.get(player.getName()) != BuildPlayerState.FIXING) {
            throw new InvalidStateException("cant fix ship now");
        }

        for (Coordinates coordinates : coordinatesList) {
            player.getShip().breakTile(coordinates.getX(), coordinates.getY());
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
                stringBuilder.append(renderKFigures(5, isLookingPile.get(nickname), "piles")).append("\n").append("\n");
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


    // getters needed to simplify testing
    public Map<String, Integer> getIsLookingPile() {
        return isLookingPile;
    }

    public Map<String, BuildPlayerState> getPlayerState() {
        return playerState;
    }
}
