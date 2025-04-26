package it.polimi.ingsw.cg04.network.Client.RMI;

import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import it.polimi.ingsw.cg04.network.Server.RMI.ClientHandlerRMI;
import it.polimi.ingsw.cg04.network.Server.RMI.HandlersProviderRMI;
import it.polimi.ingsw.cg04.network.Server.RMI.VirtualControllerRMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandlerRMI extends ServerHandler {

    private VirtualControllerRMI virtualController;
    private final VirtualClientRMI virtualClient;

    // Single thread that runs RMI methods
    private final ExecutorService rmiExecutor = Executors.newSingleThreadExecutor(new ClientHandlerRMI.DeamonThreadFactory());

    public ServerHandlerRMI(String serverIP, String serverPort, String viewType) throws RemoteException {
        super(serverIP, serverPort, viewType);
        virtualClient = new VirtualClientImp();
//
        startConnection();
//
        System.out.println("Connection to server established");
    }

    public static void main(String[] args) {
        try {
            new ServerHandlerRMI("localhost", "9696", "GUI");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

    }


    public void startConnection() throws RemoteException {
        // Try connecting to the server
        try {
            HandlersProviderRMI provider = (HandlersProviderRMI) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/RmiHandlerProvider");
            virtualController = provider.connect(virtualClient);

            // Set up a demon thread to check if server is still up
            Thread demon = new Thread(new RmiServerChecker()); demon.setDaemon(true); demon.setName("Rmi connection checker");
            //serverChecker.scheduleAtFixedRate(demon, 1, 4, TimeUnit.SECONDS);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            //notifyViewServerUnreachable();
            //killRmiReaper();
            throw new RemoteException("Rmi connection to server failed");
        }
    }


    public void setNickname(String nickname) {
        Action setNicknameAction = new SetNicknameAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(setNicknameAction);
            } catch (RemoteException ignored) {}
        });

    }

    public void createGame(int gameLevel, int maxPlayers) {

    }

    public void joinGame(int gameId) {

    }

    // buildActions
    public void chooseTile(int tileIdx) {

    }

    public void closeFaceUpTiles() {

    }

    public void drawFaceDown() {

    }

    public void endBuilding(int position) {

    }

    public void pickPile(int pileIdx) {

    }

    public void place(int x, int y) {

    }

    public void placeInBuffer() {

    }

    public void returnPile() {

    }

    public void returnTile() {

    }

    public void showFaceUp() {

    }

    public void startTimer() {

    }

    //adventureCardActions
    public void chooseBattery(int x, int y) {

    }

    public void choosePropulsor(List<Coordinates> coordinates, List<Integer> usedBatteries) {

    }

    public void compareCrew() {

    }

    public void compareFirePower(List<Coordinates> BatteryCoords, List <Coordinates> CannonCoords) {

    }

    public void spreadEpidemic() {

    }

    public void getNextAdventureCard() {

    }

    public void getRewards(boolean acceptReward) {

    }

    public void handleBoxes(List<Coordinates> coords, List<Map<BoxType,Integer>> boxes) {

    }

    // forse fare moveBoxes, throwBoxes;
    public void landToPlanet(int planetIdx, List<Coordinates> coords, List<Map<BoxType,Integer>> boxes) {

    }

    // forse da separare in choosePlanet e azioni boxes;
    public void removeCrew(List<Coordinates> coords, List<Integer> numCrewMembersLost) {

    }

    public void rollDice() {

    }

    public void starDust() {

    }

    public void fixShip(List<Coordinates> coords) {

    }

    public void loadCrew(Coordinates pinkCoords, Coordinates brownCoords) {

    }




    class RmiServerChecker implements Runnable {
        @Override
        public void run() {
//            // Generate a random string
//            String heartBeat = UUID.randomUUID().toString();
//
//            //System.out.println("Sending: " + heartBeat);
//
//            Thread pinger = new Thread(() -> {
//                try {
//                    virtualController.pingRMI(heartBeat);
//                } catch (RemoteException ignored) {}
//            });
//            pinger.setDaemon(true);
//            pinger.start();
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException ignored) {}
//
//            // Check if client responded to the ping message
//            if(!heartBeat.equals(lastHeartBeat)) {
//                new Thread(() -> {
//                    notifyViewServerUnreachable();
//                    serverChecker.shutdownNow();
//                    rmiExecutor.shutdownNow();
//                    killRmiReaper();
//                }).start();
//            }
        }
    }
}
