package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlightState extends GameState {
    private final Game game;

    public FlightState(Game game) {
        this.game = game;
    }

    public void getNextAdventureCard(Player player) throws InvalidStateException {
        if (!player.equals(game.getSortedPlayers().getFirst()))
            throw new InvalidStateException("You are not the leading player");
        Game game = player.getGame();
        game.getNextAdventureCard();
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        this.addLog("The leader got the next adventure card: it's " + game.getCurrentAdventureCard().getType() + " time!");
    }

    @Override
    public void retire(Player player) {
        game.getRetiredPlayers().add(player);
        game.getPlayers().remove(player);
        player.setRetired(true);
        this.addLog("Player " + player.getName() + " retired.");
    }

    @Override
    public void updateView (View view, Game toDisplay) throws IOException {
        view.renderFlightState(toDisplay);
    }
}
