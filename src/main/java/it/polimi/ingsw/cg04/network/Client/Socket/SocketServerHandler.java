package it.polimi.ingsw.cg04.network.Client.Socket;

import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.JoinGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import it.polimi.ingsw.cg04.network.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketServerHandler extends ServerHandler {

    private final Socket socket;
    private final ObjectOutputStream outputStream;

    protected String lastHeartBeat = "trallalero";

    public SocketServerHandler(String serverIP, String serverPort, String viewType) throws IOException {
        super(serverIP, serverPort, viewType);

        try {
            socket = new Socket(serverIP, Integer.parseInt(serverPort));
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Could not connect to server: " + e.getMessage());
            throw new IOException("Socket connection to server failed");
        }

        // set up a thread that handles a message received from the server
        new Thread(new SocketServerReader(this, socket)).start();

        System.out.println("Connected to server");
    }

    /**
     * Send an action to the server via the socket
     *
     * @param message that contains the action the player wants to perform
     */
    public synchronized void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Failed to send message to server.");
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void createGame(int gameLevel, int maxPlayers, PlayerColor color) {
        Action a = new CreateGameAction(gameLevel, maxPlayers, nickname, color);
        send(new Message("ACTION", a));
    }

    @Override
    public void joinGame(int gameId, PlayerColor color) {
        Action a = new JoinGameAction(gameId, nickname, color);
        send(new Message("ACTION", a));
    }

    @Override
    public void setNickname(String nickname) {
        Action a = new SetNicknameAction(nickname);
        send(new Message("SUBSCRIPTION-REQUEST", a));
    }

    @Override
    public void chooseTile(int tileIdx) {

    }

    @Override
    public void closeFaceUpTiles() {

    }

    @Override
    public void drawFaceDown() {

    }

    @Override
    public void endBuilding(int position) {

    }

    @Override
    public void pickPile(int pileIdx) {

    }

    @Override
    public void place(int x, int y) {

    }

    @Override
    public void placeInBuffer() {

    }

    @Override
    public void returnPile() {

    }

    @Override
    public void returnTile() {

    }

    @Override
    public void showFaceUp() {

    }

    @Override
    public void startTimer() {

    }

    @Override
    public void chooseBattery(int x, int y) {

    }

    @Override
    public void choosePropulsor(List<Coordinates> coordinates, List<Integer> usedBatteries) {

    }

    @Override
    public void compareCrew() {

    }

    @Override
    public void compareFirePower(List<Coordinates> BatteryCoords, List<Coordinates> CannonCoords) {

    }

    @Override
    public void spreadEpidemic() {

    }

    @Override
    public void getNextAdventureCard() {

    }

    @Override
    public void getRewards(boolean acceptReward) {

    }

    @Override
    public void handleBoxes(List<Coordinates> coords, List<Map<BoxType, Integer>> boxes) {

    }

    @Override
    public void landToPlanet(int planetIdx, List<Coordinates> coords, List<Map<BoxType, Integer>> boxes) {

    }

    @Override
    public void removeCrew(List<Coordinates> coords, List<Integer> numCrewMembersLost) {

    }

    @Override
    public void rollDice() {

    }

    @Override
    public void starDust() {

    }

    @Override
    public void fixShip(List<Coordinates> coords) {

    }

    @Override
    public void loadCrew(Coordinates pinkCoords, Coordinates brownCoords) {

    }


    public class SocketServerReader implements Runnable {

        private final SocketServerHandler socketServerHandler;
        private final Socket socket;
        private final ExecutorService inputHandler = Executors.newSingleThreadExecutor();
        private final ScheduledExecutorService connectionChecker = Executors.newSingleThreadScheduledExecutor();

        public SocketServerReader(SocketServerHandler socketServerHandler, Socket socket) {
            this.socketServerHandler = socketServerHandler;
            this.socket = socket;

            connectionChecker.scheduleAtFixedRate(new ConnectionChecker(this.socket), 1, 4, TimeUnit.SECONDS);
        }

        @Override
        public void run() {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    Message message = (Message) inputStream.readObject();
                    handleMessage(message);
                }
            } catch (Exception e) {
                // System.out.println("Server -> Client communication error: " + e.getMessage());
                connectionChecker.shutdown();
                inputHandler.shutdown();
            }
        }

        private void handleMessage(Message message) {
            switch (message.messageType()) {
                case "SUBSCRIPTION-RESPONSE" -> {
                    inputHandler.submit(() -> {
                        String response = (String) message.payload();
                        if(response != null){
                            nickname = response;
                        } else {
                            addLog("Nickname already taken");
                        }
                    });
                }
                case "PING" -> {
                    send(new Message("PONG", message.payload()));
                }
                case "PONG" -> {
                    lastHeartBeat = (String) message.payload();
                }
                case "LOG" -> {
                    socketServerHandler.addLog((String) message.payload());
                }
                default -> {
                    System.out.println("Unknown message type: " + message.messageType());
                }
            }
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
                    socket.close();
                    System.out.println("Connection to server lost");
                } catch (IOException ignored) {}
            }
        }
    }
}
