package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler extends ClientHandler implements Runnable {

    private final Socket socket;
    private final ObjectOutputStream out;

    public SocketClientHandler(GamesController controller, Server server, Socket socket) throws IOException {
        super(controller, server);
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {

        System.out.println("Greetings from SocketClientHandler");

        ObjectInputStream in;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Could not get input stream for player: " + nickName);
            return;
        }

        while (true) {
            try {
                Action action = (Action) in.readObject();

                // let the controller handle the action... naive, but make it work first!
                action.dispatchTo(controller);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
