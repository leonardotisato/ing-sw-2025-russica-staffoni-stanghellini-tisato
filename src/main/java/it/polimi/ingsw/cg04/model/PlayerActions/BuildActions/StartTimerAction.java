package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class StartTimerAction implements PlayerAction {
    String playerNickname;

    public StartTimerAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }
    @Override
    public void execute(Player player) throws InvalidStateException {
        player.getGame().getGameState().startTimer(player);
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
