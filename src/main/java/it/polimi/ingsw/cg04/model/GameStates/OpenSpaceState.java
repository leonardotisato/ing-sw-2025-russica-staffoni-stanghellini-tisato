package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.ChoosePropuslsorAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.List;

public class OpenSpaceState extends AdventureCardState {

    public OpenSpaceState(Game game) {
        super(game);
    }
    public void handleAction(Player player, PlayerAction action) {
        if (player.equals(sortedPlayers.get(currPlayerIdx))) {
            action.execute(player);
            this.played.set(currPlayerIdx, 1);
            this.currPlayerIdx++;
            if (currPlayerIdx == sortedPlayers.size()) {
                triggerNextState();
            }
        } else {
            throw new RuntimeException("Not your turn!");
        }
    }

    @Override
    public void triggerNextState() {
        context.setGameState(new FlightState());
        context.setCurrentAdventureCard(null);
    }
}
