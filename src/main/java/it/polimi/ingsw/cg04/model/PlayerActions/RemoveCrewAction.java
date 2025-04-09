package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class RemoveCrewAction implements PlayerAction{
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
        if(coordinates == null && numCrewMembersLost == null) return true;
        if (coordinates == null || numCrewMembersLost == null) return false;
        int numberOfCrewLost = numCrewMembersLost.stream().mapToInt( b -> b).sum();
        if(numberOfCrewLost >= player.getShip().getNumCrew()) return false;
        if (numCrewMembersLost.size() != coordinates.size()) return false;
        for (int i = 0; i < numCrewMembersLost.size(); i++) {
            if(!coordinates.get(i).isIn(player.getShip().getTilesMap().get("HousingTile"))){
                return false;
            }
            if(numCrewMembersLost.get(i) > player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY()).getNumCrew()){
                return false;
            }
        }
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return this.nickname;
    }
}

