package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class PickPileAction extends PlayerAction {
    int pileIndex;

    public PickPileAction(String playerNickname, int pileIndex) {
        super(playerNickname);
        this.pileIndex = pileIndex;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.pickPile(player, pileIndex);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        if (pileIndex < 0 || pileIndex > 3) throw new InvalidActionException("Invalid pile index!");
        return true;
    }
}
