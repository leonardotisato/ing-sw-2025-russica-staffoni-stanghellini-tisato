package it.polimi.ingsw.cg04.model;

import java.util.*;

public class FlightBoardLev1 extends FlightBoard{
    public FlightBoardLev1(Game game){
        super(game);
        this.path = new Player[18];
        this.pathSize = 18;
        this.startingPosition = new HashMap<Integer, Integer>();
        this.startingPosition.put(1, 4);
        this.startingPosition.put(2, 2);
        this.startingPosition.put(3, 1);
        this.startingPosition.put(4, 0);
        this.endGameCredits = new HashMap<Integer, Integer>();
        this.endGameCredits.put(1, 4);
        this.endGameCredits.put(2, 3);
        this.endGameCredits.put(3, 2);
        this.endGameCredits.put(4, 1);
        this.mostBeautifulShipCredits = 2;
    }

    @Override
    public List<Integer> createAdventureCardsDeck(Game game) {
        List<Integer> adventureCardsDeck = new ArrayList<>(List.of(1, 3, 4, 8, 12, 15, 17, 18));
        Collections.shuffle(adventureCardsDeck, rand);
        return adventureCardsDeck;
    }

    public String draw() {
        final int COLS = 7, ROWS = 4;
        final int BOX_INNER = 3;
        final int BOX_W = BOX_INNER + 2;
        final int TITLE_ROW = 2;
        final String TITLE = "level 1 flightboard";
        final String RESET = "\u001B[0m";

        StringBuilder board = new StringBuilder();

        for (int r = 0; r < ROWS; r++) {

            boolean[] hasBox = new boolean[COLS];
            if (r == 0 || r == ROWS - 1) Arrays.fill(hasBox, true);
            else { hasBox[0] = true; hasBox[COLS - 1] = true; }

            // three parts: top middle bottom
            for (int part = 0; part < 3; part++) {
                StringBuilder line = new StringBuilder();

                for (int c = 0; c < COLS; c++) {
                    if (hasBox[c]) {
                        int idx = pathIndex(r, c);          // −1 if not on the perimeter
                        boolean occupied = idx != -1 && path[idx] != null; // chek if the box is occupied

                        String colorStart = occupied ? path[idx].getColorUnicode() : "";
                        String colorEnd   = occupied ? RESET : "";

                        switch (part) {
                            case 0 -> line.append(colorStart)
                                    .append("╭").append("─".repeat(BOX_INNER)).append("╮")
                                    .append(colorEnd);
                            case 2 -> line.append(colorStart)
                                    .append("╰").append("─".repeat(BOX_INNER)).append("╯")
                                    .append(colorEnd);
                            case 1 -> {
                                String content = " ";
                                if (r == 0) {        // numeri fissi
                                    if (c == 0) content = "4";
                                    else if (c == 1) content = "3";
                                    else if (c == 2) content = "2";
                                    else if (c == 4) content = "1";
                                }
                                if (path[idx] != null) content = path[idx].getName().substring(0, 1);
                                line.append(colorStart)
                                        .append("│").append(this.center(content, BOX_INNER)).append("│")
                                        .append(colorEnd);
                            }
                        }
                    } else line.append(" ".repeat(BOX_W)); // empty
                }

                // center content inside the box
                if (r == TITLE_ROW && part == 1) {
                    int blankStart = BOX_W;
                    int blankWidth = (COLS - 2) * BOX_W;
                    line.replace(blankStart, blankStart + blankWidth,
                            center(TITLE, blankWidth));
                }

                board.append(line).append('\n');
            }
        }
        return board.toString();
    }

    /**
     * Computes the one-dimensional path index for a given cell in a bidimensional grid,
     * based on its row and column coordinates. The index is determined based on the
     * perimeter paths of a specific grid layout.
     *
     * @param r the row index of the cell
     * @param c the column index of the cell
     * @return the computed path index of the cell if it is on the grid perimeter,
     *         or -1 if the cell is not on the perimeter
     */
    private static int pathIndex(int r, int c) {
        // upper row, order left to right
        if (r == 0) return c;                            // 0-6
        // right column, order top to bottom
        if (c == 6 && (r == 1 || r == 2)) return 6 + r;  // 7-8
        // lower row order right to left
        if (r == 3) return 9 + (6 - c);                  // 9-15
        // left column order bottom to top
        if (c == 0 && (r == 2 || r == 1)) return 16 + (2 - r); // 16-17
        return -1;                                       // not on perimeter
    }
}
