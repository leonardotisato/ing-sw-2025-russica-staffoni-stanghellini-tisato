package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class RemoveCrewAction implements PlayerAction {
    List<Integer> numCrewMembersLost;
    List<Coordinates> coordinates;
    String nickname;

    public RemoveCrewAction(String nickname, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) {
        this.coordinates = coordinates;
        this.numCrewMembersLost = numCrewMembersLost;
        this.nickname = nickname;
    }

    public void execute(Player player) {
        GameState state = player.getGame().getGameState();
        state.removeCrew(player, coordinates, numCrewMembersLost);
    }

    public boolean checkAction(Player player) {

        GameState gameState = player.getGame().getGameState();

        // if both null, the player doesn't want to play
        if (coordinates == null && numCrewMembersLost == null) return true;

        //if just one of them is null, it's an error
        if (coordinates == null || numCrewMembersLost == null) return false;

        //number of crew members lost by the player should not be >= crew members on the ship
        int numberOfCrewLost = numCrewMembersLost.stream().mapToInt(b -> b).sum();
        if (numberOfCrewLost >= player.getShip().getNumCrew()) return false;

        //mismatch error
        if (numCrewMembersLost.size() != coordinates.size()) return false;

        // check if coordinates are HousingTile and crew in that HousingTile is enough
        for (int i = 0; i < numCrewMembersLost.size(); i++) {
            //the tile in these coordinates is not a HousingTile
            if (!coordinates.get(i).isIn(player.getShip().getTilesMap().get("HousingTile"))) {
                return false;
            }

            //not enough crew members in this tile!
            if (numCrewMembersLost.get(i) > player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY()).getNumCrew()) {
                return false;
            }
        }

        //the action is legal!
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return this.nickname;
    }
}

