package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
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
        this.clientHandlers = new ArrayList<>();
    }

    /**
     * Starts the server by initializing both the socket-based and RMI-based components.
     *
     * @throws RemoteException if an issue occurs while setting up the RMI components.
     */
    public void start() throws RemoteException {
        new Thread(this::startSocket).start();
        startRMI();
    }

    /**
     * Initializes and starts the server socket to listen for incoming client connections.
     * The method creates a server socket bound to port 4200 and manages client connections
     * using a fixed thread pool with 8 threads. Incoming connections are handled by creating
     * separate `SocketClientHandler` instances for each client.
     */
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

    /**
     * Starts the RMI (Remote Method Invocation) server for the application.
     *
     * @throws RemoteException if an issue occurs during the initialization or binding
     *         of the RMI components.
     */
    private void startRMI() throws RemoteException {
        HandlersProviderRMI handlersProvider = new HandlersProviderImp(controller, this);
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(9696);
            registry.bind("RmiHandlerProvider", handlersProvider);
            System.out.println("RMI Server bound and ready on port: 9696");
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Failed to start RMI server");
        }
    }


    /**
     * Subscribes a client to the server.
     *
     * This method adds the given {@code ClientHandler} to the list of subscribed clients
     * and logs the nickname of the subscribed client.
     *
     * @param clientHandler the {@code ClientHandler} instance representing the client to be subscribed.
     */
    public synchronized void subscribe(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
        System.out.println("Subscribed client: " + clientHandler.getNickName());
    }

    /**
     * Unsubscribes a client from the server.
     *
     * This method removes the specified {@code ClientHandler} instance from the list of subscribed clients
     * and logs the nickname of the unsubscribed client.
     *
     * @param clientHandler the {@code ClientHandler} instance representing the client to be unsubscribed.
     */
    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Unsubscribed client: " + clientHandler.getNickName());
    }

    // todo: no usages
    public synchronized boolean isSubscribed(ClientHandler clientHandler) {
        return clientHandlers.contains(clientHandler);
    }

    /**
     * Retrieves the subscribed {@code ClientHandler} associated with the given nickname.
     *
     * @param nickname the nickname of the client to be retrieved.
     * @return the {@code ClientHandler} instance associated with the given nickname,
     *         or {@code null} if no subscribed client with the specified nickname exists.
     */
    private synchronized ClientHandler getSubscribedClient(String nickname) {
        for(ClientHandler clientHandler : clientHandlers) {
            if(clientHandler.getNickName().equals(nickname)) {
                return clientHandler;
            }
        }
        return null;
    }

    /**
     * Broadcasts a game update to a list of subscribed clients.
     *
     * The method iterates through the provided list of nicknames, retrieves the associated
     * {@code ClientHandler} for each nickname, and updates their game state with the provided {@code Game}.
     *
     * @param nicknames the list of nicknames identifying the clients to whom the game update should be broadcast.
     * @param game the updated {@code Game} instance to be sent to the clients.
     */
    public void broadcastGameUpdate(List<String> nicknames, Game game) {
        for (String nickname : nicknames) {
            ClientHandler clientHandler = getSubscribedClient(nickname);
            if (clientHandler != null) {
                getSubscribedClient(nickname).setGame(game);
            } else {
                System.out.println("Client " + nickname + " not found");
            }
        }
    }

    /**
     * Broadcasts log messages to a list of subscribed clients identified by their nicknames.
     *
     * This method iterates over the provided list of nicknames, retrieves the corresponding
     * {@code ClientHandler} for each nickname, and sends the given list of logs to those
     * clients.
     *
     * @param nicknames the list of nicknames identifying the clients to whom the log messages should be broadcast.
     * @param logs the list of log messages to be sent to the subscribed clients.
     */
    public void broadcastLogs(List<String> nicknames, List<String> logs) {
        for (String nickname : nicknames) {
            ClientHandler clientHandler = getSubscribedClient(nickname);
            if (clientHandler != null) {
                clientHandler.addLogs(logs);
            } else {
                System.out.println("Client " + nickname + " not found");
            }
        }
    }

    /**
     * Broadcasts information about joinable games to a list of subscribed clients.
     *
     * The method iterates through the provided list of nicknames, retrieves the associated
     * {@code ClientHandler} for each nickname, and sends the list of {@code Game.GameInfo}
     * instances representing joinable games to those clients.
     *
     * @param nicknames the list of nicknames identifying the clients to whom the
     *                  joinable game information should be broadcast.
     * @param infos the list of {@code Game.GameInfo} instances containing details
     *              about the joinable games to be sent to the subscribed clients.
     */
    public void broadcastJoinableGames(List<String> nicknames, List<Game.GameInfo> infos) {
        for (String nickname : nicknames) {
            ClientHandler clientHandler = getSubscribedClient(nickname);
            if (clientHandler != null) {
                clientHandler.sendJoinableGames(infos);
            } else {
                System.out.println("Client " + nickname + " not found");
            }
        }
    }
}
