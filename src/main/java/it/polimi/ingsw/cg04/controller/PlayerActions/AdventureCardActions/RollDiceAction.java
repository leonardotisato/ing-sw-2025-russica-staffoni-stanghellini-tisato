package it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class RollDiceAction extends PlayerAction {

    public RollDiceAction(String playerNick) {
        super(playerNick);
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.rollDice(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) {
        return true;
    }
}
