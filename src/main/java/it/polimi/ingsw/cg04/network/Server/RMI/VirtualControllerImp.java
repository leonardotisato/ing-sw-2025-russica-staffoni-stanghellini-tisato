package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
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
    public void handleActionRMI(Action action) throws RemoteException{
        clientHandler.handleAction(action);
    }


}
