package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;

public class EpidemicAction implements PlayerAction {
    private final String nickname;
    public EpidemicAction(String nickname) {
        this.nickname = nickname;
    }

    public void execute(Player player) {
        GameState state = player.getGame().getGameState();
        state.spreadEpidemic(player);
    }

    @Override
    public boolean checkAction(Player player) {
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return this.nickname;
    }
}
