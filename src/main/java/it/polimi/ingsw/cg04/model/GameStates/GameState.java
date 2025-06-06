package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GameState implements Serializable {
    protected List<String> logs;

    // AdventureCardState methods

    public void loadCrew(Player player, Coordinates pinkAlienCoords, Coordinates brownAlienCoords) throws InvalidStateException {
        throw new InvalidStateException("Invalid action!");
    }

    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) throws InvalidStateException {
        throw new InvalidStateException("Invalid action!");
    }

    public void handleBoxes(Player player, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) throws InvalidStateException {
        throw new InvalidStateException("Invalid action!");
    }

    public void landToPlanet(Player player, Integer planetIdx, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) throws InvalidStateException {
        throw new InvalidStateException("Invalid action!");
    }

    // Allowed in: OpenSpaceState
    public void usePropulsors(Player p, List<Coordinates> coordinates, List<Integer> usedBatteries) throws InvalidStateException {
        throw new InvalidStateException("invalid action");
    }

    // Allowed in: MeteorRainState, ...
    public void rollDice(Player player) throws InvalidStateException {
        throw new InvalidStateException("invalid action");
    }

    // Allowed in: MeteorRainState, BuildState, ...
    public void fixShip(Player player, List<Coordinates> coordinatesList) throws InvalidStateException {
        throw new InvalidStateException("invalid action");
    }

    // Allowed in: MeteorRainState, ...
    public void chooseBattery(Player player, int x, int y) throws InvalidStateException {
        throw new InvalidStateException("invalid action");
    }

    // Allowed in Smugglers, Pirates, Slavers, ...
    public void getReward(Player player, boolean acceptReward) throws InvalidStateException {
        throw new InvalidStateException("invalid action");
    }

    // Allowed in Smugglers, Pirates, Slavers, War zone, ...
    public void compareFirePower(Player player, List<Coordinates> batteries, List<Coordinates> doubleCannons) throws InvalidStateException {
        throw new InvalidStateException("invalid action");
    }


    // BuildState Methods
    public void placeTile(Player player, int x, int y) throws InvalidStateException {
        throw new InvalidStateException("cant place tile in this game state");
    }

    public void setShip(Player player, int x, int y) throws InvalidStateException {
        throw new InvalidStateException("cant setShip tile in this game state");
    }

    public void rotateTile(Player player, String type) throws InvalidStateException {
        throw new InvalidStateException("cant rotate tile in this game state");
    }

    public void placeInBuffer(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant place tile in buffer in this game state");
    }

    public void chooseTile(Player player, int tileID) throws InvalidStateException {
        throw new InvalidStateException("cant choose tile in this game state");
    }

    public void chooseTileFromBuffer(Player player, int idx) throws InvalidStateException {
        throw new InvalidStateException("cant choose tile in this game state");
    }

    public void showFaceUp(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant show face up tile in this game state");
    }

    public void drawFaceDown(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant draw face down tile in this game state");
    }

    public void returnTile(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant return tile in this game state");
    }

    public void closeFaceUpTiles(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant close face up tile in this game state");
    }

    public void pickPile(Player player, int pileIndex) throws InvalidStateException {
        throw new InvalidStateException("cant pick pile in this game state");
    }

    public void returnPile(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant return pile in this game state");
    }

    public void endBuilding(Player player, int position) throws InvalidStateException {
        throw new InvalidStateException("cant end building tile in this game state");
    }

    public void startTimer(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant start timer in this game state");
    }

    public void stopBuilding(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant stoped building tile in this game state");
    }

    public void spreadEpidemic(Player player) throws InvalidStateException {
        throw new InvalidStateException("cant return pile in this game state");
    }

    // allowed in WarZone ...
    public void countCrewMembers(Player player) throws InvalidStateException {
        throw new InvalidStateException("Invalid action in this game state!");
    }

    public void starDust(Player player) throws InvalidStateException {
        throw new InvalidStateException("Invalid action in this game state!");
    }

    public void getNextAdventureCard(Player player) throws InvalidStateException {
        throw new InvalidStateException("Invalid action in this game state!");
    }

    public void retire(Player player) throws InvalidStateException {
        throw new InvalidStateException("Invalid action in this game state!");
    }

    public String render(String nickname) {
        return null;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void addLog(String newLogs) {
        this.logs.add(newLogs);
    }

    public void appendLog(String newLogs) {
        this.logs.add(newLogs);
    }

    public void initLogs() {
        this.logs = new ArrayList<>();
    }

    public void updateView(View view, Game game) throws IOException {
        throw new RuntimeException();
    }
}
