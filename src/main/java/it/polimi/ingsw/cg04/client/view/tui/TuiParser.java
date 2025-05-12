package it.polimi.ingsw.cg04.client.view.tui;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.*;

public class TuiParser {

    public void printCommandFormat(String command) {
        System.out.println("Parameter format for command: " + command);
        switch (command) {
            case "setNickname" -> System.out.println("  <nickname>");
            case "createGame" -> System.out.println("  <gameLevel> <maxPlayers> <color>");
            case "joinGame" -> System.out.println("  <gameId> <color>");
            case "chooseTile" -> System.out.println("  <tileIndex>");
            case "chooseTileFromBuffer" -> System.out.println("  <tileIndex>");
            case "pickPile" -> System.out.println("  <pileIndex>");
            case "endBuilding" -> System.out.println("  <position>");
            case "place" -> System.out.println("  <x> <y>");
            case "chooseBattery" -> System.out.println("  <x> <y>");
            case "choosePropulsor" -> System.out.println("  -c <x1> <y1> <x2> <y2> ... -n <battery1> <battery2> ...");
            case "compareCrew" -> System.out.println("  (no arguments)");
            case "compareFirePower" -> System.out.println("  -c <battery_x1> <battery_y1> ... -c <cannon_x1> <cannon_y1> ...");
            case "drawFaceDown" -> System.out.println("  (no arguments)");
            case "returnPile" -> System.out.println("  (no arguments)");
            case "returnTile" -> System.out.println("  (no arguments)");
            case "placeInBuffer" -> System.out.println("  (no arguments)");
            case "closeFaceUp" -> System.out.println("  (no arguments)");
            case "showFaceUp" -> System.out.println("  (no arguments)");
            case "startTimer" -> System.out.println("  (no arguments)");
            case "getNextAdventureCard" -> System.out.println("  (no arguments)");
            case "epidemic" -> System.out.println("  (no arguments)");
            case "getRewards" -> System.out.println("  <true|false>");
            case "handleBoxes" -> System.out.println("  -c <x1> <y1> ... -m -k <BoxType> <value> -k <BoxType> <value> ... -m ...");
            case "planets" -> System.out.println("  <planetIndex> -c <x1> <y1> ... -m -k <BoxType> <value> -k <BoxType> <value> ... -m ...");
            case "removeCrew" -> System.out.println("  -c <coord_x1> <coord_y1> ... -n <crew1> <crew2> ...");
            case "retire" -> System.out.println("  (no arguments)");
            case "rollDice" -> System.out.println("  (no arguments)");
            case "stardust" -> System.out.println("  (no arguments)");
            case "fixShip" -> System.out.println("  -c <x1> <y1> ...");
            case "loadCrew" -> System.out.println("  <pink_x> <pink_y> <brown_x> <brown_y>");
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
            int planetIdx
    ) {}

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
        int planetIdx = -1;

        switch (command) {
            case "setNickname" -> string = scanner.hasNext() ? scanner.next() : null;
            case "createGame" -> {
                if (scanner.hasNextInt()) singleInt = scanner.nextInt();
                if (scanner.hasNextInt()) intList.add(scanner.nextInt());
                if (scanner.hasNext()) string = scanner.next();
            }
            case "joinGame" -> {
                if (scanner.hasNextInt()) singleInt = scanner.nextInt();
                if (scanner.hasNext()) string = scanner.next();
            }
            case "chooseTile", "chooseTileFromBuffer", "pickPile", "endBuilding" -> {
                if (scanner.hasNextInt()) singleInt = scanner.nextInt();
            }
            case "place", "chooseBattery" -> {
                if (scanner.hasNextInt()) {
                    int x = scanner.nextInt();
                    if (scanner.hasNextInt()) coord1 = new Coordinates(x, scanner.nextInt());
                }
            }
            case "choosePropulsor", "removeCrew" -> {
                while (scanner.hasNext()) {
                    String token = scanner.next();
                    if ("-c".equals(token)) {
                        List<Coordinates> group = new ArrayList<>();
                        while (scanner.hasNextInt()) {
                            int x = scanner.nextInt();
                            if (scanner.hasNextInt()) group.add(new Coordinates(x, scanner.nextInt()));
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
                        if (scanner.hasNextInt()) group.add(new Coordinates(x, scanner.nextInt()));
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
                            if (scanner.hasNextInt()) group.add(new Coordinates(x, scanner.nextInt()));
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
                        if (scanner.hasNextInt()) group.add(new Coordinates(x, scanner.nextInt()));
                    }
                    coordGroups.add(group);
                }
            }
            case "loadCrew" -> {
                if (scanner.hasNextInt()) {
                    int x1 = scanner.nextInt();
                    int y1 = scanner.nextInt();
                    coord1 = new Coordinates(x1, y1);
                }
                if (scanner.hasNextInt()) {
                    int x2 = scanner.nextInt();
                    int y2 = scanner.nextInt();
                    coord2 = new Coordinates(x2, y2);
                }
            }
        }

        return new ParsedArgs(coordGroups, intList, boxList, singleInt, string, coord1, coord2, accept, planetIdx);
    }
}
