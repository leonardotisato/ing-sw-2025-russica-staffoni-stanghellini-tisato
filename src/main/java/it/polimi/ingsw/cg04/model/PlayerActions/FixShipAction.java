package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;

public class FixShipAction implements PlayerAction {

    private final String playerNickname;
    private final List<Coordinates> coordinatesList;


    public FixShipAction(String playerNickname, List<Coordinates> coordinatesList) {
        this.playerNickname = playerNickname;
        this.coordinatesList = coordinatesList;
    }

    public void execute(Player player) {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.fixShip(player, coordinatesList);
    }

    @Override
    public boolean checkAction(Player player) {
        // check that all tiles selected are not null in the ship matrix
        for (Coordinates coordinates : coordinatesList) {

            // if coords is out of bound return false
            if (!player.getShip().isSlotValid(coordinates.getX(), coordinates.getY())) {
                return false;
            }

            if (player.getShip().getTile(coordinates.getX(), coordinates.getY()) == null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
