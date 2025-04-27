package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.network.Client.RMI.VirtualClientRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HandlersProviderRMI extends Remote {

    VirtualControllerRMI connect(VirtualClientRMI virtualClient) throws RemoteException;
}
