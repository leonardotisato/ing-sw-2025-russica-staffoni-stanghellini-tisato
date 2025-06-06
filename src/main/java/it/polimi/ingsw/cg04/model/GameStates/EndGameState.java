package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EndGameState extends GameState {
    private List<Player> leaderboard;

    public EndGameState(Game game) {
        this.leaderboard = new ArrayList<>();
        leaderboard = game.getPlayers();
        leaderboard.addAll(game.getRetiredPlayers());
        leaderboard.sort(Comparator.comparingDouble(Player::getNumCredits).reversed());
    }

    /**
     * Retrieves the leaderboard for the current game state.
     *
     * @return a list of {@code Player} objects representing the leaderboard.
     */
    public List<Player> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Updates the given view with the current state of the provided game object.
     *
     * @param view      the view instance that should be updated
     * @param toDisplay the game object containing the state to render on the view
     * @throws IOException if an input or output exception occurs during the view update
     */
    @Override
    public void updateView(View view, Game toDisplay) throws IOException {
        view.renderEndGameState(toDisplay);
    }
}
