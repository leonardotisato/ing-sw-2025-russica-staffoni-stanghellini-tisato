package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class ReturnPileAction extends PlayerAction {
    String playerNickname;

    public ReturnPileAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.returnPile(player);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) {
        //TODO controllare che il player abbia in mano una pila
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
