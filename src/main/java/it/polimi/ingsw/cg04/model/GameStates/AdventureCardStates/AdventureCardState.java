package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.EndGameState;
import it.polimi.ingsw.cg04.model.GameStates.FlightState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.*;

public abstract class AdventureCardState extends GameState {
    protected List<Player> sortedPlayers;
    protected List<Integer> played;
    protected Integer currPlayerIdx;
    protected AdventureCard card;
    protected Game context;
    protected List<Player> currPlayers;


    public AdventureCardState(Game game) {
        this.context = game;
        this.sortedPlayers = game.getSortedPlayers();
        this.card = game.getCurrentAdventureCard();
        this.played = new ArrayList<>(Collections.nCopies(this.sortedPlayers.size(), 0));
        this.currPlayerIdx = 0;
        this.logs = new ArrayList<>();
    }

    public void triggerNextState() {

        // if an adventure ended
        if (card != null) {

            // for each player, check if they need to retire
            for (Player player : context.getSortedPlayers()) {
                String log = context.flagLapped(player);
                log = log + context.flagNoHumans(player);
                if (!log.isEmpty()) this.appendLog(log);
            }

            if (context.getPlayers().isEmpty()) {
                // end game since all players are retired
                this.appendLog("No player can continue the game. They are all either retired or disconnected.");
                context.setGameState(new EndGameState(context));
                context.setCurrentAdventureCard(null);
                context.handleEndGame();
            } else {
                // game continues
                System.out.println(card.getType() + " è stata risolta!");
                this.appendLog(card.getType() + " has been solved!");
                context.setGameState(new FlightState(context));
                context.setCurrentAdventureCard(null);
            }

        }
        if (getContext().getAdventureCardsDeck().isEmpty()) {
            // the game ended since there are no more adv cards
            context.setGameState(new EndGameState(context));
            context.setCurrentAdventureCard(null);
            context.handleEndGame();
        } else {
            // game continues
            context.setGameState(new FlightState(context));
            context.setCurrentAdventureCard(null);
        }
    }

    public List<Player> getSortedPlayers() {
        return sortedPlayers;
    }

    public List<Integer> getPlayed() {
        return played;
    }

    public Integer getCurrPlayerIdx() {
        return currPlayerIdx;
    }

    public AdventureCard getCard() {
        return card;
    }

    public Game getContext() {
        return context;
    }

    public void setSortedPlayers(List<Player> sortedPlayers) {
        this.sortedPlayers = sortedPlayers;
    }

    public void setPlayed(List<Integer> played) {
        this.played = played;
    }

    // todo: handle this unused method
    public void setCurrPlayerIdx(Integer currPlayerIdx) {
        this.currPlayerIdx = currPlayerIdx;
    }

    public void setCard(AdventureCard card) {
        this.card = card;
    }

    // todo: handle this unused method
    public void setContext(Game context) {
        this.context = context;
    }

    public boolean checkAction(Player player) {
        return true;
    }

    public Map<Player, Integer> getChosenPlanets() {
        return null;
    }

    // todo: handle this unused method
    public Boolean getAllPlanetsChosen() {
        return false;
    }

    // todo: handle this unused method
    public void setAllPlanetsChosen(Boolean allPlanetsChosen) {
        return;
    }

    // todo: handle this unused method
    public void setPlayerToBeMovedIdx(Integer playerToBeMovedIdx) {
        return;
    }

    // todo: handle this unused method
    public List<Player> getCurrPlayers() {
        return currPlayers;
    }
}
