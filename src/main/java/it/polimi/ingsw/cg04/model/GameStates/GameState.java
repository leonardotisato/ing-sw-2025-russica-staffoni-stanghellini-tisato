package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;
import java.util.Map;

public abstract class GameState {
    public void handleAction(Player player, PlayerAction action){
        return;
    }



    // AdventureCardState methods

    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost){
        throw new RuntimeException("Invalid action!");
    }

    public void handleBoxes(Player player, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes){
        throw new RuntimeException("Invalid action!");
    }

    public void landToPlanet(Player player, Integer planetIdx, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) {
        throw new RuntimeException("Invalid action!");
    }

    // Allowed in: OpenSpaceState
    public void usePropulsors(Player p, List<Coordinates> coordinates, List<Integer> usedBatteries) throws RuntimeException {
        throw new RuntimeException("invalid action");
    }

    // Allowed in: MeteorRainState, ...
    public void rollDice(Player player) {
        throw new RuntimeException("invalid action");
    }

    // Allowed in: MeteorRainState, ...
    public void fixShip(Player player, List<Coordinates> coordinatesList) {
        throw new RuntimeException("invalid action");
    }

    // Allowed in: MeteorRainState, ...
    public void chooseBattery(Player player, int x, int y) {
        throw new RuntimeException("invalid action");
    }

    // Allowed in Smugglers, Pirates, Slavers, ...
    public void getReward(Player player, boolean acceptReward) {
        throw new RuntimeException("invalid action");
    }

    // Allowed in Smugglers, Pirates, Slavers, Warzone, ...
    public void compareFirePower(Player player, List<Coordinates> batteries, List<Coordinates> doubleCannons) {
        throw new RuntimeException("invalid action");
    }



    // BuildState Methods
    public void placeTile(Player player, int x, int y) {
        throw new IllegalArgumentException("cant place tile in this game state");
    }

    public void placeInBuffer(Player player) {
        throw new IllegalArgumentException("cant place tile in buffer in this game state");
    }

    public void chooseTile(Player player, Tile tile) {
        throw new IllegalArgumentException("cant choose tile in this game state");
    }

    public void showFaceUp(Player player) {
        throw new IllegalArgumentException("cant show face up tile in this game state");
    }

    public void drawFaceDown(Player player) {
        throw new IllegalArgumentException("cant draw face down tile in this game state");
    }

    public void returnTile(Player player) {
        throw new IllegalArgumentException("cant return tile in this game state");
    }

    public void closeFaceUpTiles(Player player) {
        throw new IllegalArgumentException("cant close face up tile in this game state");
    }

    public void pickPile(Player player, Integer pileIndex) {
        throw new IllegalArgumentException("cant pick pile in this game state");
    }

    public void returnPile(Player player, Integer pileIndex) {
        throw new IllegalArgumentException("cant return pile in this game state");
    }

    public void spreadEpidemic(Player player) {
        throw new IllegalArgumentException("cant return pile in this game state");
    }
}
