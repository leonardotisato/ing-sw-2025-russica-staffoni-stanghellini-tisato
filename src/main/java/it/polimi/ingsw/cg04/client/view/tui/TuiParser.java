package it.polimi.ingsw.cg04.client.view.tui;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.*;

public class TuiParser {

    private int columnOffset;

    public TuiParser(int columnOffset) {
        this.columnOffset = columnOffset;
    }

    public void printCommandFormat(String command) {
        System.out.println("Parameter format for command: " + command);
        switch (command) {
            case "setNickname", "setNick" -> System.out.println("  <nickname>");
            case "createGame", "create" -> System.out.println("  <gameLevel> <maxPlayers> <COLOR>");
            case "joinGame", "join" -> System.out.println("  <gameId> <COLOR>");
            case "chooseTile", "choose" -> System.out.println("  <tileIndex>");
            case "chooseTileFromBuffer" -> System.out.println("  <tileIndex>");
            case "pickPile" -> System.out.println("  <pileIndex>");
            case "endBuilding" -> System.out.println("  <startingPosition>");
            case "place", "p" -> System.out.println("  <x> <y>");
            case "rotate", "r" -> System.out.println("  <LEFT | RIGHT | DOWN>");
            case "chooseBattery" -> {
                System.out.println("Press enter if you don't want to activate double cannons.");
                System.out.println("  <x> <y>");
            }
            case "choosePropulsor" -> {
                System.out.println("Press enter if you don't want to activate double propulsors.");
                System.out.println("  -c <x1> <y1> <x2> <y2> ... -n <battery1> <battery2> ...");
            }
            case "compareCrew" -> System.out.println("  (no arguments)");
            case "compareFirePower" -> {
                System.out.println("Press enter if you don't want to activate double cannons.");
                System.out.println("  -c <battery_x1> <battery_y1> ... -c <cannon_x1> <cannon_y1> ...");
            }
            case "drawFaceDown", "draw", "d" -> System.out.println("  (no arguments)");
            case "returnPile" -> System.out.println("  (no arguments)");
            case "returnTile" -> System.out.println("  (no arguments)");
            case "placeInBuffer" -> System.out.println("  (no arguments)");
            case "closeFaceUp" -> System.out.println("  (no arguments)");
            case "showFaceUp" -> System.out.println("  (no arguments)");
            case "startTimer" -> System.out.println("  (no arguments)");
            case "getNextAdventureCard" -> System.out.println("  (no arguments)");
            case "epidemic" -> System.out.println("  (no arguments)");
            case "getRewards" -> System.out.println("  <true|false>");
            case "handleBoxes" -> {

                System.out.println("  -c <x1> <y1> ... -m -k <BoxType> <value> -k <BoxType> <value> ... -m ...");
            }
            case "planets" -> {
                    System.out.println("Press enter if you don't want to land on a planet.");
                    System.out.println("  <planetIndex> -c <x1> <y1> ... -m -k <BoxType> <value> -k <BoxType> <value> ... -m ...");
            }
            case "removeCrew" -> {
                System.out.println("Press enter if you don't want to remove crew (not possible in WarZone).");
                System.out.println("  -c <coord_x1> <coord_y1> ... -n <crew1> <crew2> ...");
            }
            case "retire" -> System.out.println("  (no arguments)");
            case "rollDice" -> System.out.println("  (no arguments)");
            case "stardust" -> System.out.println("  (no arguments)");
            case "fixShip" -> System.out.println("  -c <x1> <y1> ...");
            case "loadCrew" -> {
                System.out.println("Press enter if you don't want to load crew.");
                System.out.println(" -p <pink_x> <pink_y> -b <brown_x> <brown_y>");
            }
            case "helper", "viewShips", "ships" -> {}
            default -> System.out.println("  Unknown command");
        }
        System.out.println();
    }

    public record ParsedArgs(
            List<List<Coordinates>> coordGroups,
            List<Integer> intList,
            List<Map<BoxType, Integer>> boxMapList,
            int singleInt,
            String string,
            Coordinates coord1,
            Coordinates coord2,
            boolean accept,
            Integer planetIdx
    ) {
    }

    public ParsedArgs parseArguments(String command, String inputLine) {
        Scanner scanner = new Scanner(inputLine);
        List<List<Coordinates>> coordGroups = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        List<Map<BoxType, Integer>> boxList = new ArrayList<>();
        int singleInt = -1;
        String string = null;
        Coordinates coord1 = null;
        Coordinates coord2 = null;
        boolean accept = true;
        Integer planetIdx = null;

        switch (command) {
            case "setNickname", "setNick", "rotate" -> string = scanner.hasNext() ? scanner.next() : null;
            case "createGame", "create" -> {
                if (scanner.hasNextInt()) singleInt = scanner.nextInt();
                if (scanner.hasNextInt()) intList.add(scanner.nextInt());
                if (scanner.hasNext()) string = scanner.next().toUpperCase();
            }
            case "joinGame", "join" -> {
                if (scanner.hasNextInt()) singleInt = scanner.nextInt();
                if (scanner.hasNext()) string = scanner.next().toUpperCase();
            }
            case "chooseTile", "choose", "chooseTileFromBuffer", "pickPile", "endBuilding" -> {
                if (scanner.hasNextInt()) singleInt = scanner.nextInt();
            }
            case "place", "chooseBattery" -> {
                if (scanner.hasNextInt()) {
                    int x = scanner.nextInt();
                    if (scanner.hasNextInt()) coord1 = new Coordinates(x - 5, scanner.nextInt() - columnOffset);
                }
            }
            case "choosePropulsor", "removeCrew" -> {
                while (scanner.hasNext()) {
                    String token = scanner.next();
                    if ("-c".equals(token)) {
                        List<Coordinates> group = new ArrayList<>();
                        while (scanner.hasNextInt()) {
                            int x = scanner.nextInt();
                            if (scanner.hasNextInt())
                                group.add(new Coordinates(x - 5, scanner.nextInt() - columnOffset));
                        }
                        coordGroups.add(group);
                    } else if ("-n".equals(token)) {
                        while (scanner.hasNextInt()) intList.add(scanner.nextInt());
                    }
                }
            }
            case "compareFirePower" -> {
                while (scanner.hasNext("-c")) {
                    scanner.next();
                    List<Coordinates> group = new ArrayList<>();
                    while (scanner.hasNextInt()) {
                        int x = scanner.nextInt();
                        if (scanner.hasNextInt()) group.add(new Coordinates(x - 5, scanner.nextInt() - columnOffset));
                    }
                    coordGroups.add(group);
                }
            }
            case "getRewards" -> {
                if (scanner.hasNext()) accept = Boolean.parseBoolean(scanner.next());
            }
            case "handleBoxes", "planets" -> {
                if (command.equals("planets") && scanner.hasNextInt()) planetIdx = scanner.nextInt();
                while (scanner.hasNext()) {
                    String token = scanner.next();
                    if ("-c".equals(token)) {
                        List<Coordinates> group = new ArrayList<>();
                        while (scanner.hasNextInt()) {
                            int x = scanner.nextInt();
                            if (scanner.hasNextInt())
                                group.add(new Coordinates(x - 5, scanner.nextInt() - columnOffset));
                        }
                        coordGroups.add(group);
                    } else if ("-m".equals(token)) {
                        Map<BoxType, Integer> map = new HashMap<>();
                        while (scanner.hasNext("-k")) {
                            scanner.next();
                            String type = scanner.next();
                            int value = scanner.nextInt();
                            map.put(BoxType.valueOf(type), value);
                        }
                        for (BoxType boxType : BoxType.values()) {
                            if (!map.containsKey(boxType)) map.put(boxType, 0);
                        }
                        boxList.add(map);
                    }
                }
            }
            case "fixShip" -> {
                if (scanner.hasNext("-c")) {
                    scanner.next();
                    List<Coordinates> group = new ArrayList<>();
                    while (scanner.hasNextInt()) {
                        int x = scanner.nextInt();
                        if (scanner.hasNextInt()) group.add(new Coordinates(x - 5, scanner.nextInt() - columnOffset));
                    }
                    coordGroups.add(group);
                }
            }
            case "loadCrew" -> {
                if (scanner.hasNext("-p")) {
                    scanner.next();
                    if (scanner.hasNextInt()) {
                        int x1 = scanner.nextInt();
                        int y1 = scanner.nextInt();
                        coord1 = new Coordinates(x1 - 5, y1 - columnOffset);
                    }
                }
                if (scanner.hasNext("-b")) {
                    scanner.next();
                    if (scanner.hasNextInt()) {
                        int x2 = scanner.nextInt();
                        int y2 = scanner.nextInt();
                        coord2 = new Coordinates(x2 - 5, y2 - columnOffset);
                    }
                }
            }
        }

        return new ParsedArgs(coordGroups, intList, boxList, singleInt, string, coord1, coord2, accept, planetIdx);
    }
}
