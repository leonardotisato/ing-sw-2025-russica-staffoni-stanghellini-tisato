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

public class TUI extends View {
    private final InputReader input;

    public TUI(ServerHandler server, ClientModel clientModel) {
        super(server, clientModel);
        System.out.println("Welcome to Galaxy Trucker!");
        System.out.println("Type helper to receive the list of commands.");

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

            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println(toPrint.render(nickname));
        } catch (NullPointerException ignored) {
            System.out.println("Game not found");
        }
    }


    public void updateLogs() {
        List<String> logs = clientModel.getLogs();

        int logLines = 10;

        // 1. Risali di 'logLines' + intestazione
        System.out.print("\033[" + (logLines + 1) + "A"); // risale di (logLines + 1) righe
        System.out.flush();

        // 2. Sovrascrivi i log (e svuota le righe rimanenti)
        System.out.println("--- LOGS ---");
        for (int i = 0; i < logLines; i++) {
            if (i < logs.size()) {
                System.out.print("\033[2K"); // cancella riga corrente
                System.out.println("- " + logs.get(i));
            } else {
                System.out.print("\033[2K"); // cancella riga vuota
                System.out.println();       // riga vuota per riempire
            }
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
                String argsLine = stdinScanner.nextLine();
                TuiParser.ParsedArgs args = parser.parseArguments(command, argsLine);

                switch (command) {
                    case "setNickname" -> server.setNickname(args.string());
                    case "createGame" -> server.createGame(args.singleInt(), args.intList().getFirst(), PlayerColor.valueOf(args.string()));
                    case "joinGame" -> server.joinGame(args.singleInt(), PlayerColor.valueOf(args.string()));
                    case "chooseTile" -> server.chooseTile(args.singleInt());
                    case "chooseTileFromBuffer" -> server.chooseTileFromBuffer(args.singleInt());
                    case "closeFaceUp" -> server.closeFaceUpTiles();
                    case "drawFaceDown" -> server.drawFaceDown();
                    case "endBuilding" -> server.endBuilding(args.singleInt());
                    case "pickPile" -> server.pickPile(args.singleInt());
                    case "place" -> server.place(args.coord1().getX(), args.coord1().getY());
                    case "placeInBuffer" -> server.placeInBuffer();
                    case "returnPile" -> server.returnPile();
                    case "returnTile" -> server.returnTile();
                    case "showFaceUp" -> server.showFaceUp();
                    case "startTimer" -> server.startTimer();
                    case "chooseBattery" -> server.chooseBattery(args.coord1().getX(), args.coord1().getY());
                    case "choosePropulsor" -> {
                        List<Coordinates> coords = args.coordGroups().isEmpty() ? List.of() : args.coordGroups().getFirst();
                        server.choosePropulsor(coords, args.intList());
                    }
                    case "compareCrew" -> server.compareCrew();
                    case "compareFirePower" -> {
                        List<List<Coordinates>> groups = args.coordGroups();
                        if (groups.size() >= 2) {
                            server.compareFirePower(groups.get(0), groups.get(1));
                        } else {
                            System.out.println("Error: two -c groups are required.");
                        }
                    }
                    case "epidemic" -> server.spreadEpidemic();
                    case "getNextAdventureCard" -> server.getNextAdventureCard();
                    case "getRewards" -> server.getRewards(args.accept());
                    case "handleBoxes" -> server.handleBoxes(args.coordGroups().getFirst(), args.boxMapList());
                    case "planets" -> server.landToPlanet(args.planetIdx(), args.coordGroups().getFirst(), args.boxMapList());
                    case "removeCrew" -> server.removeCrew(args.coordGroups().getFirst(), args.intList());
                    case "retire" -> server.retire();
                    case "rollDice" -> server.rollDice();
                    case "stardust" -> server.starDust();
                    case "fixShip" -> server.fixShip(args.coordGroups().getFirst());
                    case "loadCrew" -> server.loadCrew(args.coord1(), args.coord2());
                    case "helper" -> helper();
                    default -> throw new NoSuchElementException();
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Try -h for help.");
            }
        }


        private void helper() {
            System.out.println("Commands list:\n");

            System.out.println("LOBBY COMMANDS:");
            System.out.println("\tset_nickname              -choose an unique nickname");
            System.out.println("\tcreate_game               -create a new game specifying the number of players, the type of game and your color");
            System.out.println("\tjoin_game                 -join an existing game\n");

            System.out.println("BUILDING COMMANDS:");
            System.out.println("\tchoose_tile               -chose a tile from face up tiles specifying it's index");
            System.out.println("\tchoose_tile_from_buffe    -choose a tile from buffer tiles specifying it's index");
            System.out.println("\tshow_faceup_tiles         -show all face up tiles");
            System.out.println("\tclose_faceup_tiles        -close face up tiles");
            System.out.println("\tdraw_facedown_tile        -draw a random face down tile");
            System.out.println("\tplace_tile                -place the tile you are currently holding specifying the coordinates");
            System.out.println("\tend_building              -finish the building phase and decide the position you want to start in");
            System.out.println("\tpick_pile                 -show the cards in one of the piles specifying the pile index");
            System.out.println("\tplace_in_buffer           -place the tile you are currently holding in the buffer");
            System.out.println("\treturn_pile               -return the pile you are currently holding");
            System.out.println("\treturn_tile               -return the tile you are currently holding");
            System.out.println("\tstart_timer               -start the timer");
            System.out.println("\tfix_ship                  -specify the coordinates of the tiles you want to remove to make the ship legal\n");

            System.out.println("ADVENTURE CARDS COMMANDS:");
            System.out.println("\tchoose_battery            -select the battery you want to use specifying it's coordinates");
            System.out.println("\tchoose_propulsor          -select the propulsors you want to use specifying their coordinates");
            System.out.println("\tget_next_adventure_card   -draw a new adventure card");
            System.out.println("\thandle_boxes              -select the new boxes formation on the tiles in which you want to modify it");
            System.out.println("\tremove_crew               -choose the crew you want to remove specifying it's coordinates");
            System.out.println("\tretire                    -retire from the game");
            System.out.println("\troll_dice                 -roll dices");
            System.out.println("\tfix_ship                  -specify the coordinates of the tiles you want to remove to make the ship legal");
            System.out.println("\tload_crew                 -load aliens specifying the coordinates of the tiles where you want to put them\n");

            System.out.println("====================================================================================");

            System.out.println("once you select a command it will be explained in detail the format of the arguments");

            System.out.println("====================================================================================");
        }

        private void stopInputReader(){
            if(!stdinClosed)
                System.out.println("Connection to server unavailable, exiting.");
            stdinClosed = true;
        }
    }
}
