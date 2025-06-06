package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.buildRightPanel;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public class StardustState extends AdventureCardState {

    public StardustState(Game game) {
        super(game);
    }


    /**
     * Executes the stardust phase for the current player. The player must be the one whose turn it is.
     * During this phase, all players lose a number of "flight days" based on the number of exposed connectors on their ships.
     *
     * @param player the player attempting to execute the stardust phase
     * @throws InvalidStateException if it is not the player's turn, or the action is invalid for the current state
     */
    public void starDust(Player player) throws InvalidStateException {
        if (player.getName().equals(sortedPlayers.getFirst().getName())) {
            for (int i = sortedPlayers.size() - 1; i >= 0; i--) {
                sortedPlayers.get(i).move(-sortedPlayers.get(i).getShip().getNumExposedConnectors());
                if (i == sortedPlayers.size() - 1) {
                    this.addLog("Player " + sortedPlayers.get(i).getName() + " perde " + sortedPlayers.get(i).getShip().getNumExposedConnectors() + " giorni di volo.");
                } else {
                    this.appendLog("Player " + sortedPlayers.get(i).getName() + " perde " + sortedPlayers.get(i).getShip().getNumExposedConnectors() + " giorni di volo.");
                }
            }
            triggerNextState();
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
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
        view.renderStardustState(toDisplay);
    }
}
