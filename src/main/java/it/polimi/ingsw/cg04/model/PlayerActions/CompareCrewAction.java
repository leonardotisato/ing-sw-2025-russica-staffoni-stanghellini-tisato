package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;

public class CompareCrewAction implements PlayerAction {
    private String nickname;

    public CompareCrewAction(String nickname) {
        this.nickname = nickname;
    }

    public void execute(Player player) {
        GameState state = player.getGame().getGameState();
        state.countCrewMembers(player);
    }

    public boolean checkAction(Player player) { return true; }

    public String getPlayerNickname() { return this.nickname; }
}
