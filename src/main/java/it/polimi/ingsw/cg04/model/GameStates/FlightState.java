package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

import java.util.List;

public class FlightState extends GameState {
    private Game game;
    public FlightState (Game game){
        this.game = game;
    }
    public void getNextAdventureCard(Player player){
        Game game = player.getGame();
        game.getNextAdventureCard();
        game.getCurrentAdventureCard().createState(game);
    }

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append("State: flightState\n");
        stringBuilder.append("Remaining cards to play: ").append(game.getAdventureCardsDeck().size()).append("\n").append("\n");
        for (Player p : game.getSortedPlayers()) {
            stringBuilder.append("player: ").append(p.getName()).append("\n");
            stringBuilder.append("Color: ").append(p.getColor()).append("\n");
            stringBuilder.append("Position: ").append(p.getRanking()).append("\n");
            stringBuilder.append("Credits: ").append(p.getNumCredits()).append("\n").append("\n");
        }
        stringBuilder.append(game.getBoard().draw()).append("\n");
        Player p = game.getPlayer(playerName);
        stringBuilder.append(p.getRanking() == 1 ? "Send x to start the next adventure!" : ("wait for " + game.getPlayer(0).getName() + " to start the next adventure!"));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
