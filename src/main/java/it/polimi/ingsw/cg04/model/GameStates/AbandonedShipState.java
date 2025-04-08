package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleCrewAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbandonedShipState extends AdventureCardState {
    public AbandonedShipState(Game game) {
        super(game);
    }
    public void handleAction(Player player, PlayerAction action) {
        return;
    }

    public void handleCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost){
        if (!player.equals(sortedPlayers.get(this.currPlayerIdx))) throw new RuntimeException("Not curr player");
        if (numCrewMembersLost.isEmpty()) {
            played.set(currPlayerIdx, 1);
            currPlayerIdx++;
        }
        else {
            for (int i = 0; i < numCrewMembersLost.size(); i++) {
                player.getShip().removeCrew(CrewType.HUMAN, coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
            }
            played.replaceAll(ignored -> 1);
            player.updateCredits(card.getEarnedCredits());
            player.move(-card.getDaysLost());
        }
        if (!played.contains(0)){
            triggerNextState();
        }
        return;
    }
}

