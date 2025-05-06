package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;

import java.util.*;
import java.util.stream.Collectors;

public class FlightBoardLev2 extends FlightBoard {

    private final int MAX_FLIPS = 3;
    private int timerFlipsUsed = 0;
    private long timerEndTime;

    public FlightBoardLev2(){
        this.path = new Player[24];
        this.pathSize = 24;

        this.startingPosition = new HashMap<>();
        this.startingPosition.put(1, 6);
        this.startingPosition.put(2, 3);
        this.startingPosition.put(3, 1);
        this.startingPosition.put(4, 0);

        this.endGameCredits = new HashMap<>();
        this.endGameCredits.put(1, 8);
        this.endGameCredits.put(2, 6);
        this.endGameCredits.put(3, 4);
        this.endGameCredits.put(4, 2);

        this.mostBeautifulShipCredits = 4;
    }

    public void startTimer() {
        long TIMER_DURATION = 5000;
        timerEndTime = System.currentTimeMillis() + TIMER_DURATION;
    }

    public boolean isTimerExpired() {
        return System.currentTimeMillis() >= timerEndTime;
    }

    public boolean flipTimer() {
        if (isTimerExpired() && timerFlipsUsed < MAX_FLIPS) {
            timerFlipsUsed++;
            startTimer();
            return true;
        }
        return false;
    }

    public int getTimerFlipsUsed() {
        return timerFlipsUsed;
    }

    public int getTimerFlipsRemaining() {
        return MAX_FLIPS - timerFlipsUsed;
    }

    public long getRemainingTime() {
        long remainingTime = timerEndTime - System.currentTimeMillis();
        return Math.max(0, remainingTime);
    }

    public List<Integer> createAdventureCardsDeck(Game game) {
        createPreFlightPiles(game);
        List<Integer> adventureCardsDeck = new ArrayList<>();
        for (List<Integer> pile : game.getPreFlightPiles()) {
            adventureCardsDeck.addAll(pile);
        }
        Collections.shuffle(adventureCardsDeck, rand);
        return adventureCardsDeck;
    }

    public void createPreFlightPiles(Game game) {
        Map<Integer, AdventureCard> cardMap = game.getAdventureCardsMap();

        for (int i = 0; i < 4; i++) {
            List<Integer> level2Keys = cardMap.entrySet().stream()
                    .filter(entry -> entry.getValue().getCardLevel() == 2)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            List<Integer> level1Keys = cardMap.entrySet().stream()
                    .filter(entry -> entry.getValue().getCardLevel() == 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            Collections.shuffle(level2Keys, rand);
            Collections.shuffle(level1Keys, rand);

            List<Integer> selectedKeys = new ArrayList<>();
            selectedKeys.add(level2Keys.get(0));
            selectedKeys.add(level2Keys.get(1));
            selectedKeys.add(level1Keys.get(0));

            game.getPreFlightPiles().get(i).addAll(selectedKeys);
        }
    }
}
