package it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class GetNextAdventureCardAction extends PlayerAction {
    public GetNextAdventureCardAction (String nickname) {
        super(nickname);
    }
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.getNextAdventureCard(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        if (player.getGame().getCurrentAdventureCard() != null) throw new InvalidActionException("It's not time to get the next adventure card");
        return true;
    }
}
