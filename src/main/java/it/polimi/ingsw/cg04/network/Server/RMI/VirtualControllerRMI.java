package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualControllerRMI extends Remote {
    // initActions
    public void setNicknameRMI(String nickname) throws RemoteException;
    public void createGameRMI(int gameLevel, int maxPlayers) throws RemoteException;
    public void joinGameRMI(int gameId) throws RemoteException;

    // buildActions
    public void chooseTileRMI(int tileIdx) throws RemoteException;
    public void closeFaceUpTilesRMI() throws RemoteException;
    public void drawFaceDownRMI() throws RemoteException;
    public void endBuildingRMI(int position) throws RemoteException;
    public void pickPileRMI(int pileIdx) throws RemoteException;
    public void placeRMI(int x, int y) throws RemoteException;
    public void placeInBufferRMI() throws RemoteException;
    public void returnPileRMI() throws RemoteException;
    public void returnTileRMI() throws RemoteException;
    public void showFaceUpRMI() throws RemoteException;
    public void startTimerRMI() throws RemoteException;

    //adventureCardActions
    public void chooseBatteryRMI(int x, int y) throws RemoteException;
    public void choosePropulsorRMI(List<Coordinates> coordinates, List<Integer> usedBatteries) throws RemoteException;
    public void compareCrewRMI() throws RemoteException;
    public void compareFirePowerRMI(List<Coordinates> BatteryCoords, List <Coordinates> CannonCoords) throws RemoteException;
    public void spreadEpidemicRMI() throws RemoteException;
    public void getNextAdventureCardRMI() throws RemoteException;
    public void getRewardsRMI(boolean acceptReward) throws RemoteException;
    public void handleBoxesRMI(List<Coordinates> coords, List<Map<BoxType,Integer>> boxes) throws RemoteException;
    // forse fare moveBoxes, throwBoxes;
    public void landToPlanetRMI(int planetIdx, List<Coordinates> coords, List<Map<BoxType,Integer>> boxes) throws RemoteException;
    // forse da separare in choosePlanet e azioni boxes;
    public void removeCrewRMI(List<Coordinates> coords, List<Integer> numCrewMembersLost) throws RemoteException;
    public void rollDiceRMI() throws RemoteException;
    public void starDustRMI() throws RemoteException;

    public void fixShipRMI(List<Coordinates> coords) throws RemoteException;
    public void loadCrewRMI(Coordinates pinkCoords, Coordinates brownCoords) throws RemoteException;
}
