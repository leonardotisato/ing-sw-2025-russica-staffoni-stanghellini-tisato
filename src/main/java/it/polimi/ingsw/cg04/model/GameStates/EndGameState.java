package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EndGameState extends GameState {
    private Game game;
    private List<Player> leaderboard;

    public EndGameState(Game game) {
        this.game = game;
        this.leaderboard = new ArrayList<>();
        leaderboard = game.getPlayers();
        leaderboard.addAll(game.getRetiredPlayers());
        leaderboard.sort(Comparator.comparingDouble(Player::getNumCredits).reversed());
    }

    public List<Player> getLeaderboard() {
        return leaderboard;
    }

    @Override
    public void updateView(View view, Game toDisplay) throws IOException {
        view.renderEndGameState(toDisplay);
    }
}
