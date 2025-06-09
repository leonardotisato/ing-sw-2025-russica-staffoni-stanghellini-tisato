package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.network.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class SocketClientHandler extends ClientHandler implements Runnable {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ExecutorService inputHandler = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService connectionChecker = Executors.newSingleThreadScheduledExecutor();
    protected String lastHeartBeat = "trallala";

    public SocketClientHandler(GamesController controller, Server server, Socket socket) throws IOException {
        super(controller, server);
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.connectionChecker.scheduleAtFixedRate(new ConnectionChecker(this.socket), 1, 4, TimeUnit.SECONDS);
    }

    /**
     * Sends a message to the client through the output stream.
     * This method synchronizes access to the output stream to ensure thread safety.
     *
     * @param message the {@code Message} object to be sent to the client.
     */
    private void send(Message message) {
        synchronized (outputStream) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Failed to send message to client.");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handles incoming messages from a client through an input stream and processes them
     * based on their type. This method continuously listens for messages as long as the
     * associated socket remains open.
     */
    @Override
    public void run() {

        System.out.println("Wassup from SocketClientHandler");

        ObjectInputStream in;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Could not get input stream for player: " + nickname);
            return;
        }

        while (!socket.isClosed()) {
            try {
                Message message = (Message) in.readObject();

                switch (message.messageType()) {
                    case "ACTION" -> {
                        inputHandler.submit(() -> {
                            try {
                                Action a = (Action) message.payload();
                                if (a.getPlayerNickname() == null) {
                                    addLogs(Collections.singletonList("Player " + nickname + " tried to perform an action without specifying the player nickname"));
                                } else {
                                    handleAction(a);
                                }
                            } catch (Exception e) {
                                addLogs(Collections.singletonList(e.getMessage()));
                            }
                        });
                    }
                    case "SUBSCRIPTION-REQUEST" -> {
                        inputHandler.submit(() -> {
                            try {
                                Action a = (Action) message.payload();
                                if (handleSubscription(a)) {
                                    String response = a.getPlayerNickname();
                                    send(new Message("SUBSCRIPTION-RESPONSE", response));
                                    send(new Message("JOINABLE-GAMES", provideJoinableGames()));
                                } else {
                                    send(new Message("SUBSCRIPTION-RESPONSE", null));
                                }
                            } catch (Exception e) {
                                addLogs(Collections.singletonList(e.getMessage()));
                            }
                        });
                    }
                    case "PING" -> {
                        send(new Message("PONG", message.payload()));
                    }
                    case "PONG" -> {
                        lastHeartBeat = (String) message.payload();
                    }
                    default -> {
                        System.out.println("Unknown message type: " + message.messageType());
                    }
                }

            } catch (Exception e) {
                break;
            }
        }
    }

    /**
     * Updates the game state for the client by sending the provided game object
     * through a message to the client.
     *
     * @param game the {@code Game} object representing the current state of the game
     *             to be sent to the client.
     */
    @Override
    public void setGame(Game game) {
        send(new Message("GAME", game));
    }

    /**
     * Sends a list of log messages to the client. The log messages are sent as a payload
     * within a message with the type "LOG".
     *
     * @param logs the list of log messages to be sent to the client.
     */
    @Override
    public void addLogs(List<String> logs) {
        send(new Message("LOG", logs));
    }

    /**
     * Sends a list of joinable games to the client.
     * This method constructs a message of type "JOINABLE-GAMES"
     * containing the list of game information and sends it through the output stream.
     *
     * @param gameInfos the list of {@code Game.GameInfo} objects representing
     *                  the available games that the client can join.
     */
    @Override
    public void sendJoinableGames(List<Game.GameInfo> gameInfos){send(new Message("JOINABLE-GAMES", gameInfos));}

    /**
     * The {@code ConnectionChecker} class is responsible for monitoring the connection
     * with a client to ensure that it remains active. It periodically sends a heartbeat
     * message to the client and validates the response to detect potential disconnections.
     */
    public class ConnectionChecker implements Runnable {
        private final Socket socket;

        public ConnectionChecker(Socket socket) {
            this.socket = socket;
        }

        /**
         * Periodically performs tasks to monitor and validate the connection with the client.
         * A heartbeat message is generated and sent to the client. The method then waits
         * for a response from the client. If the response does not match the expected heartbeat,
         * it assumes the connection is lost and proceeds to disconnect the client and shut down
         * relevant resources.
         */
        @Override
        public void run() {
            String heartBeat = UUID.randomUUID().toString();

            // System.out.println("Sending heartbeat: " + heartBeat);
            send(new Message("PING", heartBeat));

            // Wait for server response
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }

            if (!heartBeat.equals(lastHeartBeat)) {
                try {
                    disconnect();
                    inputHandler.shutdown();
                    connectionChecker.shutdown();
                    socket.close();
                    System.out.println("Connection with client lost");
                } catch (IOException ignored) {
                }
            }
        }
    }
}
