package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

import java.io.IOException;

public class FlightState extends GameState {
    private final Game game;

    public FlightState(Game game) {
        this.game = game;
    }

    /**
     * Retrieves the next adventure card for the game, updating the game state accordingly.
     * <p>
     * This method must be invoked by the leading player in the current game turn.
     * Upon successfully fetching the next adventure card, the game state transitions to the state
     * associated with the retrieved adventure card.
     *
     * @param player the player attempting to retrieve the next adventure card; must be the leading player.
     * @throws InvalidStateException if the player is not the leading player in the game.
     */
    public void getNextAdventureCard(Player player) throws InvalidStateException {
        if (!player.equals(game.getSortedPlayers().getFirst()))
            throw new InvalidStateException("You are not the leading player");
        Game game = player.getGame();
        game.getNextAdventureCard();
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        this.addLog("The leader got the next adventure card: it's " + game.getCurrentAdventureCard().getType() + " time!");
    }

    /**
     * Marks the specified player as retired, updates the game state by removing the player
     * from the active players list, and logs the retirement action. The player's retired status
     * is updated to reflect this change.
     *
     * @param player the player who is retiring from the game
     */
    @Override
    public void retire(Player player) {
        game.getRetiredPlayers().add(player);
        game.getPlayers().remove(player);
        player.setRetired(true);
        this.addLog("Player " + player.getName() + " retired.");
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
        view.renderFlightState(toDisplay);
    }
}
