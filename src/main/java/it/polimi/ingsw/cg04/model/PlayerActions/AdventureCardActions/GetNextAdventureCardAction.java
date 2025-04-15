package it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

public class GetNextAdventureCardAction implements PlayerAction {
    private String nickname;
    public GetNextAdventureCardAction (String nickname) {
        this.nickname = nickname;
    }
    public void execute(Player player) {
        GameState state = player.getGame().getGameState();
        state.getNextAdventureCard(player);
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        if (player.getGame().getCurrentAdventureCard() != null) throw new InvalidActionException("It's not time to get the next adventure card");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return "";
    }

}
