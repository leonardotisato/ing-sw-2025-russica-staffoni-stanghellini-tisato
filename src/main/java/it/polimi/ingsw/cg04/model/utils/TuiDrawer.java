package it.polimi.ingsw.cg04.model.utils;

import it.polimi.ingsw.cg04.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TuiDrawer {
    public static String drawTopBoundary(int width) {
        return "┌" + "─".repeat(width - 2) + "┐";
    }

    public static String drawBottomBoundary(int width) {
        return "└" + "─".repeat(width - 2) + "┘";
    }

    public static String drawEmptyRow(int width) {
        return "│" + " ".repeat(width - 2) + "│";
    }

    public static String centerText(String text, int width) {
        if (text.length() >= width) return text.substring(0, width);
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }

    public static String drawCenteredRow(String content, int width) {
        return "│ " + centerText(content, width - 4) + " │";
    }

    public static void clear() {
        // Clear screen + stampa
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static List<String> toLines(String multi) {
        return new ArrayList<>(Arrays.asList(multi.split("\n")));
    }

    public static void adjustVerticalAlignment(List<String> leftLines, List<String> rightLines) {
        int leftHeight = leftLines.size();
        int rightHeight = rightLines.size();

        if (leftHeight > rightHeight) {
            int topPad = (leftHeight - rightHeight) / 2;
            for (int i = 0; i < topPad; i++) rightLines.addFirst("");
            while (rightLines.size() < leftHeight) rightLines.add("");
        } else if (rightHeight > leftHeight) {
            int topPad = (rightHeight - leftHeight) / 2;
            for (int i = 0; i < topPad; i++) leftLines.addFirst("");
            while (leftLines.size() < rightHeight) leftLines.add("");
        }
    }

    /** Crea la colonna destra (upper + lower centrati nelle rispettive metà). */
    public static List<String> buildRightPanel(List<String> upper,
                                               List<String> lower,
                                               int totalH,
                                               int rightW) {

        int halfTop    = totalH / 2;
        int halfBottom = totalH - halfTop;

        /* ---------- blocco superiore centrato ---------- */
        List<String> top = new ArrayList<>();
        int padTop = Math.max(0, (halfTop - upper.size()) / 2);
        for (int i = 0; i < padTop; i++) top.add("");
        top.addAll(upper);
        while (top.size() < halfTop) top.add("");

        /* ---------- blocco inferiore centrato ---------- */
        List<String> bottom = new ArrayList<>();
        int padBottomTop = Math.max(0, (halfBottom - lower.size()) / 2);
        for (int i = 0; i < padBottomTop; i++) bottom.add("");
        bottom.addAll(lower);
        while (bottom.size() < halfBottom) bottom.add("");

        /* ---------- linea orizzontale di separazione ---------- */
        String hSep = "─".repeat(rightW);          // se vuoi la “T”: "┼" + "─".repeat(rightW-1)

        /* ---------- colonna destra finale ---------- */
        List<String> result = new ArrayList<>(top);
        result.add(hSep);
        result.addAll(bottom);

        /*  padding orizzontale di ogni riga a rightW  */
        return result.stream()
                .map(s -> s + " ".repeat(Math.max(0, rightW - s.length())))
                .collect(Collectors.toList());
    }

    public static String renderTwoColumnLayout(List<String> leftPanel,
                                               List<String> rightPanel,
                                               int leftWidth) {

        /* 1. allinea verticalmente */
        adjustVerticalAlignment(leftPanel, rightPanel);

        /* 2. calcola larghezza reale della colonna destra */
        int rightWidth = rightPanel.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        /* 3. indice dove piazzare la linea orizzontale (metà della colonna destra) */
        int splitIndex = rightPanel.size() / 2;

        StringBuilder sb = new StringBuilder("\n");

        for (int i = 0; i < leftPanel.size() - 1; i++) {

            /* ----- colonna sinistra (fissa) ----- */
            String left = String.format("%-" + leftWidth + "s", leftPanel.get(i));

            /* ----- separatore verticale ----- */
            String vSep = i != splitIndex ? "   │   " : "   │";                    // puoi scegliere '|' se preferisci

            /* ----- riga corrente della colonna destra ----- */
            String rightLine;
            if (i == splitIndex) {                // linea orizzontale di separazione
                rightLine = "┄".repeat(rightWidth);  // oppure '─'.repeat(...)
                // se vuoi intersezione “a T”, sostituisci il primo carattere:
                rightLine = "┼" + rightLine.substring(1);  // unisce la verticale
            } else {
                // pad a destra per uniformare la larghezza
                rightLine = rightPanel.get(i) + " ".repeat(rightWidth - rightPanel.get(i).length());
            }

            sb.append(left)
                    .append(vSep)
                    .append(rightLine)
                    .append('\n');
        }
        return sb.toString();
    }


    public static String drawInnerSeparator(int width) {
        return "├" + "─".repeat(width - 2) + "┤";
    }

    public static String renderPlayersByColumn(List<Player> players) {
        List<List<String>> playersInfo = new ArrayList<>();
        for (Player p : players) {
            List<String> singlePlayer = new ArrayList<>();
            singlePlayer.add("Player: " + p.getName());
            singlePlayer.add("Credits: " + p.getNumCredits());
            playersInfo.add(singlePlayer);
        }
        /* ------ 1. larghezza massima per ogni colonna (player) ------ */
        int columns = playersInfo.size();
        int rows    = playersInfo.getFirst().size();          // stesso numero di righe per tutti
        int[] colWidth = new int[columns];

        for (int c = 0; c < columns; c++) {
            int max = 0;
            for (int r = 0; r < rows; r++)
                max = Math.max(max, playersInfo.get(c).get(r).length());
            colWidth[c] = max;                            // larghezza fissa di questa colonna
        }

        /* ------ 2. stampa con colonne allineate ------ */
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                sb.append(String.format("%-" + colWidth[c] + "s", playersInfo.get(c).get(r)));
                if (c < columns - 1) sb.append("  ");     // spazi fra le colonne
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

