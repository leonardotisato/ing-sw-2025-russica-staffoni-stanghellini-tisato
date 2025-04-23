package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
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

    public void start() throws RemoteException {
        new Thread(this::startSocket).start();
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

}
