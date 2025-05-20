package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class RotateAction extends PlayerAction {

    private final String type;

    public RotateAction(String playerNickname, String type) {
        super(playerNickname);
        this.type = type;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.rotateTile(player, type);
        this.addLogs(state.getLogs());
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {

        if (player.getHeldTile() == null) {
            throw new InvalidActionException("Player not holding a tile!");
        }

        return true;
    }
}
