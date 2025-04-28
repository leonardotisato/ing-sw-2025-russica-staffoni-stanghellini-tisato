package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.LoadCrewAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
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

class ServerTest {

    GamesController controller;
    Server server;

    @BeforeEach
    void setUp() throws RemoteException {
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

    @Test
    void testNicknameCollision() throws IOException, InterruptedException {
        SocketServerHandler client1 = new SocketServerHandler("localhost", "4200", "TUI");
        SocketServerHandler client2 = new SocketServerHandler("localhost", "4200", "TUI");

        client1.setNickname("Alice");
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
}