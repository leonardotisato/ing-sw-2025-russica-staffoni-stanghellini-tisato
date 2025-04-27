package it.polimi.ingsw.cg04.network.Client.RMI;

import it.polimi.ingsw.cg04.model.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClientRMI extends Remote {
    void setGameRMI(Game game) throws RemoteException;
    void addLogRMI(String log) throws RemoteException;

    void pingRMI(String key) throws RemoteException;
    void pongRMI(String key) throws RemoteException;

}
