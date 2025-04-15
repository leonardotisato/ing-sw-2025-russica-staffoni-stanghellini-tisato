package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;

public class AbandonedShipState extends AdventureCardState {

    public AbandonedShipState(Game game) {
        super(game);
    }


    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) {
        if (!player.equals(sortedPlayers.get(this.currPlayerIdx))) throw new RuntimeException("Not curr player");
        if (numCrewMembersLost != null && numCrewMembersLost.stream().mapToInt(Integer::intValue).sum() != card.getLostMembers())
            throw new RuntimeException("wrong number of crew members");
        if (numCrewMembersLost == null) {
            played.set(currPlayerIdx, 1);
            currPlayerIdx++;
        } else {
            for (int i = 0; i < numCrewMembersLost.size(); i++) {
                Tile currTile = player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY());
                player.getShip().removeCrew(currTile.getHostedCrewType(), coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
            }
            played.replaceAll(ignored -> 1);
            player.updateCredits(card.getEarnedCredits());
            player.move(-card.getDaysLost());
        }
        if (!played.contains(0)) {
            triggerNextState();
        }
    }
}

