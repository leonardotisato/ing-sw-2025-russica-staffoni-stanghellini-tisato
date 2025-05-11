package it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class RetireAction extends PlayerAction {
    private final String playerNickname;

    public RetireAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void execute(Player player) throws InvalidStateException {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.retire(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) {
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
