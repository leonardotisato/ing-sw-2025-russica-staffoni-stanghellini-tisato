package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.FlightBoard;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public BuildState(Game game) {
        playerState = new HashMap<>();
        this.game = game;
        for (Player player : game.getPlayers()) {
            playerState.put(player.getName(), BuildPlayerState.BUILDING);
        }
        isLookingPile = new HashMap<>();
        for (Player player : game.getPlayers()) {
            isLookingPile.put(player.getName(), null);
        }
    }

    public void triggerNextState() {
        game.setGameState(new FlightState());
        game.setCurrentAdventureCard(null);
    }

    @Override
    public void placeTile(Player player, int x, int y) {
        // the only state in which a player can place a tile is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new IllegalArgumentException("cant place tile now");
        }

        player.placeTile(x, y);
    }

    @Override
    public void placeInBuffer(Player player) {
        // the only state in which a player can place a tile in the buffer is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING){
            throw new IllegalArgumentException("cant place tile in buffer now");
        }
        // player can use tiles buffer only in games of level 2
        if(game.getLevel() != 2){
            throw new IllegalArgumentException("cant place tile in buffer in a game of level " + game.getLevel());
        }

        player.getShip().addTileInBuffer(player.getHeldTile());
        player.setHeldTile(null);
    }

    @Override
    public void chooseTile(Player player, Tile tile) {
        // the only states in which a player can choose a tile are BUILDING and SHOWING_FACE_UP
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING  && playerState.get(player.getName()) != BuildPlayerState.SHOWING_FACE_UP) {
            throw new IllegalArgumentException("cant choose tile now");
        }

        player.setHeldTile(tile);
    }

    @Override
    public void showFaceUp(Player player) {
        // the only state in which a player can see face-up tiles is BUILDING
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new IllegalArgumentException("cant show face up now");
        }

        playerState.put(player.getName(), BuildPlayerState.SHOWING_FACE_UP);
        int j = 1;
        System.out.println("list of face up tiles\n");
        for(Integer i : player.getGame().getFaceUpTiles()){
            System.out.println(j + player.getGame().getTileById(i).toString()+ "\n");
            j++;
        }
    }

    @Override
    public void closeFaceUpTiles(Player player) {
        // the only state in which a player can close face-up tiles is SHOWING_FACE_UP
        if(playerState.get(player.getName()) != BuildPlayerState.SHOWING_FACE_UP) {
            throw new IllegalArgumentException("cant close face up tiles now");
        }

        playerState.put(player.getName(), BuildPlayerState.BUILDING);

        //todo: what to do here???
    }

    @Override
    public void drawFaceDown(Player player) {
        // the only state in which a player can draw faced down tiles is BUILDING
        if(playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new IllegalArgumentException("cant draw face down now");
        }

        Tile drawnTile = player.getGame().drawFaceDownTile();
        System.out.println("you have drawn " + drawnTile.toString() + "\n");
        player.setHeldTile(drawnTile);
    }

    @Override
    public void returnTile(Player player) {
        // the only state in which a player can return tiles is BUILDING
        if(playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new IllegalArgumentException("cant return tile now");
        }

        Tile currTile = player.getHeldTile();
        player.getGame().getFaceUpTiles().add(currTile.getId());
        player.setHeldTile(null);
    }

    @Override
    public void pickPile(Player player, int pileIndex) {
        // if player is not in BUILDING state
        if (playerState.get(player.getName()) != BuildPlayerState.BUILDING) {
            throw new IllegalArgumentException("cant pick pile now");
        }
        if (playerState.get(player.getName()) != BuildPlayerState.SHOWING_PILE) {
            throw new IllegalArgumentException("already looking at a pile");
        }
        // if someone is already looking at that pile
        for(int i : isLookingPile.values()){
            if(i == pileIndex){
                throw new IllegalArgumentException("someone is already looking at pile" + pileIndex);
            }
        }

        isLookingPile.put(player.getName(), pileIndex);
        playerState.put(player.getName(), BuildPlayerState.SHOWING_PILE);
        // print out pile
        List<Integer> pile = game.getPreFlightPiles().get(pileIndex);
        System.out.println("Pile " + pileIndex + 1 + "\n");
        for(Integer i : pile) {
            System.out.println(game.getCardById(i).toString() + "\n");
        }

    }

    @Override
    public void returnPile(Player player) {
        // player is not in building phase or is in show face-up
        if(playerState.get(player.getName()) != BuildPlayerState.SHOWING_PILE) {
            throw new IllegalArgumentException("cant return a pile if you are not looking at one");
        }

        isLookingPile.put(player.getName(), null);
        playerState.put(player.getName(), BuildPlayerState.BUILDING);
    }

    @Override
    public void endBuilding(Player player, int position) {
        // player is not in building phase or is looking af face-up tiles or is looking a pile
        if(playerState.get(player.getName()) == BuildPlayerState.READY || playerState.get(player.getName()) == BuildPlayerState.FIXING) {
            throw new IllegalArgumentException("cant end building now");
        }
        if(position > playerState.size()) {
            throw new IllegalArgumentException("there are " + playerState.size() + "cant choose position " + position);
        }
        FlightBoard board = game.getBoard();
        if(board.getCell(board.getStartingPosition(position)) != null){
            throw new IllegalArgumentException("already a player in position " + position);
        }

        player.move(board.getStartingPosition(position));
        playerState.put(player.getName(), player.getShip().isShipLegal() ? BuildPlayerState.READY : BuildPlayerState.FIXING);
    }

    @Override
    public void fixShip(Player player, List<Coordinates> coordinatesList) {
        if(playerState.get(player.getName()) != BuildPlayerState.FIXING) {
            throw new IllegalArgumentException("cant fix ship now");
        }

        for(Coordinates coordinates : coordinatesList) {
            player.getShip().breakTile(coordinates.getX(), coordinates.getY());
        }
        if(player.getShip().isShipLegal()){
            playerState.put(player.getName(), BuildPlayerState.READY);
        }
        if(playerState.values().stream().allMatch(state -> state == BuildPlayerState.READY)) {
            triggerNextState();
        }
    }

    @Override
    public void startTimer(Player player) {
        if(playerState.get(player.getName()) == BuildPlayerState.BUILDING) {
            throw new IllegalArgumentException("cant start timer now");
        }

        if(game.getLevel() != 2){
            throw new IllegalArgumentException("cant start timer in a game of level " + game.getLevel());
        }
        game.getBoard().startTimer();
    }
}
