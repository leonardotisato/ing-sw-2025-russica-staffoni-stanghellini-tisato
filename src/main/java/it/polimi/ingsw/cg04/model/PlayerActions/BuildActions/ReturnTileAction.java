package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class ReturnTileAction implements PlayerAction {
    String playerNickname;

    public ReturnTileAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }
    public void execute(Player player) throws InvalidStateException {
        player.getGame().getGameState().returnTile(player);
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // cant return heldTile if you dont have one
        if (player.getHeldTile() == null) throw new InvalidActionException("You can't return a tile if you're not holding one");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
