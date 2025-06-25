package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.controller.PlayerActions.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualControllerRMI extends Remote {
    void handleActionRMI(Action action) throws RemoteException;
    boolean handleSubscriptionRMI(Action action) throws RemoteException;
    List<Game.GameInfo> provideJoinableGamesRMI() throws RemoteException;

    void pingRMI(String key) throws RemoteException;
    void pongRMI(String key) throws RemoteException;
}
