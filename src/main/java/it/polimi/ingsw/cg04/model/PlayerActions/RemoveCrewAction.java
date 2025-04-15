package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
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

    public boolean checkAction(Player player) throws InvalidActionException{

        GameState gameState = player.getGame().getGameState();

        // if both null, the player doesn't want to play
        if (coordinates == null && numCrewMembersLost == null) return true;

        //if just one of them is null, it's an error
        if (coordinates == null || numCrewMembersLost == null) throw new InvalidActionException("Wrong inputs format!");

        //number of crew members lost by the player should not be >= crew members on the ship
        int numberOfCrewLost = numCrewMembersLost.stream().mapToInt(b -> b).sum();
        if (numberOfCrewLost > player.getShip().getNumCrew()) throw new InvalidActionException("You don't have enough crew members in your ship!");

        //mismatch error
        if (numCrewMembersLost.size() != coordinates.size()) throw new InvalidActionException("numCrewMembersLost size does not match coordinates size!");

        // check if coordinates are HousingTile and crew in that HousingTile is enough
        for (int i = 0; i < numCrewMembersLost.size(); i++) {
            //the tile in these coordinates is not a HousingTile
            if (!coordinates.get(i).isIn(player.getShip().getTilesMap().get("HousingTile"))) {
                throw new InvalidActionException("Tile in " + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + " is not a HousingTile!");
            }

            //not enough crew members in this tile!
            if (numCrewMembersLost.get(i) > player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY()).getNumCrew()) {
                throw new InvalidActionException("HousingTile in " + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + " has not enough crew members!");
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

