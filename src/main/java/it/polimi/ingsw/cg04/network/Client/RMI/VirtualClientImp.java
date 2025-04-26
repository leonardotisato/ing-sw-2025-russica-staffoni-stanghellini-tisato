package it.polimi.ingsw.cg04.network.Client.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class VirtualClientImp extends UnicastRemoteObject implements VirtualClientRMI {
    protected VirtualClientImp() throws RemoteException {    }


    @Override
    public void pingRMI(String key) throws RemoteException {

    }

    @Override
    public void pongRMI(String key) throws RemoteException {

    }
}
