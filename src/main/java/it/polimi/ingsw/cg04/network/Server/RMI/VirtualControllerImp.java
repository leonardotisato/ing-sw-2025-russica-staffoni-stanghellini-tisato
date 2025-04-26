package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.BuildActions.ChooseTileAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.JoinGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.network.Server.ClientHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class VirtualControllerImp extends UnicastRemoteObject implements VirtualControllerRMI{
    private ClientHandler clientHandler;
    public VirtualControllerImp(ClientHandler clientHandler) throws RemoteException {
        this.clientHandler = clientHandler;
    }
    @Override
    public void setNicknameRMI(String nickname) throws RemoteException {
        clientHandler.setNickname(String nickname);
    }

    @Override
    public void createGameRMI(int gameLevel, int maxPlayers, String playerName, PlayerColor playerColor) throws RemoteException {
        controller.onInitActionReceived(new CreateGameAction(gameLevel, maxPlayers, playerName, playerColor));
    }

    @Override
    public void joinGameRMI(int gameId, String playerName, PlayerColor playerColor) throws RemoteException {
        controller.onInitActionReceived(new JoinGameAction(gameId, playerName, playerColor));
    }

    @Override
    public void chooseTileRMI(String playerName, int tileIdx) throws RemoteException {
        //TODO modifica chooseTileAction
        //controller.onActionReceived(new ChooseTileAction(playerName, tileIdx));
    }

    @Override
    public void closeFaceUpTilesRMI() throws RemoteException {

    }

    @Override
    public void drawFaceDownRMI() throws RemoteException {

    }

    @Override
    public void endBuildingRMI(int position) throws RemoteException {

    }

    @Override
    public void pickPileRMI(int pileIdx) throws RemoteException {

    }

    @Override
    public void placeRMI(int x, int y) throws RemoteException {

    }

    @Override
    public void placeInBufferRMI() throws RemoteException {

    }

    @Override
    public void returnPileRMI() throws RemoteException {

    }

    @Override
    public void returnTileRMI() throws RemoteException {

    }

    @Override
    public void showFaceUpRMI() throws RemoteException {

    }

    @Override
    public void startTimerRMI() throws RemoteException {

    }

    @Override
    public void chooseBatteryRMI(int x, int y) throws RemoteException {

    }

    @Override
    public void choosePropulsorRMI(List<Coordinates> coordinates, List<Integer> usedBatteries) throws RemoteException {

    }

    @Override
    public void compareCrewRMI() throws RemoteException {

    }

    @Override
    public void compareFirePowerRMI(List<Coordinates> BatteryCoords, List<Coordinates> CannonCoords) throws RemoteException {

    }

    @Override
    public void spreadEpidemicRMI() throws RemoteException {

    }

    @Override
    public void getNextAdventureCardRMI() throws RemoteException {

    }

    @Override
    public void getRewardsRMI(boolean acceptReward) throws RemoteException {

    }

    @Override
    public void handleBoxesRMI(List<Coordinates> coords, List<Map<BoxType, Integer>> boxes) throws RemoteException {

    }

    @Override
    public void landToPlanetRMI(int planetIdx, List<Coordinates> coords, List<Map<BoxType, Integer>> boxes) throws RemoteException {

    }

    @Override
    public void removeCrewRMI(List<Coordinates> coords, List<Integer> numCrewMembersLost) throws RemoteException {

    }

    @Override
    public void rollDiceRMI() throws RemoteException {

    }

    @Override
    public void starDustRMI() throws RemoteException {

    }

    @Override
    public void fixShipRMI(List<Coordinates> coords) throws RemoteException {

    }

    @Override
    public void loadCrewRMI(Coordinates pinkCoords, Coordinates brownCoords) throws RemoteException {

    }


}
