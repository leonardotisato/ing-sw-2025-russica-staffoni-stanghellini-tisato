package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.LoadCrewAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.network.Client.RMI.ServerHandlerRMI;
import it.polimi.ingsw.cg04.network.Client.Socket.SocketServerHandler;
import it.polimi.ingsw.cg04.network.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ServerTest {

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


    void testPings() throws IOException, InterruptedException {
        SocketServerHandler client = new SocketServerHandler("localhost", "4200", "tui");

        Thread.sleep(10000);
    }

    // @Test
    void testNicknameCollision() throws IOException, InterruptedException {
        SocketServerHandler client1 = new SocketServerHandler("localhost", "4200", "TUI");
        SocketServerHandler client2 = new SocketServerHandler("localhost", "4200", "TUI");

        client1.setNickname("Alice");
        Thread.sleep(500);
        client2.setNickname("Alice");

        client1.createGame(1, 4, PlayerColor.BLUE);

        Thread.sleep(500); // let the controller create the game
        client2.joinGame(0, PlayerColor.RED); // client2 cannot join since it has a nick that collides with other client

        Thread.sleep(500);
        client2.setNickname("Bob"); // client2 changes his nick
        client2.joinGame(0, PlayerColor.RED);

        Thread.sleep(500);
        controller.getGames().getFirst().getPlayers()
                .forEach(player -> System.out.println(player.getName()));
    }

    // @Test
    void testJoinGame() throws IOException, InterruptedException {
        SocketServerHandler client1 = new SocketServerHandler("localhost", "4200", "TUI");
        SocketServerHandler client2 = new SocketServerHandler("localhost", "4200", "TUI");
        SocketServerHandler client3 = new SocketServerHandler("localhost", "4200", "TUI");
        SocketServerHandler client4 = new SocketServerHandler("localhost", "4200", "TUI");

        client1.setNickname("Bob");
        Thread.sleep(500);
        client2.setNickname("Bob");
        Thread.sleep(500);
        assertNull(client2.getNickname());
        client3.setNickname("Alice");
        Thread.sleep(500);
        client1.setNickname("Trent");
        Thread.sleep(500);
        client2.setNickname("Brr Brr Patapim");
        Thread.sleep(500);
        client4.setNickname("Trent");
        Thread.sleep(500);

        System.out.println("Client 1: " + client1.getNickname());
        System.out.println("Client 2: " + client2.getNickname());
        System.out.println("Client 3: " + client3.getNickname());
        System.out.println("Client 4: " + client4.getNickname());

        assertEquals("Bob", client1.getNickname());
        assertEquals("Brr Brr Patapim", client2.getNickname());
        assertEquals("Alice", client3.getNickname());
        assertEquals("Trent", client4.getNickname());
    }

    // @Test
    void testJoinGameRMI() throws IOException, InterruptedException {
        ServerHandlerRMI client1 = new ServerHandlerRMI("localhost", "9696", "TUI");
        ServerHandlerRMI client2 = new ServerHandlerRMI("localhost", "9696", "TUI");
        ServerHandlerRMI client3 = new ServerHandlerRMI("localhost", "9696", "TUI");
        ServerHandlerRMI client4 = new ServerHandlerRMI("localhost", "9696", "TUI");

        client1.setNickname("Bob");
        Thread.sleep(500);
        client2.setNickname("Bob");
        Thread.sleep(500);
        assertNull(client2.getNickname());
        client3.setNickname("Alice");
        Thread.sleep(500);
        client1.setNickname("Trent");
        Thread.sleep(500);
        client2.setNickname("Brr Brr Patapim");
        Thread.sleep(500);
        client4.setNickname("Trent");
        Thread.sleep(500);

        System.out.println("Client 1: " + client1.getNickname());
        System.out.println("Client 2: " + client2.getNickname());
        System.out.println("Client 3: " + client3.getNickname());
        System.out.println("Client 4: " + client4.getNickname());

        assertEquals("Bob", client1.getNickname());
        assertEquals("Brr Brr Patapim", client2.getNickname());
        assertEquals("Alice", client3.getNickname());
        assertEquals("Trent", client4.getNickname());
    }
}