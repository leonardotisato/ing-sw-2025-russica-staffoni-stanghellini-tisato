package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.network.Client.RMI.VirtualClientRMI;
import it.polimi.ingsw.cg04.network.Server.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HandlersProviderImp extends UnicastRemoteObject implements HandlersProviderRMI{
    private final GamesController controller;
    private final Server server;

    public HandlersProviderImp(GamesController controller, Server server) throws RemoteException {
        this.controller = controller;
        this.server = server;
    }

    @Override
    public VirtualControllerRMI connect(VirtualClientRMI virtualClient) throws RemoteException {
        System.out.println("New rmi connection!");
        return new ClientHandlerRMI(controller, server, virtualClient).getRmiVirtualController();
    }
}
