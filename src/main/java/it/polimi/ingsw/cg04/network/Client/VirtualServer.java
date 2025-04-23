package it.polimi.ingsw.cg04.network.Client;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;
import java.util.Map;

public interface VirtualServer {
    // initActions
    public void setNickname(String nickname);
    public void createGame(int gameLevel, int maxPlayers);
    public void joinGame(int gameId);

    // buildActions
    public void chooseTile(int tileIdx);
    public void closeFaceUpTiles();
    public void drawFaceDown();
    public void endBuilding(int position);
    public void pickPile(int pileIdx);
    public void place(int x, int y);
    public void placeInBuffer();
    public void returnPile();
    public void returnTile();
    public void showFaceUp();
    public void startTimer();

    //adventureCardActions
    public void chooseBattery(int x, int y);
    public void choosePropulsor(List<Coordinates> coordinates, List<Integer> usedBatteries);
    public void compareCrew();
    public void compareFirePower(List<Coordinates> BatteryCoords, List <Coordinates> CannonCoords);
    public void spreadEpidemic();
    public void getNextAdventureCard();
    public void getRewards(boolean acceptReward);
    public void handleBoxes(List<Coordinates> coords, List<Map<BoxType,Integer>> boxes);
    // forse fare moveBoxes, throwBoxes;
    public void landToPlanet(int planetIdx, List<Coordinates> coords, List<Map<BoxType,Integer>> boxes);
    // forse da separare in choosePlanet e azioni boxes;
    public void removeCrew(List<Coordinates> coords, List<Integer> numCrewMembersLost);
    public void rollDice();
    public void starDust();

    public void fixShip(List<Coordinates> coords);
    public void loadCrew(Coordinates pinkCoords, Coordinates brownCoords);




}
