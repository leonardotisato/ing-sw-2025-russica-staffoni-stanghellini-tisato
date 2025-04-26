package it.polimi.ingsw.cg04.network.Client.RMI;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ServerHandlerRMI extends ServerHandler {

    public ServerHandlerRMI(String serverIP, String serverPort, String viewType) throws RemoteException {
        super(serverIP, serverPort, viewType);
//        rmiFromServer = new RmiFromServer();
//
//        startConnection();
//
//        System.out.println("Connection to server established");
    }

    public void setNickname(String nickname) {

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
