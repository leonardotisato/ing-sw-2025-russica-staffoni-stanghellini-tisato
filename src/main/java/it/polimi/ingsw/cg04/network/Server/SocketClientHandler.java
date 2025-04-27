package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.network.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClientHandler extends ClientHandler implements Runnable {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ExecutorService inputHandler = Executors.newSingleThreadExecutor();

    public SocketClientHandler(GamesController controller, Server server, Socket socket) throws IOException {
        super(controller, server);
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {

        System.out.println("Greetings from SocketClientHandler");

        ObjectInputStream in;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Could not get input stream for player: " + nickname);
            return;
        }

        while (true) {
            try {
                Message message = (Message) in.readObject();

                switch (message.messageType()) {
                    case "ACTION" -> {
                        inputHandler.submit(() -> {
                            try {
                                handleAction((Action) message.payload());
                            } catch (Exception e) {
                                addLog(e.getMessage());
                            }
                        });
                    }
                    case "PING" -> {
                        // handle ping
                    }
                    case "PONG" -> {
                        // handle pong
                    }
                    default -> {
                        System.out.println("Unknown message type: " + message.messageType());
                    }
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void setGame(Game game) {    }

    // send message to client
    @Override
    public void addLog(String log) {
        try {
            outputStream.writeObject(new Message("LOG", log));
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Failed to send action to server.");
            System.out.println(e.getMessage());
        }
    }
}
