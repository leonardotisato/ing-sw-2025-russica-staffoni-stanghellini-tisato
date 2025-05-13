package it.polimi.ingsw.cg04.network.Client;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;
import java.util.Map;

public interface VirtualServer {
    // initActions
    void setNickname(String nickname);
    void createGame(int gameLevel, int maxPlayers, PlayerColor color);
    void joinGame(int gameId, PlayerColor color);

    // buildActions
    void chooseTile(int tileIdx);
    void chooseTileFromBuffer(int idx);
    void closeFaceUpTiles();
    void drawFaceDown();
    void endBuilding(int position);
    void pickPile(int pileIdx);
    void place(int x, int y);
    void rotate(String type);
    void placeInBuffer();
    void returnPile();
    void returnTile();
    void showFaceUp();
    void startTimer();

    //adventureCardActions
    void chooseBattery(int x, int y);
    void choosePropulsor(List<Coordinates> coordinates, List<Integer> usedBatteries);
    void compareCrew();
    void compareFirePower(List<Coordinates> BatteryCoords, List <Coordinates> CannonCoords);
    void spreadEpidemic();
    void getNextAdventureCard();
    void getRewards(boolean acceptReward);
    void handleBoxes(List<Coordinates> coords, List<Map<BoxType,Integer>> boxes);
    // forse fare moveBoxes, throwBoxes;
    void landToPlanet(Integer planetIdx, List<Coordinates> coords, List<Map<BoxType,Integer>> boxes);
    // forse da separare in choosePlanet e azioni boxes;
    void removeCrew(List<Coordinates> coords, List<Integer> numCrewMembersLost);
    void rollDice();
    void starDust();
    void retire();
    void fixShip(List<Coordinates> coords);
    void loadCrew(Coordinates pinkCoords, Coordinates brownCoords);

    // todo: add methods to leave the game, rejoin after disconnecting, ending game, retire (and similar, check rules)




}
