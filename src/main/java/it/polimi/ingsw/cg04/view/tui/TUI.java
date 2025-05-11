package it.polimi.ingsw.cg04.view.tui;

import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import it.polimi.ingsw.cg04.view.View;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class TUI extends View {
    private final InputReader input;

    public TUI(ServerHandler server) {
        super(server);

        input = new InputReader();
        Thread t = new Thread(input);
        t.setName("Stdin InputReader");
        t.setDaemon(true);
        t.start();
    }


    public void updateGame() {

    }


    public void updateLogs() {

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

        private void handleInput(String input){
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter(" ");

            try {

                String command = scanner.next();

                switch (command) {
                    case "setNickname":
                        break;
                    case "createGame":
                        break;
                    case "joinGame":
                        break;
                    case "chooseTile":
                        break;
                    case "chooseTileFromBuffer":
                        break;
                    case "closeFaceUp":
                        break;
                    case "drawFaceDown":
                        break;
                    case "endBuilding":
                        break;
                    case "pickPile":
                        break;
                    case "place":
                        break;
                    case "placeInBuffer":
                        break;
                    case "returnPile":
                        break;
                    case "returnTile":
                        break;
                    case "showFaceUp":
                        break;
                    case "startTimer":
                        break;
                    case "chooseBattery":
                        break;
                    case "choosePropulsor":
                        break;
                    case "compareCrew":
                        break;
                    case "compareFirePower":
                        break;
                    case "epidemic":
                        break;
                    case "getNextAdventureCard":
                        break;
                    case "getRewards":
                        break;
                    case "handleBoxes":
                        break;
                    case "planets":
                        break;
                    case "removeCrew":
                        break;
                    case "retire":
                        break;
                    case "rollDice":
                        break;
                    case "stardust":
                        break;
                    case "fixShip":
                        break;
                    case "loadCrew":
                        break;
                    case "helper":
                        helper();
                        break;
                    default:
                        throw new NoSuchElementException();
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Try -h for help.");
            }
        }

        private void helper() {
            System.out.println("Commands list:");

            System.out.println();
            System.out.println("LOBBY COMMANDS:");



            System.out.println();
            System.out.println("BUILDING COMMANDS");



            System.out.println();
            System.out.println("ADVENTURE CARDS COMMANDS:");



        }

        private void stopInputReader(){
            if(!stdinClosed)
                System.out.println("Connection to server unavailable, exiting.");
            stdinClosed = true;
        }
    }
}
