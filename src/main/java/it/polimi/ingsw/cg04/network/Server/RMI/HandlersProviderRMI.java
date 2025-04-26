package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.network.Client.RMI.VirtualClientRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HandlersProviderRMI extends Remote {
    /**
     * @return an object that implements the methods needed to play the game
     * @throws RemoteException if something goes wrong during RMI connection or method invocation
     */
    public VirtualControllerRMI connect(VirtualClientRMI virtualClient) throws RemoteException;
}
