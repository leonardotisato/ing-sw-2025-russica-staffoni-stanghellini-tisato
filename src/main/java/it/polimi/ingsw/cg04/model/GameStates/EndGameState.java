package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EndGameState extends GameState{
    private Game game;
    private List<Player> leaderboard;

    public EndGameState(Game game) {
        this.game = game;
        this.leaderboard = new ArrayList<>();
        leaderboard = game.getPlayers();
        leaderboard.sort(Comparator.comparingDouble(Player::getNumCredits).reversed());
    }

    public String render(String playerName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(playerName).append("The game has ended. \n\n");
        Player p = game.getPlayer(playerName);
        stringBuilder.append(p.getNumCredits() >= 1 ? "You have one credit or more, so you won!" : "You have no credit, you lost!").append("\n");
        stringBuilder.append("Leaderboard: ").append("\n");
        for(Player p1 : leaderboard){
            stringBuilder.append(leaderboard.indexOf(p1) + 1).append("- ").append(p1.getName()).append(": ").append(p1.getNumCredits()).append(" credits").append("\n");
        }
        return stringBuilder.toString();
    }
}
