package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.controller.GamesController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class VirtualControllerImp extends UnicastRemoteObject implements VirtualControllerRMI{
    private GamesController controller;
    public VirtualControllerImp(GamesController controller) throws RemoteException {
        this.controller = controller;
    }
    public void setNicknameRMI(String nickname) throws RemoteException {
        controller.onActionReceived(new setNicknameAction);
    }

}
