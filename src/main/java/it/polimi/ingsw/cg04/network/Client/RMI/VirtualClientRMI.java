package it.polimi.ingsw.cg04.network.Client.RMI;

import it.polimi.ingsw.cg04.model.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualClientRMI extends Remote {
    void setGameRMI(Game game) throws RemoteException;
    void addLogsRMI(List<String> logs) throws RemoteException;
    void sendJoinableGamesRMI(List<Game.GameInfo> infos) throws RemoteException;

    void pingRMI(String key) throws RemoteException;
    void pongRMI(String key) throws RemoteException;

}
