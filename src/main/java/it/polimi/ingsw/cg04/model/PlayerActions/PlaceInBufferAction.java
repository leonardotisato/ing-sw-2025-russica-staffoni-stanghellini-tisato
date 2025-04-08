package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class PlaceInBufferAction implements PlayerAction {
    public void execute(Player player) {}

    @Override
    public boolean checkAction(Player player) {
        return false;
    }

    @Override
    public String getPlayerNickname() {
        return "";
    }

    @Override
    public String getType() {
        return "PLACE_IN_BUFFER";
    }
}
