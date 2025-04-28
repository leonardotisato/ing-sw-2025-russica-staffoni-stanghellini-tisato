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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

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

        // set up a thread that handles a message received from the server?
        // new Thread(new SocketServerReader(this, socket)).start();

        System.out.println("Connected to server");
    }

    /**
     * Send an action to the server via the socket
     *
     * @param action player generated action
     */
    public synchronized void send(Action action) {
        try {
            outputStream.writeObject(action);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Failed to send action to server.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setNickname(String nickname) {
        send(new SetNicknameAction(nickname));

        // Set the nickname of the player... but we need to make sure the server has received the action and nick is available...
        this.nickname = nickname;
    }

    @Override
    public void createGame(int gameLevel, int maxPlayers, PlayerColor color) {
        send(new CreateGameAction(gameLevel, maxPlayers, nickname, color));
    }

    @Override
    public void joinGame(int gameId, PlayerColor color) {
        send(new JoinGameAction(gameId, nickname, color));
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
}
