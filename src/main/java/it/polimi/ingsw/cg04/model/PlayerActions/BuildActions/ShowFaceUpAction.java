package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class ShowFaceUpAction implements PlayerAction {
    String playerNickname;

    public ShowFaceUpAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        player.getGame().getGameState().showFaceUp(player);
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        //todo: is this check correct??
        if (player.getHeldTile() != null) throw new InvalidActionException("Player already holding a tile!");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}