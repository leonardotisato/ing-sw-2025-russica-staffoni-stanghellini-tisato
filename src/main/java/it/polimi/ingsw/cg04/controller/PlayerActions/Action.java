package it.polimi.ingsw.cg04.controller.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

import java.io.Serializable;

public interface Action extends Serializable {

    /**
     * Executes the action for the specified player. (Command pattern's execute)
     *
     * @param player the player for which the action is executed.
     * @throws InvalidStateException if the action cannot be executed due to an invalid state.
     */
    void execute(Player player) throws InvalidStateException;

    /**
     * Checks whether the given player can perform the action.
     *
     * @param player the player attempting to perform the action.
     * @return true if the action is valid for the player;
     * @throws InvalidActionException if the action cannot be performed
     */
    boolean checkAction(Player player) throws InvalidActionException;

    /**
     * Retrieves the nickname of the player associated with the action.
     *
     * @return the player's nickname as a string
     */
    String getPlayerNickname();

    /**
     * Dispatches the action to the specified {@link GamesController} for processing.
     *
     * @param controller the GamesController handling the action dispatch.
     * @throws InvalidActionException
     * @throws InvalidStateException
     */
    void dispatchTo(GamesController controller) throws InvalidActionException, InvalidStateException;
}
