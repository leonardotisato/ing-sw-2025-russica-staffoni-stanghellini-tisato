package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;

public class LoadCrewState extends AdventureCardState {

    public LoadCrewState(Game game) {
        super(game);
    }

    /**
     * Handles the process of loading the crew onto a player's ship. Allows the
     * player to assign pink and/or brown aliens to specific ship coordinates.
     *
     * @param player           The player attempting to load the crew.
     * @param pinkAlienCoords  The coordinates where the pink alien is to be loaded,
     *                         or null if no pink alien is being assigned.
     * @param brownAlienCoords The coordinates where the brown alien is to be loaded,
     *                         or null if no brown alien is being assigned.
     * @throws InvalidStateException If the action is performed by a player out of turn.
     */
    @Override
    public void loadCrew(Player player, Coordinates pinkAlienCoords, Coordinates brownAlienCoords) throws InvalidStateException {
        if (sortedPlayers.get(currPlayerIdx).equals(player)) {

            Ship ship = player.getShip();

            // fill selected coordinates with alien
            if (pinkAlienCoords != null) {
                ship.removeCrew(CrewType.HUMAN, pinkAlienCoords.getX(), pinkAlienCoords.getY(), 2);
                ship.addCrew(CrewType.PINK_ALIEN, pinkAlienCoords.getX(), pinkAlienCoords.getY());
                this.addLog(player.getName() + " added a pink alien to his ship!");
            }
            if (brownAlienCoords != null) {
                ship.removeCrew(CrewType.HUMAN, brownAlienCoords.getX(), brownAlienCoords.getY(), 2);
                ship.addCrew(CrewType.BROWN_ALIEN, brownAlienCoords.getX(), brownAlienCoords.getY());
                this.addLog(player.getName() + " added a brown alien to his ship!");
            }

            currPlayerIdx++;

            // when all player loaded their ships, the flight can begin
            if (currPlayerIdx == sortedPlayers.size()) {
                this.addLog("All the players loaded the crew. It's time to start!");
                triggerNextState();
            }

        } else {
            throw new InvalidStateException("Load crew not allowed for player: " + player.getName());
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
        view.renderLoadCrewState(toDisplay);
    }
}

