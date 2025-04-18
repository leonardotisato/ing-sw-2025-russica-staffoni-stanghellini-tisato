package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class ReturnPileAction implements PlayerAction {
    String playerNickname;

    public ReturnPileAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void execute(Player player) throws InvalidStateException {
        player.getGame().getGameState().returnPile(player);
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
