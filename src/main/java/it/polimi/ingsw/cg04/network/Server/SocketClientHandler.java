package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.network.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Failed to send message to client.");
            System.out.println(e.getMessage());
        }
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
                                Action a = (Action) message.payload();
                                if (a.getPlayerNickname() == null){
                                    addLog("Player " + nickname + " tried to perform an action without specifying the player nickname");
                                } else {
                                    handleAction(a);
                                }
                            } catch (Exception e) {
                                addLog(e.getMessage());
                            }
                        });
                    }
                    case "SUBSCRIPTION-REQUEST" -> {
                        inputHandler.submit(() -> {
                            try {
                                Action a = (Action) message.payload();
                                if(handleSubscription(a)){
                                    String response = a.getPlayerNickname();
                                    send(new Message("SUBSCRIPTION-RESPONSE", response));
                                } else{
                                    send(new Message("SUBSCRIPTION-RESPONSE", null));
                                }
                            } catch (Exception e) {
                                addLog(e.getMessage());
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

    public class ConnectionChecker implements Runnable {
        private final Socket socket;

        public ConnectionChecker(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {
            String heartBeat = UUID.randomUUID().toString();

            // System.out.println("Sending heartbeat: " + heartBeat);
            send(new Message("PING", heartBeat));

            // Wait for server response
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}

            if (!heartBeat.equals(lastHeartBeat)) {
                try {
                    disconnect();
                    socket.close();
                    System.out.println("Connection with client lost");
                } catch (IOException ignored) {}
            }
        }
    }
}
