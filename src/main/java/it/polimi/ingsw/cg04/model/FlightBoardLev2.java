package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.stream.Collectors;

public class FlightBoardLev2 extends FlightBoard {

    private final int MAX_FLIPS = 3;
    private int timerFlipsUsed = 0;
    private transient Timer timer;
    private transient TimerTask currentTask;
    private long timerEndTime;


    public FlightBoardLev2(Game game) {
        super(game);
        this.path = new Player[24];
        this.pathSize = 24;

        this.startingPosition = new HashMap<>();
        this.startingPosition.put(1, 6);
        this.startingPosition.put(2, 3);
        this.startingPosition.put(3, 1);
        this.startingPosition.put(4, 0);

        this.endGameCredits = new HashMap<>();
        this.endGameCredits.put(1, 8);
        this.endGameCredits.put(2, 6);
        this.endGameCredits.put(3, 4);
        this.endGameCredits.put(4, 2);

        this.mostBeautifulShipCredits = 4;
    }

    public void startTimer() {

        System.out.println("Starting timer");

        if (!isTimerExpired() || timerFlipsUsed >= MAX_FLIPS) return;

        long TIMER_DURATION = 5000;
        timerEndTime = System.currentTimeMillis() + TIMER_DURATION;
        timerFlipsUsed++;

        // start the timer
        timer = new Timer();
        currentTask = new TimerTask() {
            @Override
            public void run() {
                if (isTimerExpired()) {
                    System.out.println("Timer expired");
                    game.notifyListener(new PropertyChangeEvent(game.getId(), "timerExpired", null, timerFlipsUsed));
                }
            }
        };

        timer.schedule(currentTask, TIMER_DURATION);
    }

    public boolean isTimerExpired() {
        return System.currentTimeMillis() >= timerEndTime;
    }

    public int getTimerFlipsUsed() {
        return timerFlipsUsed;
    }

    public int getTimerFlipsRemaining() {
        return MAX_FLIPS - timerFlipsUsed;
    }

    public long getRemainingTime() {
        long remainingTime = timerEndTime - System.currentTimeMillis();
        return Math.max(0, remainingTime);
    }


    public List<Integer> createAdventureCardsDeck(Game game) {
        List<Integer> adventureCardsDeck = new ArrayList<>();
        game.buildPiles();
        for (List<Integer> pile : game.getPreFlightPiles()) {
            adventureCardsDeck.addAll(pile);
        }
        Collections.shuffle(adventureCardsDeck, rand);

        // used for testing purpose
        // adventureCardsDeck.removeFirst();
        // adventureCardsDeck.removeFirst();
        // adventureCardsDeck.addFirst(16);
        // adventureCardsDeck.addFirst(36);


        return adventureCardsDeck;
    }

    public String draw() {
        final int COLS = 9, ROWS = 5;
        final int BOX_INNER = 3;
        final int BOX_W = BOX_INNER + 2;
        final String RESET = "\u001B[0m";

        StringBuilder board = new StringBuilder();

        for (int r = 0; r < ROWS; r++) {
            boolean[] hasBox = new boolean[COLS];
            if (r == 0 || r == ROWS - 1) Arrays.fill(hasBox, true);
            else {
                hasBox[0] = true;
                hasBox[COLS - 1] = true;
            }

            for (int part = 0; part < 3; part++) {
                StringBuilder line = new StringBuilder();

                for (int c = 0; c < COLS; c++) {
                    if (hasBox[c]) {
                        int idx = pathIndex(r, c);
                        boolean occupied = idx != -1 && path[idx] != null;

                        String colorStart = occupied ? path[idx].getColorUnicode() : "";
                        String colorEnd = occupied ? RESET : "";

                        switch (part) {
                            case 0 -> line.append(colorStart)
                                    .append("╭").append("─".repeat(BOX_INNER)).append("╮")
                                    .append(colorEnd);
                            case 2 -> line.append(colorStart)
                                    .append("╰").append("─".repeat(BOX_INNER)).append("╯")
                                    .append(colorEnd);
                            case 1 -> {
                                String content = " ";
                                if (r == 0) { // numeri fissi in prima riga
                                    if (c == startingPosition.get(4)) content = "4";
                                    else if (c == startingPosition.get(3)) content = "3";
                                    else if (c == startingPosition.get(2)) content = "2";
                                    else if (c == startingPosition.get(1)) content = "1";
                                }
                                if (idx >= 0 && path[idx] != null) {
                                    content = path[idx].getName().substring(0, 1);
                                }
                                line.append(colorStart)
                                        .append("│").append(center(content, BOX_INNER)).append("│")
                                        .append(colorEnd);
                            }
                        }
                    } else {
                        line.append(" ".repeat(BOX_W));
                    }
                }

                board.append(line).append('\n');
            }
        }
        return board.toString();
    }

    /**
     * Determines the index based on the row and column within a predefined path grid.
     *
     * @param r the row number (0 to 4) in the path grid
     * @param c the column number (0 to 8) in the path grid
     * @return the calculated path index, or -1 if the row and column combination is invalid
     */
    private static int pathIndex(int r, int c) {
        if (r == 0) {
            return c;                // top row: 0..8
        } else if (r == 1 && c == 8) {
            return 9;
        } else if (r == 2 && c == 8) {
            return 10;
        } else if (r == 3 && c == 8) {
            return 11;
        } else if (r == 4) {
            return 12 + (8 - c);     // bottom row: 12..20
        } else if (r == 3 && c == 0) {
            return 21;
        } else if (r == 2 && c == 0) {
            return 22;
        } else if (r == 1 && c == 0) {
            return 23;
        }
        return -1;
    }
}
