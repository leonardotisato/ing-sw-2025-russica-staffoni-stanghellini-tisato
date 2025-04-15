package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

public class DrawFaceDownAction implements PlayerAction {
    String playerNickname;

    public DrawFaceDownAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void execute(Player player) {
        player.getGame().getGameState().drawFaceDown(player);
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // player is already holding a tile
        if (player.getHeldTile() != null) throw new InvalidActionException("Already holding a tile!");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
