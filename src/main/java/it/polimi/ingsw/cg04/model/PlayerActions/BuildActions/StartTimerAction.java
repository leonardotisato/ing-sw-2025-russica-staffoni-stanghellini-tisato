package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class StartTimerAction extends PlayerAction {
    String playerNickname;

    public StartTimerAction(String playerNickname) {
        this.playerNickname = playerNickname;
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

        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
