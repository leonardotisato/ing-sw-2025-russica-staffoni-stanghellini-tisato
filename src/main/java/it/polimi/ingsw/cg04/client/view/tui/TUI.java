package it.polimi.ingsw.cg04.client.view.tui;

import it.polimi.ingsw.cg04.client.ClientModel;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import it.polimi.ingsw.cg04.client.view.View;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class TUI extends View {
    private final InputReader input;
    private final ReentrantLock terminalLock = new ReentrantLock();

    public TUI(ServerHandler server, ClientModel clientModel) {
        super(server, clientModel);
        System.out.println("Welcome to Galaxy Trucker!");
        System.out.println("Type helper to receive the list of commands.");
        System.out.println("Once typed the command you decided to use, an explanation of it's accepted input format will be displayed.");
        System.out.println("Note that not every command requires parameters.");

        input = new InputReader();
        Thread t = new Thread(input);
        t.setName("Stdin InputReader");
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void updateGame(String nickname) {
        try {
            Game toPrint = clientModel.getGame();
            String rendered = toPrint.render(nickname);

            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println(rendered);
            System.out.println("\n".repeat(10));
        } catch (NullPointerException ignored) {
            System.out.println("Game not found");
        }
    }


    public void updateLogs() {
        terminalLock.lock();
        try {
            List<String> logs = clientModel.getLogs();

            int logLines = 10;

            // 1. Risali di 'logLines' + intestazione
            System.out.print("\033[" + (logLines + 1) + "A"); // risale di (logLines + 1) righe
            System.out.flush();

            // 2. Sovrascrivi i log (e svuota le righe rimanenti)
            System.out.println("--- LOGS ---");
            for (String log : logs) {
                System.out.print("\033[2K"); // cancella riga corrente
                System.out.println("- " + log);
            }
        }finally {
            terminalLock.unlock();
        }
    }


    public void serverUnreachable() {
        input.stopInputReader();
    }


    class InputReader implements Runnable{
        private final Scanner stdin = new Scanner(System.in);
        private boolean stdinClosed = false;

        @Override
        public void run() {
            while (!stdinClosed) {
                try {
                    String line = stdin.nextLine();
                    if(stdinClosed)
                        break;

                    handleInput(line);
                } catch (IllegalStateException e) {
                    break;
                }
            }
        }

        private void handleInput(String input) {
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter(" ");

            try {
                String command = scanner.next();
                TuiParser parser = new TuiParser();
                parser.printCommandFormat(command);

                Scanner stdinScanner = new Scanner(System.in);
                String argsLine;
                TuiParser.ParsedArgs args;

                switch (command) {
                    case "setNickname" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.setNickname(args.string());
                    }
                    case "createGame" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.createGame(args.singleInt(), args.intList().getFirst(), PlayerColor.valueOf(args.string()));
                    }
                    case "joinGame" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.joinGame(args.singleInt(), PlayerColor.valueOf(args.string()));
                    }
                    case "chooseTile" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.chooseTile(args.singleInt());
                    }
                    case "chooseTileFromBuffer" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.chooseTileFromBuffer(args.singleInt());
                    }
                    case "closeFaceUp" -> server.closeFaceUpTiles();
                    case "drawFaceDown" -> server.drawFaceDown();
                    case "endBuilding" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.endBuilding(args.singleInt());
                    }
                    case "pickPile" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.pickPile(args.singleInt());
                    }
                    case "place" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.place(args.coord1().getX(), args.coord1().getY());
                    }
                    case "placeInBuffer" -> server.placeInBuffer();
                    case "returnPile" -> server.returnPile();
                    case "returnTile" -> server.returnTile();
                    case "showFaceUp" -> server.showFaceUp();
                    case "startTimer" -> server.startTimer();
                    case "chooseBattery" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.chooseBattery(args.coord1().getX(), args.coord1().getY());
                    }
                    case "choosePropulsor" -> {
                        argsLine = stdinScanner.nextLine();
                        if(argsLine.isEmpty()) {
                            server.choosePropulsor(null, null);
                        } else {
                            argsLine = stdinScanner.nextLine();
                            args = parser.parseArguments(command, argsLine);
                            List<Coordinates> coords = args.coordGroups().isEmpty() ? List.of() : args.coordGroups().getFirst();
                            server.choosePropulsor(coords, args.intList());
                        }
                    }
                    case "compareCrew" -> server.compareCrew();
                    case "compareFirePower" -> {
                        argsLine = stdinScanner.nextLine();
                        if(argsLine.isEmpty()) {
                            server.compareFirePower(null, null);
                        } else {
                            args = parser.parseArguments(command, argsLine);
                            List<List<Coordinates>> groups = args.coordGroups();
                            if (groups.size() >= 2) {
                                server.compareFirePower(groups.get(0), groups.get(1));
                            } else {
                                System.out.println("Error: two -c groups are required.");
                            }
                        }
                    }
                    case "epidemic" -> server.spreadEpidemic();
                    case "getNextAdventureCard" -> server.getNextAdventureCard();
                    case "getRewards" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.getRewards(args.accept());
                    }
                    case "handleBoxes" -> {
                        argsLine = stdinScanner.nextLine();
                        if(argsLine.isEmpty()) {
                            server.handleBoxes(null, null);
                        } else {
                            argsLine = stdinScanner.nextLine();
                            args = parser.parseArguments(command, argsLine);
                            server.handleBoxes(args.coordGroups().getFirst(), args.boxMapList());
                        }
                    }
                    case "planets" -> {
                        argsLine = stdinScanner.nextLine();
                        if(argsLine.isEmpty()) {
                            server.landToPlanet(null, null, null);
                        } else {
                            argsLine = stdinScanner.nextLine();
                            args = parser.parseArguments(command, argsLine);
                            server.landToPlanet(args.planetIdx(), args.coordGroups().getFirst(), args.boxMapList());
                        }
                    }
                    case "removeCrew" -> {
                        argsLine = stdinScanner.nextLine();
                        if(argsLine.isEmpty()) {
                            server.removeCrew(null, null);
                        } else {
                            argsLine = stdinScanner.nextLine();
                            args = parser.parseArguments(command, argsLine);
                            server.removeCrew(args.coordGroups().getFirst(), args.intList());
                        }
                    }
                    case "retire" -> server.retire();
                    case "rollDice" -> server.rollDice();
                    case "stardust" -> server.starDust();
                    case "fixShip" -> {
                        argsLine = stdinScanner.nextLine();
                        args = parser.parseArguments(command, argsLine);
                        server.fixShip(args.coordGroups().getFirst());
                    }
                    case "loadCrew" -> {
                        argsLine = stdinScanner.nextLine();
                        if(argsLine.isEmpty()) {
                            server.loadCrew(null, null);
                        } else {
                            args = parser.parseArguments(command, argsLine);
                            server.loadCrew(args.coord1(), args.coord2());
                        }
                    }
                    case "helper" -> helper();
                    default -> throw new NoSuchElementException();
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Try 'helper' to get the list of commands.");
            }
        }


        private void helper() {
            System.out.println("Commands list:\n");

            System.out.println("LOBBY COMMANDS:");
            System.out.println("\tsetNickname               -choose an unique nickname");
            System.out.println("\tcreateGame                -create a new game specifying the number of players, the type of game and your color");
            System.out.println("\tjoinGame                  -join an existing game\n");

            System.out.println("BUILDING COMMANDS:");
            System.out.println("\tchooseTile                -chose a tile from face up tiles specifying it's index");
            System.out.println("\tchooseTileFromBuffer      -choose a tile from buffer tiles specifying it's index");
            System.out.println("\tshowFaceUp                -show all face up tiles (no parameters)");
            System.out.println("\tcloseFaceUp               -close face up tiles (no parameters)");
            System.out.println("\tdrawFaceDown              -draw a random face down tile (no parameters)");
            System.out.println("\tplace                     -place the tile you are currently holding specifying the coordinates");
            System.out.println("\tendBuilding               -finish the building phase and decide the position you want to start in");
            System.out.println("\tpickPile                  -show the cards in one of the piles specifying the pile index");
            System.out.println("\tplaceInBuffer             -place the tile you are currently holding in the buffer (no parameters)");
            System.out.println("\treturnPile                -return the pile you are currently holding (no parameters)");
            System.out.println("\treturnTile                -return the tile you are currently holding (no parameters)");
            System.out.println("\tstartTimer                -start the timer (no parameters)");
            System.out.println("\tfixShip                   -specify the coordinates of the tiles you want to remove to make the ship legal\n");

            System.out.println("ADVENTURE CARDS COMMANDS:");
            System.out.println("\tchooseBattery             -select the battery you want to use specifying it's coordinates");
            System.out.println("\tchoosePropulsor           -select the propulsors you want to use specifying their coordinates");
            System.out.println("\tcompareCrew               -compare players crew sizes (no parameters)");
            System.out.println("\tcompareFirePower          -compare players fire powers, you can activate double cannons");
            System.out.println("\tepidemic                  -spreads epidemic (no parameters)");
            System.out.println("\tgetNextAdventureCard      -draw a new adventure card (no parameters)");
            System.out.println("\tgetRewards                -choose if you want to get rewards");
            System.out.println("\thandleBoxes               -select the new boxes formation on the tiles in which you want to modify it");
            System.out.println("\tplanets                   -select the planet you want to land and specify the new boxes formation on the tiles in which you want to modify it");
            System.out.println("\tremoveCrew                -choose the crew you want to remove specifying it's coordinates");
            System.out.println("\tretire                    -retire from the game (no parameters)");
            System.out.println("\trollDice                  -roll dices (no parameters)");
            System.out.println("\tstardust                  -activate stardust effect (no parameters)");
            System.out.println("\tfixShip                   -specify the coordinates of the tiles you want to remove to make the ship legal");
            System.out.println("\tloadCrew                  -load aliens specifying the coordinates of the tiles where you want to put them\n");

            System.out.println("====================================================================================");

            System.out.println("Once you select a command it will be explained in detail the format of the arguments");

            System.out.println("====================================================================================");
        }

        private void stopInputReader(){
            if(!stdinClosed)
                System.out.println("Connection to server unavailable, exiting.");
            stdinClosed = true;
        }
    }
}
