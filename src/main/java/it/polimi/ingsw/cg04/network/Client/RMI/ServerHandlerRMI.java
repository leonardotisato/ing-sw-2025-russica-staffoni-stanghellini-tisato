package it.polimi.ingsw.cg04.network.Client.RMI;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.*;
import it.polimi.ingsw.cg04.model.PlayerActions.BuildActions.*;
import it.polimi.ingsw.cg04.model.PlayerActions.FixShipAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LoadCrewAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.JoinGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import it.polimi.ingsw.cg04.network.Server.RMI.ClientHandlerRMI;
import it.polimi.ingsw.cg04.network.Server.RMI.HandlersProviderRMI;
import it.polimi.ingsw.cg04.network.Server.RMI.VirtualControllerRMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerHandlerRMI extends ServerHandler {

    private VirtualControllerRMI virtualController;
    private final VirtualClientRMI virtualClient;

    // Single thread that runs RMI methods
    private final ExecutorService rmiExecutor = Executors.newSingleThreadExecutor(new ClientHandlerRMI.DeamonThreadFactory());

    // Variables needed for connection check
    protected volatile String lastHeartBeat = "Franco";
    private final ScheduledExecutorService serverChecker = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        try {
            new ServerHandlerRMI("localhost", "9696", "GUI");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

    }

    public ServerHandlerRMI(String serverIP, String serverPort, String viewType) throws RemoteException {
        super(serverIP, serverPort, viewType);
        virtualClient = new VirtualClientImp();

        startConnection();

        System.out.println("Connection to server established");

    }


    public void startConnection() throws RemoteException {
        // Try connecting to the server
        try {
            HandlersProviderRMI provider = (HandlersProviderRMI) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/RmiHandlerProvider");
            virtualController = provider.connect(virtualClient);

            // Set up a demon thread to check if server is still up
            Thread demon = new Thread(new RmiServerChecker()); demon.setDaemon(true); demon.setName("Rmi connection checker");
            serverChecker.scheduleAtFixedRate(demon, 1, 4, TimeUnit.SECONDS);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            // todo: uncomment when implemented
            // notifyViewServerUnreachable();
            killRmiReaper();
            throw new RemoteException("Rmi connection to server failed");
        }
    }

    private static void killRmiReaper(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            if(t.getName().contains("RMI")){
                System.out.println("Killing RMI Reaper");
                t.interrupt();
            }
        }
    }

    @Override
    public void setNickname(String nickname) {
        if (super.nickname != null) {
            return;
        }
        Action setNicknameAction = new SetNicknameAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                if(virtualController.handleSubscriptionRMI(setNicknameAction)) {
                    super.nickname = nickname;
                }
                else {
                    super.nickname = null;
                }
            } catch (RemoteException ignored) {
            }
        });
    }

    public void createGame(int gameLevel, int maxPlayers, PlayerColor color) {
        Action createGameAction = new CreateGameAction(gameLevel, maxPlayers, nickname, color);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(createGameAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void joinGame(int gameId, PlayerColor color) {
        Action joinGameAction = new JoinGameAction(gameId, nickname, color);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(joinGameAction);
            } catch (RemoteException ignored) {}
        });
    }

    // buildActions

    public void chooseTile(int tileIdx) {
        // Action chooseTileAction = new ChooseTileAction(nickname, tileIdx);
        Action chooseTileAction = new ChooseTileAction(nickname, tileIdx);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(chooseTileAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void closeFaceUpTiles() {
        Action closeFaceUpTilesAction = new CloseFaceUpTilesAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(closeFaceUpTilesAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void drawFaceDown() {
        Action drawFaceDownAction = new DrawFaceDownAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(drawFaceDownAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void endBuilding(int position) {
        Action endBuildingAction = new EndBuildingAction(nickname, position);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(endBuildingAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void pickPile(int pileIdx) {
        Action pickPileAction = new PickPileAction(nickname, pileIdx);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(pickPileAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void place(int x, int y) {
        Action placeAction = new PlaceAction(nickname, x, y);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(placeAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void placeInBuffer() {
        Action placeInBufferAction = new PlaceInBufferAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(placeInBufferAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void returnPile() {
        Action returnPileAction = new ReturnPileAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(returnPileAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void returnTile() {
        Action returnTileAction = new ReturnTileAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(returnTileAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void showFaceUp() {
        Action showFaceUpAction = new ShowFaceUpAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(showFaceUpAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void startTimer() {
        Action startTimeAction = new StartTimerAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(startTimeAction);
            } catch (RemoteException ignored) {}
        });
    }

    //adventureCardActions
    public void chooseBattery(int x, int y) {
        Action chooseBatteryAction = new ChooseBatteryAction(nickname, x, y);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(chooseBatteryAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void choosePropulsor(List<Coordinates> coordinates, List<Integer> usedBatteries) {
        Action choosePropulsorAction = new ChoosePropulsorAction(nickname, coordinates, usedBatteries);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(choosePropulsorAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void compareCrew() {
        Action compareCrewAction = new CompareCrewAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(compareCrewAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void compareFirePower(List<Coordinates> BatteryCoords, List <Coordinates> CannonCoords) {
        Action compareFirePowerAction = new CompareFirePowerAction(nickname, BatteryCoords, CannonCoords);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(compareFirePowerAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void spreadEpidemic() {
        Action epidemicAction = new EpidemicAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(epidemicAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void getNextAdventureCard() {
        Action getNextAdventureCardAction = new GetNextAdventureCardAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(getNextAdventureCardAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void getRewards(boolean acceptReward) {
        Action getRewardsAction = new GetRewardsAction(nickname, acceptReward);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(getRewardsAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void handleBoxes(List<Coordinates> coords, List<Map<BoxType,Integer>> boxes) {
        Action handleBoxesAction = new HandleBoxesAction(nickname, coords, boxes);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(handleBoxesAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void landToPlanet(int planetIdx, List<Coordinates> coords, List<Map<BoxType,Integer>> boxes) {
        Action planetsAction = new PlanetsAction(nickname, planetIdx, coords, boxes);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(planetsAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void removeCrew(List<Coordinates> coords, List<Integer> numCrewMembersLost) {
        Action removeCrewAction = new RemoveCrewAction(nickname, coords, numCrewMembersLost);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(removeCrewAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void rollDice() {
        Action rollDiceAction = new RollDiceAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(rollDiceAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void starDust() {
        Action starDustAction = new StardustAction(nickname);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(starDustAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void fixShip(List<Coordinates> coords) {
        Action fixShipAction = new FixShipAction(nickname, coords);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(fixShipAction);
            } catch (RemoteException ignored) {}
        });
    }

    public void loadCrew(Coordinates pinkCoords, Coordinates brownCoords) {
        Action loadCrewAction = new LoadCrewAction(nickname, pinkCoords, brownCoords);
        rmiExecutor.submit(() -> {
            try {
                virtualController.handleActionRMI(loadCrewAction);
            } catch (RemoteException ignored) {}
        });
    }

    class VirtualClientImp extends UnicastRemoteObject implements VirtualClientRMI {
        protected VirtualClientImp() throws RemoteException { }

        // todo: implement
        @Override
        public void setGameRMI(Game game) throws RemoteException {

        }

        // todo: implement (now it's printing, we will modify it once implementing the view)
        @Override
        public void addLogRMI(String log) throws RemoteException {
            addLog(log);
        }

        @Override
        public void pingRMI(String key) throws RemoteException {
            virtualController.pongRMI(key);
        }

        @Override
        public void pongRMI(String key) throws RemoteException {
            System.out.println("Received: " + key);
            lastHeartBeat = key;
        }
    }

    class RmiServerChecker implements Runnable {
        @Override
        public void run() {
            // Generate a random string
            String heartBeat = UUID.randomUUID().toString();

            //System.out.println("Sending: " + heartBeat);

            Thread pinger = new Thread(() -> {
                try {
                    virtualController.pingRMI(heartBeat);
                } catch (RemoteException ignored) {}
            });
            pinger.setDaemon(true);
            pinger.start();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}

            // Check if client responded to the ping message
            if(!heartBeat.equals(lastHeartBeat)) {
                new Thread(() -> {
                    // todo: uncomment when implemented
                    // notifyViewServerUnreachable();
                    serverChecker.shutdownNow();
                    rmiExecutor.shutdownNow();
                    killRmiReaper();
                }).start();
            }
        }
    }
}
