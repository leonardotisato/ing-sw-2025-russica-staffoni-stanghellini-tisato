package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
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
        sortedPlayers = game.getSortedPlayers();

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
        }
    }

    public void rollDice(Player player) throws InvalidStateException {
        if (!rolled && player.equals(sortedPlayers.getFirst())) {
            dice = context.getBoard().rollDices();
            rolled = true;


            // check if meteor misses completely
            direction = context.getCurrentAdventureCard().getDirection(currMeteorIdx);
            attack = context.getCurrentAdventureCard().getAttack(currMeteorIdx);


            System.out.println("Attack type is: " + attack + " meteor, direction is: " + direction + " dice result is: " + dice);

            this.addLog("Attack type is: " + attack + " meteor, direction is: " + direction + " dice result is: " + dice);


            // check whether the meteor will hit the ships
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (dice < leftBoundary || dice > rightBoundary) {
                    this.appendLog("No ship was hit!");
                    triggerNextRound();
                    return;
                } else {
                    // substract offset to find zero-based index to handle hit
                    dice = dice - leftBoundary;
                }
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                if (dice < upBoundary || dice > downBoundary) {
                    this.appendLog("No ship was hit");
                    triggerNextRound();
                    return;
                } else {
                    // substract offset to find zero-based index to handle hit
                    dice = dice - upBoundary;
                }
            }

            System.out.println("0-based dice result is: " + dice);

            // create list that maps player with what he needs to do
            for (int i = 0; i < sortedPlayers.size(); i++) {
                Player p = sortedPlayers.get(i);
                int hitState = p.getShip().checkHit(direction, attack, dice, "meteor");

                // if player is safe set its state to done for this attack
                if (hitState == -1) {
                    this.appendLog("Player: " + p.getName() + " was not hit");
                    played.set(i, DONE);
                }
                if (hitState == -2) {
                    this.appendLog("Player: " + p.getName() + " used a single cannon!");
                    played.set(i, DONE);
                }

                // deliver guaranteed hit and check if ship is still legal if not put in correction state "2"
                if (hitState == 2) {
                    this.appendLog("Player: " + p.getName() + " was hit and did not have a valid protection!");
                    p.getShip().handleHit(direction, dice);

                    // if ship is legal, player is done for this attack
                    if (p.getShip().isShipLegal()) {
                        this.appendLog("Player: " + p.getName() + "'s ship was damaged, but it's still legal!");
                        played.set(i, DONE);
                    } else {
                        // player needs to correct his ship
                        this.appendLog("Player: " + p.getName() + "'s ship is now illegal! Fix it with fixShip!");
                        played.set(i, CORRECT_SHIP);
                    }
                }

                // player can decide to use batteries to defend his ship
                if (hitState == 0 || hitState == 1) {
                    played.set(i, PROVIDE_BATTERY);
                    System.out.println("Player: " + p.getName() + " can use a battery to save his ship!");
                    appendLog("Player: " + p.getName() + " can use a battery to save his ship!");
                }
            }

            if (isAllDone(played)) {
                triggerNextRound();
            }
        } else {
            System.out.println("Rolled status is: " + rolled);
            System.out.println("Player list is " + sortedPlayers.stream().map(Player::getName));
            if (rolled && player.equals(sortedPlayers.getFirst())) {
                throw new InvalidStateException("Wait before rolling the dices again: " + player.getName() + "!");
            }

            throw new InvalidStateException("Roll dices are not allowed for player: " + player.getName());
        }
    }

    public void fixShip(Player player, List<Coordinates> coordinatesList) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        // check if player actually needs to fix his ship
        if (rolled && played.get(playerIdx) == CORRECT_SHIP) {
            for (Coordinates coordinates : coordinatesList) {
                player.getShip().breakTile(coordinates.getX(), coordinates.getY());
            }

            // if fixes make the ship legal player is done for this round
            if (player.getShip().isShipLegal()) {
                played.set(playerIdx, DONE);
                addLog("Player: " + player.getName() + "'s ship was fixed!");
            } else {
                addLog("Player: " + player.getName() + "'s ship is still illegal! Fix it with fixShip!");
            }

            if (isAllDone(played)) {
                triggerNextRound();
            }

        } else {
            throw new InvalidStateException("Fix ship not allowed for player: " + player.getName());
        }
    }

    public void chooseBattery(Player player, int x, int y) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        // check if player actually needs to fix his ship
        if (rolled && played.get(playerIdx) == PROVIDE_BATTERY) {

            // handle case where player decide to take the hit
            if (x == -1 && y == -1) {
                player.getShip().handleHit(direction, dice);
                addLog("Player: " + player.getName() + " handles the hit!");
                if (!player.getShip().isShipLegal()) {
                    played.set(playerIdx, CORRECT_SHIP);
                    appendLog("Player: " + player.getName() + "'s ship is now illegal! Fix it with fixShip!");
                } else {
                    played.set(playerIdx, DONE);
                    appendLog("Player: " + player.getName() + "'s ship was damaged, but it's still legal!");
                }

            } else { // player used battery and he is done for the round
                player.getShip().removeBatteries(1, x, y);
                played.set(playerIdx, DONE);
                addLog("Player: " + player.getName() + " used a battery to save his ship!");
            }
        } else {
            throw new InvalidStateException("Choose battery not allowed for player: " + player.getName());
        }

        if (isAllDone(played)) {
            triggerNextRound();
        }
    }

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder(super.render(playerName));
        stringBuilder.append("\n".repeat(3));
        stringBuilder.append("Meteor " + (currMeteorIdx + 1) + " approaching").append(rolled ? " from "+ dice : "").append("\n");
        Player p = context.getPlayer(playerName);
        if (!rolled) {
        stringBuilder.append(currPlayerIdx == (p.getRanking() - 1) ? "Roll the dices!" : "Wait for " + getSortedPlayers().getFirst().getName() + " to roll the dices!").append("\n");
        }
        else{
            if (played.get(sortedPlayers.indexOf(p)) == PROVIDE_BATTERY) {
                stringBuilder.append("You're about to be hit by a meteor! Send a chooseBattery to save your ship!");
            } else if (played.get(sortedPlayers.indexOf(p)) == CORRECT_SHIP) {
                stringBuilder.append("You've been hit by a meteor! You need to FIX YOUR SHIP!");
            }
            else{
                stringBuilder.append("You're done for this round! Wait for the other players to handle their attacks and fix their ships!");
            }
        }
        return stringBuilder.toString();
    }
}
