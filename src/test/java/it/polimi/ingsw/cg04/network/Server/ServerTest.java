package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.LoadCrewAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
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

    @Test
    void testSocketConnection() throws IOException, InterruptedException {
        ObjectOutputStream out = null;
        try {
            Socket socket = new Socket("localhost", 4200);
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.writeObject(new SetNicknameAction("Alice"));
        out.flush();

        // Alice wants to create a game, sends the action to the server
        InitAction createGameAction = new CreateGameAction(2, 4, "Alice", PlayerColor.BLUE);
        out.writeObject(createGameAction);
        out.flush();

        // check that game was created
        Thread.sleep(500);
        assertEquals(1, controller.getGames().size());

        // Bob tries to send a player action as the first action
        PlayerAction playerAction = new LoadCrewAction("Bob", null, null);
        out.writeObject(playerAction);
        out.flush(); // controller does not execute the action

        // A new player named alice tries to create a game
        out.writeObject(new SetNicknameAction("Alice"));
        out.flush();


    }
}