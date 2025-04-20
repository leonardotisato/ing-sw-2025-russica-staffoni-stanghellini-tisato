package it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class PlaceInBufferAction extends PlayerAction {
    private final String playerNickname;

    public PlaceInBufferAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    @Override
    public void execute(Player player) throws InvalidStateException {
        Game game = player.getGame();
        game.getGameState().placeInBuffer(player);
    }

    @Override
    public boolean checkAction(Player player) throws InvalidActionException {
        // check if player is holding a tile
        if(player.getHeldTile()==null) {
            throw new InvalidActionException("You are not holding a tile!");
        }

        // check if buffer is already full
        if (player.getShip().getTilesBuffer().size() == 2) throw new InvalidActionException("Buffer is full!");
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
