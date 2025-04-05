package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;

public class StardustAction implements PlayerAction {
    private Game game;

    public StardustAction(Game game) {
        this.game = game;
    }

    public void execute(Player player) {
        // AdventureCardState gameState = (AdventureCardState)game.getGameState();

        player.move(-player.getShip().getNumExposedConnectors());

        // gameState.getPlayed().set(gameState.getCurrPlayerIdx(), 1);
    }

    @Override
    public boolean checkAction(Player player) {
        return false;
    }
}
