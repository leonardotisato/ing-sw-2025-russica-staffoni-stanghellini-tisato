package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;

public class RollDiceAction implements PlayerAction {

    private final String playerNick;

    public RollDiceAction(String playerNick) {
        this.playerNick = playerNick;
    }

    @Override
    public void execute(Player player) {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.rollDice(player);
    }

    @Override
    public boolean checkAction(Player player) {
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNick;
    }

    @Override
    public String getType() {
        return "ROLL_DICE";
    }
}
