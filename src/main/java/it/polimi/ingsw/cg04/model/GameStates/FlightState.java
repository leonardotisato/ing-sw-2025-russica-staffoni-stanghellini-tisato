package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlightState extends GameState {
    private Game game;
    public FlightState (Game game){
        this.game = game;
    }
    public void getNextAdventureCard(Player player) throws InvalidStateException {
        if(!player.equals(game.getSortedPlayers().getFirst())) throw new InvalidStateException("You are not the leading player");
        Game game = player.getGame();
        game.getNextAdventureCard();
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        this.addLog("The leader got the next adventure card: it's " + game.getCurrentAdventureCard().getType() + " time!");
    }

    @Override
    public void retire(Player player){
        game.getRetiredPlayers().add(player);
        game.getPlayers().remove(player);
        this.addLog("Player " + player.getName() + " retired");
    }

    @Override
    public String render(String playerName) {
        List<String> leftLines = new ArrayList<>();

        // Informazioni generali
        leftLines.add("State: flightState");
        leftLines.add("Remaining cards to play: " + game.getAdventureCardsDeck().size());
        leftLines.add("");

        // Info su ogni giocatore
        for (Player p : game.getSortedPlayers()) {
            leftLines.add("Player: " + p.getName());
            leftLines.add("Credits: " + p.getNumCredits());
            leftLines.add("");
        }

        // Crea blocco per la board
        List<String> rightLines = new ArrayList<>(Arrays.asList(game.getBoard().draw().split("\n")));

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
        Player p = game.getPlayer(playerName);
        stringBuilder.append("\n");
        stringBuilder.append(p.getRanking() == 1 ? "Send x to start the next adventure!" : ("wait for " + game.getPlayer(0).getName() + " to start the next adventure!"));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
