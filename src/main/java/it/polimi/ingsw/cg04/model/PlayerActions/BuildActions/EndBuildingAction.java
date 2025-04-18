package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class EndBuildingAction implements PlayerAction {
    String playerNickname;
    int position;
    public EndBuildingAction(String playerNickname, int position) {
        this.playerNickname = playerNickname;
        this.position = position;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        player.getGame().getGameState().endBuilding(player, position);
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        if (position < 1 || position > 4) throw new InvalidActionException("Position must be between 1 and 4!");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
