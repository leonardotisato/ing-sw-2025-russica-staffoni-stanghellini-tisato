package it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

import java.util.ArrayList;
import java.util.List;

public abstract class InitAction implements Action {

    protected List<String> logs;
    protected String nickname;

    protected InitAction(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Executes the implementation-specific logic of the action using the provided GamesController instance.
     *
     * @param controller the GamesController instance on which the action is executed
     */
    public abstract void execute(GamesController controller);

    /**
     * Validates whether the action can be performed using the provided GamesController instance.
     *
     * @param controller the GamesController instance to validate the action against
     * @return true if the action is valid and can be performed
     * @throws InvalidActionException if the action is invalid
     */
    public abstract boolean checkAction(GamesController controller) throws InvalidActionException;

    @Override
    public String getPlayerNickname() {
        return nickname;
    }

    @Override
    public void dispatchTo(GamesController controller) throws InvalidActionException {
        controller.onInitActionReceived(this);
    }

    /**
     * Adds a log message to the list of logs.
     *
     * @param newLog the log message to be added to the logs list
     */
    public void addLog(String newLog) {
        if (logs == null) {
            logs = new ArrayList<>();
        }
        this.logs.add(newLog);
    }

    /**
     *
     * @return a list of strings representing the log messages
     */
    public List<String> getLogs() {
        return logs;
    }

}
