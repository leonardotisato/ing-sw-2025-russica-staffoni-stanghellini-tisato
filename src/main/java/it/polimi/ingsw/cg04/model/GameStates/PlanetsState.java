package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.*;

public class PlanetsState extends AdventureCardState {
    Map<Player, Integer> chosenPlanets;
    Boolean allPlanetsChosen;
    List<Player> currPlayers;
    public PlanetsState(Game game) {
        super(game);
        this.allPlanetsChosen = false;
        this.chosenPlanets = new HashMap<>();
    }
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
        this.played.set(sortedPlayers.indexOf(player), 1);
        this.currPlayerIdx++;
        if (chosenPlanets.size() == card.getPlanetReward().size() || currPlayerIdx == sortedPlayers.size()) {
            for (int i = sortedPlayers.size() - 1; i >= 0; i--) {
                if (chosenPlanets.containsKey(sortedPlayers.get(i))) {
                    sortedPlayers.get(i).move(-card.getDaysLost());
                }
            }
            triggerNextState();
        }
    }

    public Map<Player, Integer> getChosenPlanets() {
        return chosenPlanets;
    }

    public Boolean getAllPlanetsChosen() {
        return allPlanetsChosen;
    }
    public void setAllPlanetsChosen(Boolean allPlanetsChosen) {
        this.allPlanetsChosen = allPlanetsChosen;
    }

    @Override
    public List<Player> getCurrPlayers() {
        return currPlayers;
    }

    public boolean allPlanetsChosen() {
        return allPlanetsChosen;
    }
}
