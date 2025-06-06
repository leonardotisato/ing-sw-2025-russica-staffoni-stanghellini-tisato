package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.util.List;

public class AbandonedShipState extends AdventureCardState {

    public AbandonedShipState(Game game) {
        super(game);
    }


    /**
     * Handles the removal of crew members from a player's ship during the Abandoned Ship event state.
     *
     * @param player             the player attempting to remove crew members
     * @param coordinates        a list of coordinates specifying the positions of the crew members to be removed
     * @param numCrewMembersLost a list of integers representing the number of crew members lost per specified coordinate
     * @throws InvalidStateException if the action is invalid due to turn order, incorrect crew count, or other game rules
     */
    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) throws InvalidStateException {
        if (!player.equals(sortedPlayers.get(this.currPlayerIdx)))
            throw new InvalidStateException("Player" + player.getName() + " can't play, it's not his turn, player " + sortedPlayers.get(this.currPlayerIdx) + " should play");
        if (numCrewMembersLost != null && numCrewMembersLost.stream().mapToInt(Integer::intValue).sum() != card.getLostMembers())
            throw new InvalidStateException("The number of crew sent by Player " + player.getName() + " is incorrect for this card, he should send " + card.getLostMembers() + " crew members");
        if (numCrewMembersLost == null) {
            this.addLog("Player " + player.getName() + " decided not to play this card.");
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
            this.addLog("Player " + player.getName() + " decided to play this card. He earned " + card.getEarnedCredits() + " credits. He lost " + card.getLostMembers() + " crew members and " + card.getDaysLost() + " flight days.");
        }
        if (!played.contains(0)) {
            triggerNextState();
        }
    }

    /**
     * Updates the given view with the current state of the provided game object.
     *
     * @param view      the view instance that should be updated
     * @param toDisplay the game object containing the state to render on the view
     * @throws IOException if an input or output exception occurs during the view update
     */
    @Override
    public void updateView(View view, Game toDisplay) throws IOException {
        view.renderAbandonedShipState(toDisplay);
    }

}

