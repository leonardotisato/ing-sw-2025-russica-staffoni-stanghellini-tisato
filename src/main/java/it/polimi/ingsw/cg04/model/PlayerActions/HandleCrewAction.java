package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class HandleCrewAction implements PlayerAction{
    List<Integer> numCrewMembersLost;
    List<List<Integer>> coordinates;

    public HandleCrewAction(List<List<Integer>> coordinates, List<Integer> numCrewMembersLost) {
        this.coordinates = new ArrayList<>(coordinates);
        this.numCrewMembersLost = new ArrayList<>(numCrewMembersLost);
    }

    public void execute(Player player) {
        if (numCrewMembersLost == null) {
            endAction(player);
            return;
        }
        Tile currTile;
        for (int i = 0; i < numCrewMembersLost.size(); i++) {
            currTile = player.getShip().getTile(coordinates.get(i).get(0), coordinates.get(i).get(1));
            if (currTile instanceof HousingTile) {
                player.getShip().removeCrew(CrewType.HUMAN, coordinates.get(i).get(0), coordinates.get(i).get(1), numCrewMembersLost.get(i));
            }
            else throw new RuntimeException("you can't remove crew members here, not an HousingTile!");
        }
        endAction(player);
    }

    public void endAction(Player player) {
        int position = player.getRanking();
        player.setActivity(0);
        if (numCrewMembersLost == null) {
            if (player.getGame().getPlayers().size() == position) {
                // porto tutti a flight
                player.getGame().getPlayers().stream().
                        forEach(p -> p.setState(ExPlayerState.FLIGHT));
            }
            else {
                player.getGame().getSortedPlayers().get(position + 1).setActivity(1);
            }
        }
        else {
            player.updateCredits(player.getGame().getCurrentAdventureCard().getEarnedCredits());
            player.move(-player.getGame().getCurrentAdventureCard().getDaysLost());
            player.getGame().getPlayers().stream().
                    forEach(p -> p.setState(ExPlayerState.FLIGHT));
        }
    }
}
