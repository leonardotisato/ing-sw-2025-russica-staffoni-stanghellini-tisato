package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
public class StartTimerAction implements PlayerAction {
    String playerNickname;

    public StartTimerAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }
    @Override
    public void execute(Player player) {
        player.getGame().getGameState().startTimer(player);
    }

    @Override
    public boolean checkAction(Player player) {
        return player.getGame().getBoard().isTimerExpired();
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
