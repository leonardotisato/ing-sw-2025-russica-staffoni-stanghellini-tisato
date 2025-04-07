package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleBoxesAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.ArrayList;
import java.util.List;

public class AbandonedStationState extends AdventureCardState {
    private Integer playerToBeMovedIdx;
    public AbandonedStationState(Game game) {
        super(game);
        playerToBeMovedIdx = null;
        currPlayers = new ArrayList<>();
        currPlayers.add(sortedPlayers.getFirst());
    }
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
        played.set(currPlayerIdx, 1);
        if (playerToBeMovedIdx != null) {
            sortedPlayers.get(playerToBeMovedIdx).move(-card.getDaysLost());
            triggerNextState();
            return;
        }
        currPlayerIdx++;
        if (currPlayerIdx == sortedPlayers.size()) {
            triggerNextState();
        }
        else currPlayers.set(0, sortedPlayers.get(currPlayerIdx));
    }

    public boolean checkAction(Player player) {
        return player.getShip().getNumCrew() > getCard().getMembersNeeded();
    }

    public void setPlayerToBeMovedIdx(Integer playerToBeMovedIdx) {
        this.playerToBeMovedIdx = playerToBeMovedIdx;
    }
}
