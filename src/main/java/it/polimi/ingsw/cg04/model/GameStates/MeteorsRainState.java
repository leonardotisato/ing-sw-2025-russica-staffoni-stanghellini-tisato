package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class MeteorsRainState extends AdventureCardState {

    private boolean rolled = false;
    private int currMeteorIdx = 0;
    private final int numMeteors;
    private int dice;
    private Direction direction;
    private Attack attack;

    private final int DONE = 3;
    private final int CORRECT_SHIP = 2;
    private final int PROVIDE_BATTERY = 1;
    private final int INIT = 0;

    private final List<Player> sortedPlayers;

    private final int leftBoundary, rightBoundary;
    private final int upBoundary, downBoundary;

    public MeteorsRainState(Game game) {
        super(game);

        numMeteors = card.getAttacks().size();
        sortedPlayers = game.getPlayers();

        played = new ArrayList<>();
        for (int i = 0; i < sortedPlayers.size(); i++) {
            played.add(INIT);
        }

        upBoundary = 5;
        downBoundary = 9;

        if (game.getLevel() == 2) {
            leftBoundary = 4;
            rightBoundary = 10;
        } else {
            leftBoundary = 5;
            rightBoundary = 9;
        }
    }

    @Override
    public void handleAction(Player player, PlayerAction action) {
        // todo: delete me!
    }


    private boolean isAllDone(List<Integer> list) {
        for (Integer integer : list) {
            if (integer != DONE) {
                return false;
            }
        }
        return true;
    }


    private void triggerNextRound() {
        rolled = false;
        currMeteorIdx++;

        if (currMeteorIdx >= numMeteors) {
            triggerNextState();
        } else {
            played.replaceAll(ignored -> INIT);
            System.out.println("New meteor incoming! Current meteor: " + currMeteorIdx + 1 + " out of " + numMeteors);
        }
    }

    public void rollDice(Player player) {
        if (!rolled && player.equals(sortedPlayers.getFirst())) {
            dice = context.getBoard().rollDices();
            rolled = true;


            // check if meteor misses completely
            direction = context.getCurrentAdventureCard().getDirection(currMeteorIdx);
            attack = context.getCurrentAdventureCard().getAttack(currMeteorIdx);

            System.out.println("Attack type is: " + attack + " meteor, direction is: " + direction + " dice result is: " + dice);

            // check whether the meteor will hit the ships
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (dice < leftBoundary || dice > rightBoundary) {
                    System.out.println("No ship was hit");
                    triggerNextRound();
                    return;
                } else {
                    dice -= upBoundary - 1;
                }
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                if (dice < upBoundary || dice > downBoundary) {
                    System.out.println("No ship was hit");
                    triggerNextRound();
                    return;
                } else {
                    dice -= leftBoundary - 1;
                }
            }

            // create list that maps player with what he needs to do
            for (int i = 0; i < sortedPlayers.size(); i++) {
                Player p = sortedPlayers.get(i);
                int hitState = p.getShip().checkHit(direction, attack, dice, "meteor");

                // if player is safe set its state to done for this attack
                if (hitState == -1) {
                    System.out.println("Player: " + p.getName() + " was not hit");
                    played.set(i, DONE);
                }
                if (hitState == -2) {
                    System.out.println("Player: " + p.getName() + " used a single cannon!");
                    played.set(i, DONE);
                }

                // deliver guaranteed hit and check if ship is still legal if not put in correction state "2"
                if (hitState == 2) {
                    System.out.println("Player: " + p.getName() + " was hit");
                    p.getShip().handleHit(direction, dice);

                    // if ship is legal, player is done for this attack
                    if (p.getShip().isShipLegal()) {
                        played.set(i, DONE);
                    } else {
                        // player needs to correct his ship
                        played.set(i, CORRECT_SHIP);
                    }
                }

                // player can decide to use batteries to defend his ship
                if (hitState == 0 || hitState == 1) {
                    System.out.println("Player: " + p.getName() + " can use a battery to save his ship!");
                    played.set(i, PROVIDE_BATTERY);
                }
            }

            if (isAllDone(played)) {
                triggerNextRound();
            }
        }
    }

    public void fixShip(Player player, List<Coordinates> coordinatesList) {
        int playerIdx = sortedPlayers.indexOf(player);

        // check if player actually needs to fix his ship
        if (rolled && played.get(playerIdx) == CORRECT_SHIP) {
            for (Coordinates coordinates : coordinatesList) {
                player.getShip().breakTile(coordinates.getX(), coordinates.getY());
            }
        }

        // if fixes make the ship legal player is done for this round
        if (player.getShip().isShipLegal()) {
            played.set(playerIdx, DONE);
        }

        if (isAllDone(played)) {
            triggerNextRound();
        }
    }

    public void chooseBattery(Player player, int x, int y) {
        int playerIdx = sortedPlayers.indexOf(player);

        // check if player actually needs to fix his ship
        if (rolled && played.get(playerIdx) == PROVIDE_BATTERY) {

            // handle case where player decide to take the hit
            if (x == -1 && y == -1) {
                player.getShip().handleHit(direction, dice);
                if (!player.getShip().isShipLegal()) {
                    played.set(playerIdx, CORRECT_SHIP);
                }
            } else { // player used battery and he is done for the round
                player.getShip().removeBatteries(1, x, y);
                played.set(playerIdx, DONE);
            }
        }

        if (isAllDone(played)) {
            triggerNextRound();
        }
    }
}
