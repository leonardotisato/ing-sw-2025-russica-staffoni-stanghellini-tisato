package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.network.Server.RMI.HandlersProviderImp;
import it.polimi.ingsw.cg04.network.Server.RMI.HandlersProviderRMI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final GamesController controller;
    private final List<ClientHandler> clientHandlers;

    public Server(GamesController controller) {
        this.controller = controller;
        this.clientHandlers = new ArrayList<ClientHandler>();
    }

    // todo: questo probabilmente non serve... basta far partire tutto con start() nel main x il server...
//    public static void main(String[] args) {
//        GamesController controller = new GamesController();
//        Server server = new Server(controller);
//        try {
//            server.start();
//        } catch (RemoteException e) {
//            System.out.println("Failed to start the server: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public void start() throws RemoteException {
        new Thread(this::startSocket).start();
        startRMI();
    }

    private void startSocket() {

        ServerSocket serverSocket = null;
        // Define a fixed pool of threads to handle clients connections
        final ExecutorService threadPool = Executors.newFixedThreadPool(8);

        // Create a server socket to accept connections from client
        try {
            serverSocket = new ServerSocket(4200);
        } catch (IOException e) {
            System.out.println("Server could not be created");
        }

        // Keep accepting connections
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                System.out.println("New connection from " + socket.getRemoteSocketAddress());
                threadPool.submit(new SocketClientHandler(controller, this, socket));
            } catch (IOException e) {
                System.out.println("Error while accepting new connection");
                break;
            }
        }

        System.out.println("Shutting down thread pool");
        threadPool.shutdown();
    }

    private void startRMI() throws RemoteException {
        HandlersProviderRMI handlersProvider = new HandlersProviderImp(controller, this);
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(9696);
            registry.bind("RmiHandlerProvider", handlersProvider);
            System.out.println("RMI Server bound and ready on port: 9696");
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Failed to start RMI server");
        }
    }

    // todo : use this
    public synchronized void subscribe(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
        System.out.println("Subscribed client: " + clientHandler.getNickName());
    }

    // todo : use this
    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Unsubscribed client: " + clientHandler.getNickName());
    }

    public synchronized boolean isSubscribed(ClientHandler clientHandler) {
        return clientHandlers.contains(clientHandler);
    }

    private synchronized ClientHandler getSubscribedClient(String nickname) {
        for(ClientHandler clientHandler : clientHandlers) {
            if(clientHandler.getNickName().equals(nickname)) {
                return clientHandler;
            }
        }
        return null;
    }

    public void broadcastGameUpdate(List<String> nicknames) {
        for (String nickname : nicknames) {
            ClientHandler clientHandler = getSubscribedClient(nickname);
            if (clientHandler != null) {

                int gameId = controller.getGameFromNickname(nickname).getId();

                // create snapshot of game and send it to the clients
                getSubscribedClient(nickname).setGame(controller.getGame(gameId).deepCopy());

            } else {
                System.out.println("Client " + nickname + " not found");
            }
        }
    }

    public void broadcastLog(List<String> nicknames, String log) {
        for (String nickname : nicknames) {
            ClientHandler clientHandler = getSubscribedClient(nickname);
            if (clientHandler != null) {
                clientHandler.addLog(log);
            } else {
                System.out.println("Client " + nickname + " not found");
            }
        }
    }



}
