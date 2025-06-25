package it.polimi.ingsw.cg04.controller.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class EndBuildingAction extends PlayerAction {
    int position;

    public EndBuildingAction(String playerNickname, int position) {
        super(playerNickname);
        this.position = position;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.endBuilding(player, position);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        if (position < 1 || position > 4) throw new InvalidActionException("Position must be between 1 and 4!");
        return true;
    }
}
