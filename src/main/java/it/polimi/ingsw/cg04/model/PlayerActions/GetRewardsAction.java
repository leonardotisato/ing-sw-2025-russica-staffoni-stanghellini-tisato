package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;

public class GetRewardsAction implements PlayerAction {

    private final String playerNickname;
    private final boolean acceptReward;

    public GetRewardsAction(String playerNickname, boolean acceptReward) {
        this.playerNickname = playerNickname;
        this.acceptReward = acceptReward;
    }

    public void execute(Player player) {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.getReward(player, acceptReward);
    }

    @Override
    public boolean checkAction(Player player) {
        // no parameters check?
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }

}
