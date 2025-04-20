package it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class CompareCrewAction extends PlayerAction {
    private String nickname;

    public CompareCrewAction(String nickname) {
        this.nickname = nickname;
    }

    public void execute(Player player) throws InvalidStateException  {
        GameState state = player.getGame().getGameState();
        state.countCrewMembers(player);
    }

    public boolean checkAction(Player player) { return true; }

    public String getPlayerNickname() { return this.nickname; }
}
