package it.polimi.ingsw.cg04.model.utils;

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

}

