package it.polimi.ingsw.cg04.network.Client.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClientRMI extends Remote {

    public void pingRMI(String key) throws RemoteException;
    public void pongRMI(String key) throws RemoteException;

}
