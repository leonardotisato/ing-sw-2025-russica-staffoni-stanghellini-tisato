package it.polimi.ingsw.cg04.controller.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class StartTimerAction extends PlayerAction {

    public StartTimerAction(String playerNickname) {
        super(playerNickname);
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.startTimer(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        if (!player.getGame().getBoard().isTimerExpired()) throw new InvalidActionException("Timer not expired!");
        if (player.getGame().getBoard().getTimerFlipsRemaining() <= 0)
            throw new InvalidActionException("No flips remaining!");

        return true;
    }
}
