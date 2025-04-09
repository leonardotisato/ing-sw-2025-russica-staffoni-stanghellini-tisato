package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildState extends GameState {
    // players states are stored in a map
    // when a player finishes building it goes on FixShip or ShipReady defending on isShipLegal()
    // when all players are in ShipReady they are all moved to AddingCrew
    // where they can place aliens or humans in the HousingTiles which supports both -> (this phase still needs to be designed properly)
    // player-states:
    // Building
    // FixShip
    // ShipReady
    // AddingCrew
    Map<String, String> playerState;
    Game game;
    Map<String, Boolean> isInShowFaceUp;
    Map<String, Integer> isLookingPile;

    public BuildState(Game game) {
        playerState = new HashMap<>();
        this.game = game;
        for (Player player : game.getPlayers()) {
            playerState.put(player.getName(), "Building");
        }
        this.isInShowFaceUp = new HashMap<>();
        for (Player player : game.getPlayers()) {
            isInShowFaceUp.put(player.getName(), false);
        }
        this.isLookingPile = new HashMap<>();
        for (Player player : game.getPlayers()) {
            isLookingPile.put(player.getName(), null);
        }
    }

    @Override
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }

    @Override
    public void placeTile(Player player, int x, int y) {
        // player is not in building phase or is looking af face-up tiles or is looking a pile
        if (!playerState.get(player.getName()).equals("Building") || isInShowFaceUp.get(player.getName()) || isLookingPile.get(player.getName()) != null) {
            throw new IllegalArgumentException("cant place tile now");
        }
        player.placeTile(x, y);
    }

    @Override
    public void placeInBuffer(Player player) {
        // player is not in building phase or is looking af face-up tiles or is looking a pile
        if (!playerState.get(player.getName()).equals("Building") || isInShowFaceUp.get(player.getName()) || isLookingPile.get(player.getName()) != null){
            throw new IllegalArgumentException("cant place tile in buffer now");
        }
        player.getShip().addTileInBuffer(player.getHeldTile());
        player.setHeldTile(null);
    }

    @Override
    public void chooseTile(Player player, Tile tile) {
        // player is not in building phase or is looking a pile
        if (!playerState.get(player.getName()).equals("Building") || isLookingPile.get(player.getName()) != null) {
            throw new IllegalArgumentException("cant choose tile now");
        }
        player.setHeldTile(tile);
    }

    @Override
    public void showFaceUp(Player player) {
        // player is not in building phase or is looking a pile
        if (!playerState.get(player.getName()).equals("Building") || isLookingPile.get(player.getName()) != null) {
            throw new IllegalArgumentException("cant show face up now");
        }
        // player is already in show face-up
        if(isInShowFaceUp.get(player.getName())) {
            throw new IllegalArgumentException("already in show face up");
        }
        int j = 1;
        System.out.println("list of face up tiles\n");
        for(Integer i : player.getGame().getFaceUpTiles()){
            System.out.println(j + player.getGame().getTileById(i).toString()+ "\n");
            j++;
        }
        isInShowFaceUp.put(player.getName(), true);
    }

    @Override
    public void closeFaceUpTiles(Player player) {
        // player is not in building phase or is looking a pile
        if(!playerState.get(player.getName()).equals("Building") || isLookingPile.get(player.getName()) != null) {
            throw new IllegalArgumentException("cant close face up tiles now");
        }
        if(!isInShowFaceUp.get(player.getName())) {
            throw new IllegalArgumentException("not in show face up");
        }
        isInShowFaceUp.put(player.getName(), false);

        //todo: what to do here???
    }

    @Override
    public void drawFaceDown(Player player) {
        // player is not in building phase or is looking af face-up tiles or is looking a pile
        if(!playerState.get(player.getName()).equals("Building") || isLookingPile.get(player.getName()) != null || isInShowFaceUp.get(player.getName()) != null) {
            throw new IllegalArgumentException("cant draw face down now");
        }
        Tile drawnTile = player.getGame().drawFaceDownTile();
        System.out.println("you have drawn " + drawnTile.toString() + "\n");
        player.setHeldTile(drawnTile);
    }

    @Override
    public void returnTile(Player player) {
        // player is not in building phase or is looking a pile
        if(!playerState.get(player.getName()).equals("Building") || isLookingPile.get(player.getName()) != null) {
            throw new IllegalArgumentException("cant return tile now");
        }
        Tile currTile = player.getHeldTile();
        player.getGame().getFaceUpTiles().add(currTile.getId());
        player.setHeldTile(null);
    }

    @Override
    public void pickPile(Player player, Integer pileIndex) {
        for(String p : isLookingPile.keySet()) {
            // player is not in building phase or is in show face-up
            if(!playerState.get(p).equals("Building") || isInShowFaceUp.get(p)) {
                throw new IllegalArgumentException("cant pick pile now");
            }
            // if someone is already looking at this pile
            if(pileIndex.equals(isLookingPile.get(p))) {
                throw new IllegalArgumentException("someone is already looking pile" + pileIndex+1);
            }
            // if player is already looking at another pile
            if(isLookingPile.get(player.getName()) != null) {
                throw new IllegalArgumentException("you are already looking at another pile");
            }

            // update isLookingPile
            isLookingPile.put(player.getName(), pileIndex);

            // print out pile
            List<Integer> pile = game.getPreFlightPiles().get(pileIndex);
            System.out.println("Pile " + pileIndex + 1 + "\n");
            for(Integer i : pile) {
                System.out.println(game.getCardById(i).toString() + "\n");
            }
        }
    }

    @Override
    public void returnPile(Player player, Integer pileIndex) {
        // player is not in building phase or is in show face-up
        if(!playerState.get(player.getName()).equals("Building") || isInShowFaceUp.get(player.getName()) != null) {
            throw new IllegalArgumentException("can't return pile now");
        }
        if(!isLookingPile.get(player.getName()).equals(pileIndex)) {
            throw new IllegalArgumentException("can not return a pile you are not looking");
        }

        isLookingPile.put(player.getName(), null);

    }


}
