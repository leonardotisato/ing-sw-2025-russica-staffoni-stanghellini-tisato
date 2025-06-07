package it.polimi.ingsw.cg04.client.view.tui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.*;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.GameStates.EndGameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import it.polimi.ingsw.cg04.client.view.View;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.buildRightPanel;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public class TUI extends View {
    private final InputReader input;
    String rendered;

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
    public void updateGame(Game toPrint, String nickname) {
        try {

            this.nickname = nickname;

            if (isViewingShips) {
                toPrint.renderShips();
            } else {
                // System.out.println("updating rendered");
                toPrint.getGameState().updateView(this, toPrint);
            }

            Terminal terminal = input.getTerminal();
            terminal.writer().print("\033[H\033[2J");
            terminal.writer().flush();

            terminal.writer().println(rendered);
            terminal.flush();

        } catch (NullPointerException | IOException e) {
            System.out.println("Game not found");
            e.printStackTrace();
        }
    }

    @Override
    public void updateLogs(List<String> logs) {
        LineReader reader = input.getReader();

        reader.printAbove("──────── LOGS ────────");

        for (String log : logs) {
            reader.printAbove("- " + log);
        }
    }

    @Override
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
                if (r < joinableGamesList.get(c).size())
                    sb.append(String.format("%-" + colWidth[c] + "s", joinableGamesList.get(c).get(r)));
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
        private final LineReader reader;
        private final Terminal terminal;
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
                        if (clientModel.getGame().getLevel() == 1) {
                            server.endBuilding(1);
                        } else {
                            argsLine = reader.readLine(">> ");
                            args = parser.parseArguments(command, argsLine);
                            server.endBuilding(args.singleInt());
                        }
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
                    case "viewShips" -> renderShips();
                    case "home" -> renderHome();
                    case "helper" -> helper();
                    default -> throw new NoSuchElementException();
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Try 'helper' to get the list of commands.");
            }
        }

        private void renderShips() {
            isViewingShips = true;
            System.out.println("Ships:");
            System.out.println(clientModel.getGame().renderShips());
        }

        private void renderHome() {
            System.out.println("Home");
            isViewingShips = false;
            updateGame(clientModel.getGame(), clientModel.getNickname());
            updateLogs(clientModel.getLogs());
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

            System.out.println("\tviewShips                 -have a look at other players' ship (no parameters)");
            System.out.println("\thome                      -reset view to main view of your ship (no parameters)");
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
            return new StringsCompleter(commands);
        }

        public Terminal getTerminal() {
            return terminal;
        }

        public LineReader getReader() {
            return reader;
        }
    }


    // added for testing purposes
    public String getRendered() {
        return rendered;
    }

    // render lobbyState
    @Override
    public void renderLobbyState(Game toDisplay) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        for (Player p : toDisplay.getPlayers()) {
            stringBuilder.append(p.getName()).append("\n");
        }
        stringBuilder.append("waiting for ").append(toDisplay.getMaxPlayers() - toDisplay.getPlayers().size()).append(" players to start the game\n");

        rendered = stringBuilder.toString();
    }

    // render BuildState

    @Override
    public void renderBuildState(Game toDisplay) {

        BuildState currState = (BuildState) toDisplay.getGameState();

        Map<String, BuildPlayerState> playerState = currState.getPlayerState();
        Map<String, Integer> isLookingPile = currState.getIsLookingPile();

        StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append(TuiDrawer.renderPlayersByColumn(toDisplay.getPlayers()));

        if (playerState.get(nickname) == BuildPlayerState.FIXING) {
            stringBuilder.append("Your ship:").append("\n").append("\n");
            stringBuilder.append(toDisplay.getPlayer(nickname).getShip().draw()).append("\n").append("\n");
            stringBuilder.append("Your ship is not legal, fix it by removing tiles until you can properly fly!");
        } else {
            if (playerState.get(nickname) == BuildPlayerState.SHOWING_PILE) {
                stringBuilder.append(renderKFigures(5, isLookingPile.get(nickname) - 1, "piles", toDisplay)).append("\n").append("\n");
            } else {
                stringBuilder.append(renderPilesBackside(29, 11, isLookingPile)).append("\n").append("\n");
            }
            stringBuilder.append("Your ship:").append("\n").append("\n");
            stringBuilder.append(toDisplay.getPlayer(nickname).getShip().drawWithBuffer()).append("\n").append("\n");
            if (playerState.get(nickname) == BuildPlayerState.BUILDING) {
                stringBuilder.append(toDisplay.getPlayer(nickname).getHeldTile() != null ? ("Held tile: \n" + toDisplay.getPlayer(nickname).getHeldTile().draw()) : "Pick a tile!").append("\n").append("\n");
            }
            if (playerState.get(nickname) == BuildPlayerState.READY) {
                stringBuilder.append("You're done building the ship! Wait for the other players to finish the building").append("\n");
            }
            if (playerState.get(nickname) != BuildPlayerState.SHOWING_FACE_UP) {
                stringBuilder.append("Face up tiles: send 'showFaceUp' to show more tiles!").append("\n").append("\n");
                stringBuilder.append(toDisplay.getFaceUpTiles().isEmpty() ? "No face up tiles at the moment" : renderKFigures(10, null, "tiles", toDisplay)).append("\n").append("\n");
            } else {
                stringBuilder.append(renderKFigures(toDisplay.getFaceUpTiles().size(), null, "tiles", toDisplay)).append("\n");
            }
        }

        rendered = stringBuilder.toString();
    }

    public String renderPilesBackside(int width, int height, Map<String, Integer> isLookingPile) {


        List<List<String>> pileLines = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<String> singlePile = new ArrayList<>();
            singlePile.add(TuiDrawer.drawTopBoundary(width));

            int paddingLines = height - 3;
            for (int j = 0; j < paddingLines / 2; j++) {
                singlePile.add(TuiDrawer.drawEmptyRow(width));
            }

            // Riga con ID e stato
            singlePile.add(TuiDrawer.drawCenteredRow("#pile: " + (i + 1), width));
            singlePile.add(TuiDrawer.drawCenteredRow(isLookingPile.containsValue(i + 1) ? "Held" : "Free", width));

            for (int j = 0; j < paddingLines - paddingLines / 2; j++) {
                singlePile.add(TuiDrawer.drawEmptyRow(width));
            }

            singlePile.add(TuiDrawer.drawBottomBoundary(width));
            pileLines.add(singlePile);
        }

        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < height + 1; row++) {
            for (int p = 0; p < pileLines.size(); p++) {
                sb.append(pileLines.get(p).get(row));
                if (p < pileLines.size() - 1) {
                    sb.append("  "); // spazio tra pile
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public String renderKFigures(int k, Integer pileId, String typeFigure, Game toDisplay) {
        List<List<String>> tileLines = new ArrayList<>();
        List<Integer> figures = typeFigure.equals("tiles") ? toDisplay.getFaceUpTiles() : toDisplay.getPreFlightPiles().get(pileId);

        for (int i = 0; i < k && i < figures.size(); i++) {
            String[] lines = typeFigure.equals("tiles") ? toDisplay.getTileById(figures.get(i)).draw().split("\n") :
                    toDisplay.getCardById(figures.get(i)).draw().split("\n");
            tileLines.add(Arrays.asList(lines));
        }

        if (tileLines.isEmpty()) return "";

        int tileHeight = tileLines.getFirst().size();
        int tilesPerRow = 10;
        int totalTiles = tileLines.size();
        int numRowsOfTiles = (int) Math.ceil((double) totalTiles / tilesPerRow);

        StringBuilder sb = new StringBuilder();

        for (int rowBlock = 0; rowBlock < numRowsOfTiles; rowBlock++) {
            int start = rowBlock * tilesPerRow;
            int end = Math.min(start + tilesPerRow, totalTiles);

            // stampa le righe delle tile
            for (int line = 0; line < tileHeight; line++) {
                for (int i = start; i < end; i++) {
                    sb.append(tileLines.get(i).get(line));
                    if (i < end - 1) sb.append("  ");
                }
                sb.append('\n');
            }

            // stampa la riga con gli indici
            if (typeFigure.equals("tiles")) {
                for (int i = start; i < end; i++) {
                    int tileWidth = 14; // larghezza della tile
                    String label = "[" + i + "]";
                    int padLeft = (tileWidth - label.length()) / 2;
                    int padRight = tileWidth - label.length() - padLeft;
                    sb.append(" ".repeat(Math.max(0, padLeft)))
                            .append(label)
                            .append(" ".repeat(Math.max(0, padLeft)));
                    if (i < end - 1) sb.append("   ");
                }
            }
            sb.append("\n\n");
        }

        return sb.toString();
    }

    // render LoadCrewState
    @Override
    public void renderLoadCrewState(Game toDisplay) {

        AdventureCardState currState = (AdventureCardState) toDisplay.getGameState();
        Integer currPlayerIdx = currState.getCurrPlayerIdx();

        Player player = toDisplay.getPlayer(nickname);
        StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append("State: Load crew").append("\n").append("\n");
        for (Player p : toDisplay.getPlayers()) {
            stringBuilder.append("player: ").append(p.getName()).append("\n");
            stringBuilder.append("Color: ").append(p.getColor()).append("\n");
            stringBuilder.append("Position: ").append(p.getRanking()).append("\n").append("\n");
        }
        stringBuilder.append("Your ship:").append("\n").append("\n");
        stringBuilder.append(toDisplay.getPlayer(nickname).getShip().draw()).append("\n").append("\n");
        if (currPlayerIdx == (player.getRanking() - 1)) {
            stringBuilder.append("It's time to load the crew! Type loadCrew and load pink or brown aliens on your ship, if you want.").append("\n");
        } else {
            stringBuilder.append("It's almost time to load the crew! Wait your turn to load pink or brown aliens on your ship.").append("\n");
        }

        rendered = stringBuilder.toString();
    }

    // render FlightState

    @Override
    public void renderFlightState(Game toDisplay) {
        List<String> leftLines = new ArrayList<>();

        // Informazioni generali
        leftLines.add("State: flightState");
        leftLines.add("Remaining cards to play: " + toDisplay.getAdventureCardsDeck().size());
        leftLines.add("");

        // Info su ogni giocatore
        for (Player p : toDisplay.getSortedPlayers()) {
            leftLines.add("Player: " + p.getName());
            leftLines.add("Credits: " + p.getNumCredits());
            leftLines.add("");
        }

        // Crea blocco per la board
        List<String> rightLines = new ArrayList<>(Arrays.asList(toDisplay.getBoard().draw().split("\n")));

        int maxLines = Math.max(leftLines.size(), rightLines.size());


        TuiDrawer.adjustVerticalAlignment(leftLines, rightLines);

        // Combina riga per riga
        StringBuilder stringBuilder = new StringBuilder("\n");
        for (int i = 0; i < maxLines; i++) {
            stringBuilder.append(String.format("%-40s", leftLines.get(i)))  // Allinea a sinistra (40 spazi)
                    .append("  ")                                      // Spazio tra le colonne
                    .append(rightLines.get(i))
                    .append("\n");
        }
        Player p = toDisplay.getPlayer(nickname);
        stringBuilder.append("\n");
        stringBuilder.append(p.getRanking() == 1 ? "Send 'getNextAdventureCard' to start the next adventure!" : ("wait for " + toDisplay.getPlayer(0).getName() + " to start the next adventure!"));
        stringBuilder.append("\n");


        rendered = stringBuilder.toString();
    }

    // render EndGameState
    @Override
    public void renderEndGameState(Game toDisplay) {

        EndGameState currState = (EndGameState) toDisplay.getGameState();
        List<Player> leaderboard = currState.getLeaderboard();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append(nickname).append("\n\nThe game has ended. \n\n");
        Player p = toDisplay.getPlayer(nickname);
        stringBuilder.append(p.getNumCredits() >= 1 ? "You have one credit or more, so you won!" : "You have no credit, you lost!").append("\n");
        stringBuilder.append("Leaderboard: ").append("\n");
        for (Player p1 : leaderboard) {
            stringBuilder.append(leaderboard.indexOf(p1) + 1).append("- ").append(p1.getName()).append(": ").append(p1.getNumCredits()).append(" credits").append("\n");
        }
        stringBuilder.append("\n");

        rendered = stringBuilder.toString();
    }

    // render AdventureCardState

    public String renderAdventureTemplate(Game toDisplay) {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(TuiDrawer.renderPlayersByColumn(toDisplay.getSortedPlayers()));
        System.out.println("Your ship:");
        stringBuilder.append(toDisplay.getPlayer(nickname).getShip().draw());
        List<String> shipPanel = toLines(stringBuilder.toString());
        List<String> flightBoardPanel = toLines(toDisplay.getBoard().draw());
        List<String> adventureCardPanel = toLines(toDisplay.getCurrentAdventureCard().draw());
        int totalH = shipPanel.size();
        int rightWidth = Stream.concat(flightBoardPanel.stream(), adventureCardPanel.stream())
                .mapToInt(String::length)
                .max()
                .orElse(0);                  // quanto spazio assegni alla colonna destra
        List<String> rightPanel = buildRightPanel(flightBoardPanel, adventureCardPanel, totalH, rightWidth);
        return TuiDrawer.renderTwoColumnLayout(shipPanel, rightPanel, 40);
    }

    @Override
    public void renderAbandonedShipState(Game toDisplay) {
        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));

        AdventureCardState currState = (AdventureCardState) toDisplay.getGameState();
        int currPlayerIdx = currState.getCurrPlayerIdx();

        stringBuilder.append("\n".repeat(3));
        Player p = toDisplay.getPlayer(nickname);
        stringBuilder.append("It's ").append(currPlayerIdx == (p.getRanking() - 1) ? "your " : toDisplay.getPlayer(currPlayerIdx).getName()).append(" turn").append("\n");
        if (currPlayerIdx == (p.getRanking() - 1)) {
            stringBuilder.append("You have ").append(toDisplay.getPlayer(nickname).getShip().getNumCrew()).append(" crew members.").append("\n");
            stringBuilder.append("Type 'removeCrew' to trade crew members for credits.").append("\n");
            stringBuilder.append("Note that you will lose ").append(toDisplay.getCurrentAdventureCard().getDaysLost()).append(" days of flight if you remove any crew members.").append("\n");
        }

        rendered = stringBuilder.toString();
    }

    @Override
    public void renderAbandonedStationState(Game toDisplay) {
        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));

        AdventureCardState currState = (AdventureCardState) toDisplay.getGameState();
        int currPlayerIdx = currState.getCurrPlayerIdx();

        stringBuilder.append("\n".repeat(3));
        Player p = toDisplay.getPlayer(nickname);
        stringBuilder.append("It's ").append(currPlayerIdx == (p.getRanking() - 1) ? "your " : toDisplay.getPlayer(currPlayerIdx).getName()).append(" turn").append("\n");
        if (currPlayerIdx == (p.getRanking() - 1)) {
            stringBuilder.append("You have ").append(toDisplay.getPlayer(nickname).getShip().getNumCrew()).append(" crew members.").append("\n");
            stringBuilder.append("Type 'removeCrew' to trade crew for boxes.").append("\n");
            stringBuilder.append("Note that you will lose ").append(toDisplay.getCurrentAdventureCard().getDaysLost()).append(" days of flight.").append("\n");
        }

        rendered = stringBuilder.toString();
    }

    @Override
    public void renderEpidemicState(Game toDisplay) {

        rendered = renderAdventureTemplate(toDisplay) + "\n".repeat(3) +
                "Send 'epidemic' to spread the epidemic (you need to spread this epidemic to continue the game)." +
                "\n" +
                "You may lose some crew members!" + "\n";
    }

    @Override
    public void renderMeteorsRainState(Game toDisplay) {

        int PROVIDE_BATTERY = 1;
        int CORRECT_SHIP = 2;
        MeteorsRainState currState = (MeteorsRainState) toDisplay.getGameState();
        List<Integer> played = currState.getPlayed();

        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));

        stringBuilder.append("\n".repeat(3));
        stringBuilder.append("Meteor ").append(currState.getCurrMeteorIdx() + 1).append(" approaching").append(currState.isRolled() ? " from " + currState.getDiceResult() : "").append("\n");
        Player p = toDisplay.getPlayer(nickname);
        if (!currState.isRolled()) {
            stringBuilder.append(currState.getCurrPlayerIdx() == (p.getRanking() - 1) ? "Roll the dices!" : "Wait for " + currState.getSortedPlayers().getFirst().getName() + " to roll the dices!").append("\n");
        } else {
            if (played.get(currState.getSortedPlayers().indexOf(p)) == PROVIDE_BATTERY) {
                stringBuilder.append("You're about to be hit by a meteor! Send a chooseBattery to save your ship!");
            } else if (played.get(currState.getSortedPlayers().indexOf(p)) == CORRECT_SHIP) {
                stringBuilder.append("You've been hit by a meteor! You need to FIX YOUR SHIP!");
            } else {
                stringBuilder.append("You're done for this round! Wait for the other players to handle their attacks and fix their ships!");
            }
        }

        rendered = stringBuilder.toString();
    }

    @Override
    public void renderOpenSpaceState(Game toDisplay) {

        AdventureCardState currState = (AdventureCardState) toDisplay.getGameState();
        int currPlayerIdx = currState.getCurrPlayerIdx();

        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));

        stringBuilder.append("\n".repeat(3));
        Player p = toDisplay.getPlayer(nickname);

        stringBuilder.append("It's ").append(currPlayerIdx == (p.getRanking() - 1) ? "your " : toDisplay.getPlayer(currPlayerIdx).getName()).append(" turn").append("\n");
        if (currPlayerIdx == (p.getRanking() - 1)) {
            List<Coordinates> propulsorCoordinates = p.getShip().getTilesMap().get("PropulsorTile");
            int totDoublePropulsor = (int) propulsorCoordinates.stream()
                    .map(coord -> p.getShip().getTile(coord.getX(), coord.getY()))
                    .filter(Tile::isDoublePropulsor)
                    .count();
            stringBuilder.append("You have: ").append("\n");
            stringBuilder.append(p.getShip().getNumBatteries()).append(" batteries").append("\n");
            stringBuilder.append("Base propulsion power of ").append(p.getShip().getBasePropulsionPower()).append("\n");
            stringBuilder.append(totDoublePropulsor).append(" double propulsors").append("\n");
            stringBuilder.append("Send batteries to increase propulsion power").append("\n");
        }

        rendered = stringBuilder.toString();
    }

    @Override
    public void renderPiratesState(Game toDisplay) {

        PiratesState currState = (PiratesState) toDisplay.getGameState();
        int currPlayerIdx = currState.getCurrPlayerIdx();

        final int WAIT = 0;
        final int ACTIVATE_CANNONS = 1;
        final int DECIDE_REWARD = 2;
        final int WAIT_FOR_SHOT = 3;
        final int PROVIDE_BATTERY = 4;
        final int CORRECT_SHIP = 5;
        final int SHOT_DONE = 6;
        final int DONE = 7;

        List<Integer> playerStates = currState.getPlayerStates();
        boolean rolled = currState.isRolled();
        List<Player> sortedPlayers = currState.getSortedPlayers();
        AdventureCard card = currState.getCard();
        int numMeteors = card.getAttacks().size();
        int currMeteorIdx = currState.getCurrMeteorIdx();

        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));

        stringBuilder.append("\n".repeat(3));
        Player p = toDisplay.getPlayer(nickname);
        int playerIdx = p.getRanking() - 1;
        int playerState = playerStates.get(playerIdx);
        if (!rolled && currState.isFirstWaitingForShot(p) && !playerStates.contains(WAIT)) {
            stringBuilder.append("You're the first loser! You must roll the dices").append("\n");
        }
        switch (playerState) {
            case ACTIVATE_CANNONS:
                List<Coordinates> lasersCoordinates = p.getShip().getTilesMap().get("LaserTile");
                int totDoubleLasers = (int) lasersCoordinates.stream()
                        .map(coord -> p.getShip().getTile(coord.getX(), coord.getY()))
                        .filter(Tile::isDoubleLaser)
                        .count();
                stringBuilder.append("Pirates are here! Activate your double cannons and try to defeat them!").append("\n");
                stringBuilder.append("Base fire power of your ship: ").append(p.getShip().getBaseFirePower()).append("\n");
                stringBuilder.append("Number of double cannons: ").append(totDoubleLasers).append("\n");
                break;
            case WAIT:
                stringBuilder.append("Pirates are coming! Wait for ").append(sortedPlayers.get(currPlayerIdx).getName()).append(" to combat them.").append("\n");
                break;
            case DECIDE_REWARD:
                stringBuilder.append("You won! Decide if you want to earn ").append(card.getEarnedCredits()).append(" credits").append("\n");
                stringBuilder.append("Please note that you will lose ").append(card.getDaysLost()).append(" days of flight.").append("\n");
                break;
            case WAIT_FOR_SHOT:
                stringBuilder.append("You lost! Wait for the other players to combat the pirates. You will need to defend your ship from the attacks.").append("\n");
                break;
            case PROVIDE_BATTERY:
                stringBuilder.append("You can defend your ship using a battery. Send the battery to neutralize the attack.").append("\n");
                break;
            case CORRECT_SHIP:
                stringBuilder.append("You've been hit! Fix the ship by removing tiles until it becomes legal.").append("\n");
                break;
            case SHOT_DONE:
                stringBuilder.append("You're done for this ").append(currMeteorIdx == numMeteors ? "card! Wait for the next adventure." : "attack. Wait for the next one.").append("\n");
                break;
            case DONE:
                stringBuilder.append("You're done for this card! Wait for the other players to start the next adventure.").append("\n");
                break;
        }

        rendered = stringBuilder.toString();
    }

    @Override
    public void renderPlanetsState(Game toDisplay) {

        AdventureCard card = toDisplay.getCurrentAdventureCard();
        PlanetsState currState = (PlanetsState) toDisplay.getGameState();
        Integer currPlayerIdx = currState.getCurrPlayerIdx();
        Map<Player, Integer> chosenPlanets = currState.getChosenPlanets();

        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));

        stringBuilder.append("\n".repeat(3));

        for (int i = 0; i < card.getPlanetReward().size(); i++) {
            if (chosenPlanets.containsValue(i)) {
                stringBuilder.append("Planet ").append(i).append(" is already chosen").append("\n");
            } else {
                stringBuilder.append("Planet ").append(i).append(" is free").append("\n");
            }
        }
        Player p = toDisplay.getPlayer(nickname);
        stringBuilder.append("It's ").append(currPlayerIdx == (p.getRanking() - 1) ? "your " : toDisplay.getPlayer(currPlayerIdx).getName()).append(" turn").append("\n");
        if (currPlayerIdx == (p.getRanking() - 1)) {
            stringBuilder.append("Choose a planet (if you want) and handle your new boxes!").append("\n");
            stringBuilder.append("Please note that you will lose ").append(card.getDaysLost()).append(" days of flight.").append("\n");
        }

        rendered = stringBuilder.toString();
    }

    @Override
    public void renderSlaversState(Game toDisplay) {

        final int WAIT = 0;
        final int ACTIVATE_CANNONS = 1;
        final int REMOVE_CREW = 2;
        final int DECIDE_REWARD = 3;
        final int DONE = 4;

        AdventureCard card = toDisplay.getCurrentAdventureCard();
        SlaversState currState = (SlaversState) toDisplay.getGameState();
        Integer currPlayerIdx = currState.getCurrPlayerIdx();
        List<Integer> playerStates = currState.getPlayerStates();
        List<Player> sortedPlayers = currState.getSortedPlayers();

        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));

        stringBuilder.append("\n".repeat(3));
        Player p = toDisplay.getPlayer(nickname);
        int playerIdx = p.getRanking() - 1;
        int playerState = playerStates.get(playerIdx);
        switch (playerState) {
            case ACTIVATE_CANNONS:
                List<Coordinates> lasersCoordinates = p.getShip().getTilesMap().get("LaserTile");
                int totDoubleLasers = (int) lasersCoordinates.stream()
                        .map(coord -> p.getShip().getTile(coord.getX(), coord.getY()))
                        .filter(Tile::isDoubleLaser)
                        .count();
                stringBuilder.append("Slavers are here! Activate your double cannons and try to defeat them!").append("\n");
                stringBuilder.append("Base fire power of your ship: ").append(p.getShip().getBaseFirePower()).append("\n");
                stringBuilder.append("Number of double cannons: ").append(totDoubleLasers).append("\n");
                break;
            case WAIT:
                stringBuilder.append("Slavers are coming! Wait for ").append(sortedPlayers.get(currPlayerIdx).getName()).append(" to combat them.").append("\n");
                break;
            case REMOVE_CREW:
                stringBuilder.append("You lost! Remove ").append(card.getLostMembers()).append(" crew members to continue the game.").append("\n");
                break;
            case DECIDE_REWARD:
                stringBuilder.append("You won! Decide if you want to earn ").append(card.getEarnedCredits()).append("\n");
                stringBuilder.append("Please note that you will lose ").append(card.getDaysLost()).append(" days of flight.").append("\n");
                break;
            case DONE:
                stringBuilder.append("You're done for this card! Wait for the other players to start the next adventure.").append("\n");
                break;
        }

        rendered = stringBuilder.toString();
    }

    @Override
    public void renderSmugglersState(Game toDisplay) {

        final int DONE = 2;
        final int HANDLE_BOXES = 1;
        final int ACTIVATE_CANNONS = 0;

        AdventureCardState currState = (AdventureCardState) toDisplay.getGameState();
        int currPlayerIdx = currState.getCurrPlayerIdx();
        List<Integer> played = currState.getPlayed();
        List<Player> sortedPlayers = currState.getSortedPlayers();
        AdventureCard card = toDisplay.getCurrentAdventureCard();


        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));
        stringBuilder.append("\n".repeat(3));
        Player p = toDisplay.getPlayer(nickname);

        int playerIdx = p.getRanking() - 1;
        if (played.get(playerIdx) == ACTIVATE_CANNONS && currPlayerIdx == playerIdx) {
            List<Coordinates> lasersCoordinates = p.getShip().getTilesMap().get("LaserTile");
            int totDoubleLasers = (int) lasersCoordinates.stream()
                    .map(coord -> p.getShip().getTile(coord.getX(), coord.getY()))
                    .filter(Tile::isDoubleLaser)
                    .count();
            stringBuilder.append("Smugglers are here! Activate your double cannons and try to defeat them!").append("\n");
            stringBuilder.append("Base fire power of your ship: ").append(p.getShip().getBaseFirePower()).append("\n");
            stringBuilder.append("Number of double cannons: ").append(totDoubleLasers).append("\n");
        } else if (played.get(playerIdx) == ACTIVATE_CANNONS && currPlayerIdx != playerIdx) {
            stringBuilder.append("Smugglers are coming! Wait for ").append(sortedPlayers.get(currPlayerIdx).getName()).append(" to combat them.").append("\n");
        } else if (played.get(playerIdx) == HANDLE_BOXES) {
            stringBuilder.append("You won! Handle your new boxes now if you want.").append("\n");
            stringBuilder.append("Please note that you will lose ").append(card.getDaysLost()).append(" days of flight.").append("\n");
        } else if (played.get(playerIdx) == DONE) {
            stringBuilder.append("You're done for this card! Wait for the other players to start the next adventure.").append("\n");
        }

        rendered = stringBuilder.toString();
    }

    @Override
    public void renderStardustState(Game toDisplay) {

        rendered = renderAdventureTemplate(toDisplay) + "\n".repeat(3) +
                "Send 'stardust' to solve stardust effect and continue the game." +
                "\n" +
                "You may lose some days of flight!" + "\n";
    }

    /**
     * Renders the game state for a player.
     *
     * @param toDisplay {@code Game} which will be printed
     */
    @Override
    public void renderWarZoneState(Game toDisplay) {

        AdventureCard card = toDisplay.getCurrentAdventureCard();
        WarZoneState currState = (WarZoneState) toDisplay.getGameState();
        Integer currPlayerIdx = currState.getCurrPlayerIdx();
        Integer penaltyIdx = currState.getPenaltyIdx();
        List<Integer> played = currState.getPlayed();
        Integer worstPlayerState = currState.getWorstPlayerState();
        Integer currShotIdx = currState.getCurrShotIdx();

        final int WORST = 2;
        final int WILL_FIGHT = 3;
        final int F_INIT = 0;
        final int F_PROVIDE_BATTERY = 1;
        final int F_CORRECT_SHIP = 2;

        //count crew solo leader chiama per tutti;
        //remove crew quando sei worst
        StringBuilder stringBuilder = new StringBuilder(renderAdventureTemplate(toDisplay));
        stringBuilder.append("\n".repeat(3));
        Player p = toDisplay.getPlayer(nickname);
        int playerIdx = p.getRanking() - 1;
        String challenge = card.getParameterCheck().get(penaltyIdx);
        if (!currState.anyWorstPlayer()) {
            switch (challenge) {
                case "CANNONS":
                    stringBuilder.append("It's time to compare fire powers!").append("\n");
                    if (currPlayerIdx == playerIdx) {
                        stringBuilder.append("It's your turn! Send batteries to increase your fire power.").append("\n");
                    } else {
                        stringBuilder.append("Wait for your turn to send batteries to increase your fire power.").append("\n");
                    }
                    break;
                case "PROPULSORS":
                    stringBuilder.append("It's time to compare propulsion powers!").append("\n");
                    if (currPlayerIdx == playerIdx) {
                        stringBuilder.append("It's your turn! Send batteries to increase your propulsion power.").append("\n");
                    } else {
                        stringBuilder.append("Wait for your turn to send batteries to increase your propulsion power.").append("\n");
                    }
                    break;
                case "CREW":
                    stringBuilder.append("It's time to compare the number of crew members!").append("\n");
                    if (currPlayerIdx == playerIdx) {
                        stringBuilder.append("You're the leader, start the challenge!").append("\n");
                    } else {
                        stringBuilder.append("Wait for the leader to start the challenge").append("\n");
                    }
            }
        } else {
            if (card.getPenaltyType().get(currPlayerIdx).equals("LOSECREW")) {
                if (played.get(playerIdx) == WORST) {
                    stringBuilder.append("You're the worst player for this challenge! Remove ").append(card.getLostMembers()).append(" crew members.").append("\n");
                } else {
                    stringBuilder.append("You survived this challenge!").append(penaltyIdx == card.getPenaltyType().size() ? " You're done for this warzone! Wait for the next adventure." :
                            " You're done for this challenge! Wait for the next one.").append("\n");
                }
            } else if (card.getPenaltyType().get(currPlayerIdx).equals("HANDLESHOTS")) {
                if (played.get(playerIdx) == WILL_FIGHT) {
                    switch (worstPlayerState) {
                        case F_INIT:
                            stringBuilder.append("You're the worst player for this challenge! Shot ").append(currShotIdx).append(" is coming, roll the dice to discover its direction").append("\n");
                            break;
                        case F_PROVIDE_BATTERY:
                            stringBuilder.append("You can defend your ship! Send a battery to destroy the shot").append("\n");
                            break;
                        case F_CORRECT_SHIP:
                            stringBuilder.append("You've been hit! Fix your ship by removing tiles until it becomes legal.").append("\n");
                            break;
                    }
                }
            }
        }

        rendered = stringBuilder.toString();
    }
}
