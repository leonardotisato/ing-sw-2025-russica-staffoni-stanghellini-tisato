package it.polimi.ingsw.cg04.client.view.tui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import it.polimi.ingsw.cg04.client.view.View;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.beans.PropertyChangeEvent;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TUI extends View {
    private final InputReader input;
    private int columnOffset = 0;

    public TUI(ServerHandler server, ClientModel clientModel) {
        super(server, clientModel);


        String asciiArt = """
                 $$$$$$\\            $$\\                                     $$$$$$$$\\                            $$\\                          \s
                $$  __$$\\           $$ |                                    \\__$$  __|                           $$ |                         \s
                $$ /  \\__| $$$$$$\\  $$ | $$$$$$\\  $$\\   $$\\ $$\\   $$\\          $$ | $$$$$$\\  $$\\   $$\\  $$$$$$$\\ $$ |  $$\\  $$$$$$\\   $$$$$$\\ \s
                $$ |$$$$\\  \\____$$\\ $$ | \\____$$\\ \\$$\\ $$  |$$ |  $$ |         $$ |$$  __$$\\ $$ |  $$ |$$  _____|$$ | $$  |$$  __$$\\ $$  __$$\\\s
                $$ |\\_$$ | $$$$$$$ |$$ | $$$$$$$ | \\$$$$  / $$ |  $$ |         $$ |$$ |  \\__|$$ |  $$ |$$ /      $$$$$$  / $$$$$$$$ |$$ |  \\__|
                $$ |  $$ |$$  __$$ |$$ |$$  __$$ | $$  $$<  $$ |  $$ |         $$ |$$ |      $$ |  $$ |$$ |      $$  _$$<  $$   ____|$$ |     \s
                \\$$$$$$  |\\$$$$$$$ |$$ |\\$$$$$$$ |$$  /\\$$\\ \\$$$$$$$ |         $$ |$$ |      \\$$$$$$  |\\$$$$$$$\\ $$ | \\$$\\ \\$$$$$$$\\ $$ |     \s
                 \\______/  \\_______|\\__| \\_______|\\__/  \\__| \\____$$ |         \\__|\\__|       \\______/  \\_______|\\__|  \\__| \\_______|\\__|     \s
                                                            $$\\   $$ |                                                                        \s
                                                            \\$$$$$$  |                                                                        \s
                                                             \\______/
                
                """;


        System.out.println("Welcome to:\n");
        System.out.println(asciiArt);
        System.out.println("Type 'helper' to receive the list of commands.");
        System.out.println("Once typed the command you decided to use, an explanation of it's accepted input format will be displayed.");
        System.out.println("Note that not every command requires parameters.\n\n");

        input = new InputReader();
        Thread t = new Thread(input);
        t.setName("Stdin InputReader");
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "GAME_UPDATE" -> {
                // System.out.println("Game updated");
                updateGame((Game) evt.getNewValue(), (String) evt.getOldValue());
                columnOffset = ((Game) evt.getNewValue()).getLevel() == 2 ? 4 : 5;
            }
            case "LOGS_UPDATE" -> {
                // System.out.println("Logs updated");
                updateLogs((List<String>) evt.getNewValue());
            }
            case "JOINABLE_GAMES" -> {
                updateJoinableGames((List<Game.GameInfo>) evt.getNewValue());
            }
        }
    }

    public void updateGame(Game toPrint, String nickname) {
        try {
            String rendered = toPrint.render(nickname);
//            Terminal t = input.getTerminal();

            LineReader reader = input.getReader();
            reader.callWidget(LineReader.CLEAR_SCREEN);

//            t.writer().print("\033[H\033[2J");  // Clear screen
//            t.writer().flush();
//            t.puts(InfoCmp.Capability.clear_screen);
            reader.printAbove(rendered);


        } catch (NullPointerException e) {
            System.out.println("Game not found");
            e.printStackTrace();

        }
    }


    public void updateLogs(List<String> logs) {
        LineReader reader = input.getReader();

        reader.printAbove("──────── LOGS ────────");

        for (int i = 0; i < logs.size(); i++) {
            reader.printAbove("- " + logs.get(i));
        }
    }

    public void updateJoinableGames(List<Game.GameInfo> joinableGames) {
        LineReader reader = input.getReader();
        StringBuilder sb = new StringBuilder();
        if (joinableGames.isEmpty()) {
            sb.append("\n\nUpdate in joinable games!:\n\n").append("No joinable games found. Create a new one with createGame");
            reader.printAbove(sb.toString());
            return;
        }
        List<List<String>> joinableGamesList = new ArrayList<>();
        for (Game.GameInfo gameInfo : joinableGames) {
            joinableGamesList.add(TuiDrawer.toLines(gameInfo.gameInfoToColumn()));
        }
        int columns = joinableGamesList.size();
        int rows = joinableGamesList.stream().mapToInt(List::size).max().getAsInt();
        int[] colWidth = new int[columns];

        for (int c = 0; c < columns; c++) {
            int max = 0;
            for (int r = 0; r < joinableGamesList.get(c).size(); r++)
                max = Math.max(max, joinableGamesList.get(c).get(r).length());
            colWidth[c] = max;
        }


        sb.append("\n\nUpdate in joinable games!:\n\n");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (r < joinableGamesList.get(c).size()) sb.append(String.format("%-" + colWidth[c] + "s", joinableGamesList.get(c).get(r)));
                else sb.append(String.format("%-" + colWidth[c] + "s", ""));
                if (c < columns - 1) sb.append("  ".repeat(3));
            }
            sb.append('\n');
        }
        reader.printAbove(sb.toString());
    }


    public void serverUnreachable() {
        input.stopInputReader();
    }




    class InputReader implements Runnable {
        private LineReader reader;
        private Terminal terminal;
        private boolean stdinClosed = false;

        public InputReader() {
            try {
                terminal = TerminalBuilder.builder().system(true).jansi(true).build();
                reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .completer(getCommandsCompleter())
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize terminal", e);
            }
        }

        @Override
        public void run() {
            while (!stdinClosed) {
                try {
                    String line = reader.readLine("> ");
                    handleInput(line, reader);
                } catch (UserInterruptException | EndOfFileException e) {
                    stdinClosed = true;
                }
            }
        }

        private void handleInput(String input, LineReader reader) {
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter(" ");

            try {
                String command = scanner.next();
                TuiParser parser = new TuiParser(columnOffset);
                parser.printCommandFormat(command);

                //Scanner stdinScanner = new Scanner(System.in);

                String argsLine;
                TuiParser.ParsedArgs args;

                switch (command) {
                    case "setNickname", "setNick" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.setNickname(args.string());
                    }
                    case "createGame", "create" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.createGame(args.singleInt(), args.intList().getFirst(), PlayerColor.valueOf(args.string()));
                    }
                    case "joinGame", "join" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.joinGame(args.singleInt(), PlayerColor.valueOf(args.string()));
                    }
                    case "chooseTile", "choose" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.chooseTile(args.singleInt());
                    }
                    case "chooseTileFromBuffer" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.chooseTileFromBuffer(args.singleInt());
                    }
                    case "closeFaceUp" -> server.closeFaceUpTiles();
                    case "drawFaceDown", "draw", "d" -> server.drawFaceDown();
                    case "endBuilding" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.endBuilding(args.singleInt());
                    }
                    case "pickPile" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.pickPile(args.singleInt());
                    }
                    case "place", "p" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.place(args.coord1().getX(), args.coord1().getY());
                    }
                    case "rotate" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.rotate(args.string());
                    }
                    case "placeInBuffer" -> server.placeInBuffer();
                    case "returnPile" -> server.returnPile();
                    case "returnTile" -> server.returnTile();
                    case "showFaceUp" -> server.showFaceUp();
                    case "startTimer" -> server.startTimer();
                    case "stopBuilding" -> server.stopBuilding();
                    case "chooseBattery" -> {
                        argsLine = reader.readLine(">> ");
                        if (argsLine.isEmpty()) {
                            server.chooseBattery(-1, -1);
                        }
                        args = parser.parseArguments(command, argsLine);
                        server.chooseBattery(args.coord1().getX(), args.coord1().getY());
                    }
                    case "choosePropulsor" -> {
                        argsLine = reader.readLine(">> ");
                        if (argsLine.isEmpty()) {
                            server.choosePropulsor(new ArrayList<>(), new ArrayList<>());
                        } else {
                            args = parser.parseArguments(command, argsLine);
                            List<Coordinates> coords = args.coordGroups().isEmpty() ? List.of() : args.coordGroups().getFirst();
                            server.choosePropulsor(coords, args.intList());
                        }
                    }
                    case "compareCrew" -> server.compareCrew();
                    case "compareFirePower" -> {
                        argsLine = reader.readLine(">> ");
                        if (argsLine.isEmpty()) {
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
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.getRewards(args.accept());
                    }
                    case "handleBoxes" -> {
                        argsLine = reader.readLine(">> ");
                        if (argsLine.isEmpty()) {
                            server.handleBoxes(null, null);
                        } else {
                            args = parser.parseArguments(command, argsLine);
                            clientModel.getGame().composeNewBoxesMap(server.getNickname(), args.coordGroups().getFirst(), args.boxMapList());
                            server.handleBoxes(args.coordGroups().getFirst(), args.boxMapList());
                        }
                    }
                    case "planets" -> {
                        argsLine = reader.readLine(">> ");
                        if (argsLine.isEmpty()) {
                            server.landToPlanet(null, null, null);
                        } else {
                            args = parser.parseArguments(command, argsLine);
                            clientModel.getGame().composeNewBoxesMap(server.getNickname(), args.coordGroups().getFirst(), args.boxMapList());
                            server.landToPlanet(args.planetIdx(), args.coordGroups().getFirst(), args.boxMapList());
                        }
                    }
                    case "removeCrew" -> {
                        argsLine = reader.readLine(">> ");
                        if (argsLine.isEmpty()) {
                            server.removeCrew(null, null);
                        } else {
                            args = parser.parseArguments(command, argsLine);
                            server.removeCrew(args.coordGroups().getFirst(), args.intList());
                        }
                    }
                    case "retire" -> server.retire();
                    case "rollDice" -> server.rollDice();
                    case "stardust" -> server.starDust();
                    case "fixShip" -> {
                        argsLine = reader.readLine(">> ");
                        args = parser.parseArguments(command, argsLine);
                        server.fixShip(args.coordGroups().getFirst());
                    }
                    case "loadCrew" -> {
                        argsLine = reader.readLine(">> ");
                        if (argsLine.isEmpty()) {
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
            System.out.println("\trotate                    -rotate the tile you are currently holding specifying the direction");
            System.out.println("\tendBuilding               -finish the building phase and decide the position you want to start in");
            System.out.println("\tpickPile                  -show the cards in one of the piles specifying the pile index");
            System.out.println("\tplaceInBuffer             -place the tile you are currently holding in the buffer (no parameters)");
            System.out.println("\treturnPile                -return the pile you are currently holding (no parameters)");
            System.out.println("\treturnTile                -return the tile you are currently holding (no parameters)");
            System.out.println("\tstartTimer                -start the timer (no parameters)");
            System.out.println("\tstopBuilding              -stop the building phase for everyone, can only be called after timer expired (no parameters)");
            System.out.println("\tfixShip                   -specify the coordinates of the tiles you want to remove to make the ship legal\n");

            System.out.println("ADVENTURE CARDS COMMANDS:");
            System.out.println("\tloadCrew                  -load aliens specifying the coordinates of the tiles where you want to put them\n");
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
            System.out.println("\trollDice                  -roll dices (no parameters)");
            System.out.println("\tstardust                  -activate stardust effect (no parameters)");
            System.out.println("\tfixShip                   -specify the coordinates of the tiles you want to remove to make the ship legal");

            System.out.println("\tretire                    -retire from the game (no parameters)\n");
            System.out.println("====================================================================================");

            System.out.println("Once you select a command it will be explained in detail the format of the arguments");

            System.out.println("====================================================================================");
        }

        private void stopInputReader() {
            stdinClosed = true;
            try {
                terminal.close();
            } catch (Exception ignored) {
            }
        }

        private Completer getCommandsCompleter() {
            // Legge il file JSON
            InputStream is = getClass().getClassLoader().getResourceAsStream("jsons/commands.json");
            assert is != null;
            Reader reader = new InputStreamReader(is);

            // Imposta il tipo: List<String>
            Type listType = new TypeToken<List<String>>() {
            }.getType();

            // Crea Gson e deserializza
            Gson gson = new Gson();
            List<String> commands = gson.fromJson(reader, listType);
            Completer completer = new StringsCompleter(commands);
            return completer;
        }

        public Terminal getTerminal() {
            return terminal;
        }

        public LineReader getReader() {
            return reader;
        }
    }
}
