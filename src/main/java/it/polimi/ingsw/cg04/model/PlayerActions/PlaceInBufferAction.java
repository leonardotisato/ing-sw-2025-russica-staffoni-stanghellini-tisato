package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

public class PlaceInBufferAction implements PlayerAction {
    private final String playerNickname;

    public PlaceInBufferAction(String playerNickname) {
        this.playerNickname = playerNickname;
    }
    public void execute(Player player) {
        Game game = player.getGame();
        game.getGameState().placeInBuffer(player);
    }

    @Override
    public boolean checkAction(Player player) {
        // check if player is holding a tile
        if(player.getHeldTile()==null) {
            return false;
        }

        // check if buffer is already full
        if(player.getShip().getTilesBuffer().size() >= 2) {
            return false;
        }

        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public String getType() {
        return "PLACE_IN_BUFFER";
    }
}
