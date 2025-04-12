package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public class EndBuildingAction implements PlayerAction {
    String playerNickname;
    int position;
    public EndBuildingAction(String playerNickname, int position) {
        this.playerNickname = playerNickname;
        this.position = position;
    }

    @Override
    public void execute(Player player) {
        player.getGame().getGameState().endBuilding(player, position);
    }

    @Override
    public boolean checkAction(Player player) {
        return position >= 1 && position <= 4;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
