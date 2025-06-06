package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenSpaceState extends AdventureCardState {
    private final Map<Player, Integer> playerDelta = new HashMap<>();

    public OpenSpaceState(Game game) {
        super(game);
    }

    /**
     * Allows a player to use their propulsors to move forward in open space. The method validates
     * whether it is the player's turn, adjusts the ship's batteries, calculates the propulsion
     * power, and updates the player's position.
     *
     * @param p the player attempting to use their propulsors
     * @param coordinates a list of coordinate positions where batteries are removed from the player's ship
     * @param usedBatteries a list of batteries used for propulsion at corresponding coordinate positions
     * @throws InvalidStateException if the player attempts to play out of turn
     */
    @Override
    public void usePropulsors(Player p, List<Coordinates> coordinates, List<Integer> usedBatteries) throws InvalidStateException {
        if (!p.equals(sortedPlayers.get(currPlayerIdx)))
            throw new InvalidStateException("Player" + p.getName() + " can't play, it's not his turn, player " + sortedPlayers.get(this.currPlayerIdx) + " should play");
        for (int i = 0; i < usedBatteries.size(); i++) {
            p.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
        }
        p.move(p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
        this.addLog("Player " + p.getName() + " moved forward by " + (p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2) + " positions!");
        playerDelta.put(p, p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
        this.played.set(currPlayerIdx, 1);
        this.currPlayerIdx++;
        if (currPlayerIdx == sortedPlayers.size()) {
            flagNoPowerOpenSpace();
            triggerNextState();
        }
    }

    /**
     * Flags players who have no propulsion power in open space, eliminates them from the game and logs their removal.
     */
    private void flagNoPowerOpenSpace() {
        for (Player p : playerDelta.keySet()) {
            if (playerDelta.get(p) == 0) {
                context.getRetiredPlayers().add(p);
                context.getPlayers().remove(p);
                p.setRetired(true);
                this.appendLog("Player " + p.getName() + " is eliminated, he has propulsion power of 0 in open space!");
            }
        }
        playerDelta.clear();
    }

    /**
     * Updates the given view with the current state of the provided game object.
     *
     * @param view      the view instance that should be updated
     * @param toDisplay the game object containing the state to render on the view
     * @throws IOException if an input or output exception occurs during the view update
     */
    @Override
    public void updateView (View view, Game toDisplay) throws IOException {
        view.renderOpenSpaceState(toDisplay);
    }
}
