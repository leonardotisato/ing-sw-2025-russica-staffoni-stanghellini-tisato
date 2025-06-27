package it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class GetRewardsAction extends PlayerAction {
    private final boolean acceptReward;

    public GetRewardsAction(String playerNickname, boolean acceptReward) {
        super(playerNickname);
        this.acceptReward = acceptReward;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.getReward(player, acceptReward);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) {
        return true;
    }
}
