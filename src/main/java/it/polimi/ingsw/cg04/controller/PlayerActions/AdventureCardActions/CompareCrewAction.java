package it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class CompareCrewAction extends PlayerAction {

    public CompareCrewAction(String nickname) {
        super(nickname);
    }

    @Override
    public void execute(Player player) throws InvalidStateException  {
        GameState state = player.getGame().getGameState();
        state.countCrewMembers(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) { return true; }
}
