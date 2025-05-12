package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.network.Client.RMI.ServerHandlerRMI;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import it.polimi.ingsw.cg04.network.Client.Socket.SocketServerHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;

public class ConnectionTest {

    GamesController controller;
    Server server;

    @BeforeEach
    void setUp() throws IOException {
        this.controller = new GamesController();
        this.server = new Server(controller);

        server.start();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSubscription() throws IOException, InterruptedException {
        // subscribe a TCP client
        ServerHandler client1 = new SocketServerHandler("localhost", "4200", "TUI");

        client1.setNickname("Alice");
        Thread.sleep(500);

        ServerHandler client2 = new ServerHandlerRMI("localhost", "9696", "TUI");

        client2.setNickname("Bob");
        Thread.sleep(500);
    }

}
