package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.EndGameState;
import it.polimi.ingsw.cg04.model.GameStates.FlightState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.util.*;
import java.util.stream.Stream;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.buildRightPanel;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

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
        if(card != null) {
            context.flagLapped();
            context.flagNoHumans();
            System.out.println(card.getType() + " è stata risolta!");
        }
        if(getContext().getAdventureCardsDeck().isEmpty()) {
            context.setGameState(new EndGameState(context));
            context.setCurrentAdventureCard(null);
            context.handleEndGame();
        }
        else {
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

    public void setCurrPlayerIdx(Integer currPlayerIdx) {
        this.currPlayerIdx = currPlayerIdx;
    }

    public void setCard(AdventureCard card) {
        this.card = card;
    }

    public void setContext(Game context) {
        this.context = context;
    }

    public boolean checkAction(Player player) {
        return true;
    }

    public Map<Player, Integer> getChosenPlanets() {
        return null;
    }

    public Boolean getAllPlanetsChosen() {
        return false;
    }

    public void setAllPlanetsChosen(Boolean allPlanetsChosen) {
        return;
    }

    public void setPlayerToBeMovedIdx(Integer playerToBeMovedIdx) {
        return;
    }

    public List<Player> getCurrPlayers() {
        return currPlayers;
    }

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(TuiDrawer.renderPlayersByColumn(context.getSortedPlayers()));
        System.out.println("Your ship:");
        stringBuilder.append(context.getPlayer(playerName).getShip().draw());
        List<String> shipPanel = toLines(stringBuilder.toString());
        List<String> flightBoardPanel = toLines(context.getBoard().draw());
        List<String> adventureCardPanel = toLines(card.draw());
        int totalH = shipPanel.size();
        int rightWidth = Stream.concat(flightBoardPanel.stream(), adventureCardPanel.stream())
                .mapToInt(String::length)
                .max()
                .orElse(0);                  // quanto spazio assegni alla colonna destra
        List<String> rightPanel = buildRightPanel(flightBoardPanel, adventureCardPanel, totalH, rightWidth);
        return TuiDrawer.renderTwoColumnLayout(shipPanel, rightPanel, 40);
    }
}
