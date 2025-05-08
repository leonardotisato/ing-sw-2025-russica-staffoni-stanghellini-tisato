package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.FlightState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AdventureCardState extends GameState {
    protected List<Player> sortedPlayers;
    protected List<Integer> played;
    protected Integer currPlayerIdx;
    protected AdventureCard card;
    protected Game context;
    protected List<Player> currPlayers;


    public AdventureCardState(Game game) {
        this.context = game;
        this.sortedPlayers = game.getSortedPlayers();
        this.card = game.getCurrentAdventureCard();
        this.played = new ArrayList<>(Collections.nCopies(this.sortedPlayers.size(), 0));
        this.currPlayerIdx = 0;
    }

    public void triggerNextState() {
        if(card != null) {
            System.out.println(card.getType() + " è stata risolta!");
        }
        context.setGameState(new FlightState(context));
        context.setCurrentAdventureCard(null);
    }

    public List<Player> getSortedPlayers() {
        return sortedPlayers;
    }

    public List<Integer> getPlayed() {
        return played;
    }

    public Integer getCurrPlayerIdx() {
        return currPlayerIdx;
    }

    public AdventureCard getCard() {
        return card;
    }

    public Game getContext() {
        return context;
    }

    public void setSortedPlayers(List<Player> sortedPlayers) {
        this.sortedPlayers = sortedPlayers;
    }

    public void setPlayed(List<Integer> played) {
        this.played = played;
    }

    public void setCurrPlayerIdx(Integer currPlayerIdx) {
        this.currPlayerIdx = currPlayerIdx;
    }

    public void setCard(AdventureCard card) {
        this.card = card;
    }

    public void setContext(Game context) {
        this.context = context;
    }

    public boolean checkAction(Player player) {
        return true;
    }

    public Map<Player, Integer> getChosenPlanets() {
        return null;
    }

    public Boolean getAllPlanetsChosen() {
        return false;
    }

    public void setAllPlanetsChosen(Boolean allPlanetsChosen) {
        return;
    }

    public void setPlayerToBeMovedIdx(Integer playerToBeMovedIdx) {
        return;
    }

    public List<Player> getCurrPlayers() {
        return currPlayers;
    }

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder();
        List<List<String>> playersInfo = new ArrayList<>();
        for (Player p : context.getSortedPlayers()) {
            List<String> singlePlayer = new ArrayList<>();
            singlePlayer.add("Player: " + p.getName());
            singlePlayer.add("Credits: " + p.getNumCredits());
            playersInfo.add(singlePlayer);
        }
        /* ------ 1. larghezza massima per ogni colonna (player) ------ */
        int columns = playersInfo.size();
        int rows    = playersInfo.get(0).size();          // stesso numero di righe per tutti
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
        System.out.println(sb.toString());
        System.out.println("Your ship:");
        stringBuilder.append(context.getPlayer(playerName).getShip().draw());
        return stringBuilder.toString();
    }
}
