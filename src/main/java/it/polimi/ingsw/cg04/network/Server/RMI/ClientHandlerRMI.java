package it.polimi.ingsw.cg04.network.Server.RMI;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.network.Server.ClientHandler;
import it.polimi.ingsw.cg04.network.Server.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientHandlerRMI extends ClientHandler {
    private final VirtualClientRMI virtualClient;

    private final VirtualControllerImp virtualController;

    // Single thread that runs RMI methods
    private final ExecutorService rmiExecutor = Executors.newSingleThreadExecutor(new DeamonThreadFactory());

    // variables needed for connection check
    protected volatile String lastHeartBeat = "Manuel";
    private final ScheduledExecutorService connectionDemon = Executors.newSingleThreadScheduledExecutor();

    /**
     * Construct a ClientHandler that connects to the client via socket
     * @param controller of the game
     * @param server to subscribe to
     * @param virtualClient reference to the client, used to call RMI methods on
     * @throws RemoteException if something goes wrong during connection
     */
    public ClientHandlerRMI(GamesController controller, Server server, VirtualClientRMI virtualClient) throws RemoteException {
        super(controller, server);
        this.virtualClient = virtualClient;
        this.virtualController = new VirtualControllerImp();

        connectionDemon.scheduleAtFixedRate(new ClientCheckerRMI(), 1, 4, TimeUnit.SECONDS);
    }

    // This getter is needed to give the Client an object that implements the RmiVirtualController interface
    public VirtualControllerRMI getRmiVirtualController() {
        return virtualController;
    }


    private class ClientCheckerRMI implements Runnable {
        @Override
        public void run() {
            // Generate a random string
            String heartBeat = UUID.randomUUID().toString();

            //System.out.println("Sending: " + heartBeat);

            Thread pinger = new Thread(() -> {
                try {
                    virtualClient.pingRMI(heartBeat);
                } catch (RemoteException ignored) {}
            });
            pinger.setDaemon(true);
            pinger.start();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}


            // Check if client responded to the ping message
            if (!heartBeat.equals(lastHeartBeat)) {
                disconnect();
                connectionDemon.shutdown();
                rmiExecutor.shutdown();
            }
        }
    }
}
