package it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public class PropulsorComparisonAction extends PlayerAction {
    public void execute(Player player) {}

    @Override
    public boolean checkAction(Player player) {
        return false;
    }

    @Override
    public String getPlayerNickname() {
        return "";
    }
}

