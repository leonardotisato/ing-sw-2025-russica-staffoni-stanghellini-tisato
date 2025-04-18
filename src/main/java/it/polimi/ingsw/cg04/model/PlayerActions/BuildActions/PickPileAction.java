package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class PickPileAction implements PlayerAction {
    String playerNickname;
    int pileIndex;

    public PickPileAction(String playerNickname, int pileIndex) {
        this.playerNickname = playerNickname;
        this.pileIndex = pileIndex;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        player.getGame().getGameState().pickPile(player, pileIndex);
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        if (pileIndex < 0 || pileIndex > 3) throw new InvalidActionException("Invalid pile index!");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
