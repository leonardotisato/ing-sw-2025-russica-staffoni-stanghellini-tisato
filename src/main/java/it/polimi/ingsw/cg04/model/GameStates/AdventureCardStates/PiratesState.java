package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PiratesState extends AdventureCardState {

    private int opponentFirePower;
    private boolean isOpponentDead = false;

    private final int numMeteors;
    private int dice;
    private Direction direction;
    private Attack attack;
    private boolean rolled = false;
    private int currMeteorIdx = 0;
    private final int leftBoundary, rightBoundary;
    private final int upBoundary, downBoundary;


    private final int reward;
    private final int delta;

    // player states: WAIT, ACTIVATE_CANNONS, DECIDE_REWARD, WAIT_FOR_SHOT, USE_BATTERY, FIX_SHIP, DONE
    private final List<Integer> playerStates = new ArrayList<>();
    private final int WAIT = 0;
    private final int ACTIVATE_CANNONS = 1;
    private final int DECIDE_REWARD = 2;
    private final int WAIT_FOR_SHOT = 3;
    private final int PROVIDE_BATTERY = 4;
    private final int CORRECT_SHIP = 5;
    private final int SHOT_DONE = 6;
    private final int DONE = 7;


    public PiratesState(Game game) {
        super(game);

        this.opponentFirePower = card.getFirePower();
        this.reward = card.getEarnedCredits();
        this.delta = card.getDaysLost();
        numMeteors = card.getAttacks().size();

        // set initial states
        for (int i = 0; i < game.getPlayers().size(); i++) {
            playerStates.add(WAIT);
        }
        // first leader must compare his firepower with opponents
        playerStates.set(0, ACTIVATE_CANNONS);

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

    // pre-attack phase
    public void compareFirePower(Player player, List<Coordinates> batteries, List<Coordinates> doubleCannons) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);
        Ship ship = player.getShip();

        if (playerStates.get(playerIdx) == ACTIVATE_CANNONS) {
            double firePower = ship.getBaseFirePower();

            // activate double cannons if playerStates provided them
            if (batteries != null && doubleCannons != null) {
                // remove batteries used
                for (Coordinates c : batteries) {
                    ship.removeBatteries(1, c.getX(), c.getY());
                }

                // increase firePower adding new double cannons based on orientation
                for (Coordinates c : doubleCannons) {
                    // if laser is up bonus is 2
                    if (ship.getTile(c.getX(), c.getY()).getConnection(Direction.UP) == Connection.GUN) {
                        firePower += 2;
                    } else {
                        //else bonus is 1
                        firePower += 1;
                    }
                }
            }

            // now check if player defeated the opponent

            // player has lost
            if (firePower < opponentFirePower) {
                playerStates.set(playerIdx, WAIT_FOR_SHOT);
                this.addLog("Player " + player.getName() + " lost against the pirates!");
                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            // tie, player does not get reward and simply end his turn
            if (firePower == opponentFirePower) {
                playerStates.set(playerIdx, DONE);
                this.addLog("Player " + player.getName() + " drew against the pirates!");
                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            if (firePower > opponentFirePower) {

                // if the player is the first one to defeat the enemy, set its state to DECIDE_REWARD
                if (!isOpponentDead) {
                    playerStates.set(playerIdx, DECIDE_REWARD);
                    isOpponentDead = true;
                    this.addLog("Player " + player.getName() + " won against the pirates!");
                } else {
                    playerStates.set(playerIdx, DONE);
                }

                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            // check if every one is done
            if (isAll(DONE, playerStates)) {
                // transition to next adventure card
                this.addLog("Pirates are not a problem anymore!");
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Compare fire power are not allowed for player: " + player.getName());
        }
    }

    // attack phase
    public void rollDice(Player player) throws InvalidStateException {

        // roll dice must be performed only when everybody left the wait stage
        // roll dice must be performed by the leader of the losers subset
        if (!rolled && isFirstWaitingForShot(player) && !playerStates.contains(WAIT)) {
            dice = context.getBoard().rollDices();
            rolled = true;

            // check if meteor misses completely
            direction = context.getCurrentAdventureCard().getDirection(currMeteorIdx);
            attack = context.getCurrentAdventureCard().getAttack(currMeteorIdx);

            System.out.println("Attack type is: " + attack + " meteor, direction is: " + direction + " dice result is: " + dice);

            // check whether the meteor will hit the ships
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (dice < leftBoundary || dice > rightBoundary) {
                    this.addLog("No ship was hit");
                    triggerNextRound();
                    return;
                } else {
                    dice -= upBoundary - 1;
                }
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                if (dice < upBoundary || dice > downBoundary) {
                    this.addLog("No ship was hit");
                    triggerNextRound();
                    return;
                } else {
                    dice -= leftBoundary - 1;
                }
            }

            // create list that maps player with what he needs to do
            for (int i = 0; i < sortedPlayers.size(); i++) {
                if (playerStates.get(i) == WAIT_FOR_SHOT) {
                    Player p = sortedPlayers.get(i);
                    int hitState = p.getShip().checkHit(direction, attack, dice, "meteor");

                    // if player is safe set its state to done for this attack
                    if (hitState == -1) {
                        addLog("Player: " + p.getName() + " was not hit");
                        playerStates.set(i, SHOT_DONE);
                    }
                    if (hitState == -2) {
                        addLog("Player: " + p.getName() + " used a single cannon!");
                        playerStates.set(i, SHOT_DONE);
                    }

                    // deliver guaranteed hit and check if ship is still legal if not put in correction state "2"
                    if (hitState == 2) {
                        addLog("Player: " + p.getName() + " was hit");
                        p.getShip().handleHit(direction, dice);

                        // if ship is legal, player is done for this attack
                        if (p.getShip().isShipLegal()) {
                            playerStates.set(i, SHOT_DONE);
                        } else {
                            // player needs to correct his ship
                            playerStates.set(i, CORRECT_SHIP);
                        }
                    }

                    // player can decide to use batteries to defend his ship
                    if (hitState == 0 || hitState == 1) {
                        System.out.println("Player: " + p.getName() + " can use a battery to save his ship!");
                        playerStates.set(i, PROVIDE_BATTERY);
                    }
                }
            }

            if (!playerStates.contains(WAIT_FOR_SHOT) && !playerStates.contains(CORRECT_SHIP) && !playerStates.contains(PROVIDE_BATTERY)) {
                triggerNextRound();
            }
        } else {
            throw new InvalidStateException("Roll dices are not allowed for player: " + player.getName());
        }
    }

    public void chooseBattery(Player player, int x, int y) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        // check if player actually needs to fix his ship
        if (rolled && playerStates.get(playerIdx) == PROVIDE_BATTERY) {

            // handle case where player decide to take the hit
            if (x == -1 && y == -1) {
                this.addLog("Player " + player.getName() + " decided to take the hit.");
                player.getShip().handleHit(direction, dice);
                if (!player.getShip().isShipLegal()) {
                    playerStates.set(playerIdx, PROVIDE_BATTERY);
                }
            } else { // player used battery and he is done for the round
                player.getShip().removeBatteries(1, x, y);
                this.addLog("Player " + player.getName() + " used a battery and neutralized the attack");
                playerStates.set(playerIdx, SHOT_DONE);
            }
        } else {
            throw new InvalidStateException("Choose battery are not allowed for player: " + player.getName());
        }

        if (!playerStates.contains(WAIT_FOR_SHOT) && !playerStates.contains(CORRECT_SHIP) && !playerStates.contains(PROVIDE_BATTERY)) {
            triggerNextRound();
        }
    }

    public void fixShip(Player player, List<Coordinates> coordinatesList) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        // check if player actually needs to fix his ship
        if (rolled && played.get(playerIdx) == CORRECT_SHIP) {
            for (Coordinates coordinates : coordinatesList) {
                player.getShip().breakTile(coordinates.getX(), coordinates.getY());
            }
        } else {
            throw new InvalidStateException("Fix ship are not allowed for player: " + player.getName());
        }

        // if fixes make the ship legal player is done for this round
        if (player.getShip().isShipLegal()) {
            this.addLog("Player " + player.getName() + " fixed his ship.");
            played.set(playerIdx, SHOT_DONE);
        }

        if (!playerStates.contains(WAIT_FOR_SHOT) && !playerStates.contains(CORRECT_SHIP) && !playerStates.contains(PROVIDE_BATTERY)) {
            triggerNextRound();
        }
    }

    public void getReward(Player player, boolean acceptReward) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        if (playerStates.get(playerIdx) == DECIDE_REWARD &&
                isOpponentDead &&
                IntStream.range(0, playerStates.size())
                        .filter(i -> i != playerIdx)
                        .allMatch(i -> playerStates.get(i) == DONE)) {
            if (acceptReward) {
                // give reward to winner
                player.updateCredits(reward);
                player.move(-delta);
                playerStates.set(playerIdx, DONE);
            } else {
                // player won but declined reward
                playerStates.set(playerIdx, DONE);
            }

            // check if every one is done
            if (isAll(DONE, playerStates)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Get reward not allowed for player: " + player.getName());
        }
    }

    private void triggerNextRound() {
        rolled = false;
        currMeteorIdx++;

        if (currMeteorIdx >= numMeteors) {
            triggerNextState();
        } else {
            playerStates.replaceAll(state -> state == SHOT_DONE ? WAIT_FOR_SHOT : state);
            System.out.println("New meteor incoming! Current meteor: " + (currMeteorIdx + 1) + " out of " + numMeteors);
        }
    }

    private boolean isAll(int state, List<Integer> list) {
        for (Integer integer : list) {
            if (integer != state) {
                return false;
            }
        }
        return true;
    }

    private boolean isFirstWaitingForShot(Player player) {
        for (int i = 0; i < playerStates.size(); i++) {
            if (playerStates.get(i) == WAIT_FOR_SHOT) {
                return sortedPlayers.get(i).equals(player);
            }
        }
        return false;
    }

    // for testing purposes only!
    public void FORCE_OPPONENT_FIREPOWER(int val) {
        this.opponentFirePower = val;
    }

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder(super.render(playerName));
        stringBuilder.append("\n".repeat(3));
        Player p = context.getPlayer(playerName);
        int playerIdx = p.getRanking() - 1;
        int playerState = playerStates.get(playerIdx);
        if (!rolled && isFirstWaitingForShot(p) && !playerStates.contains(WAIT)){
            stringBuilder.append("You're the first loser! You must roll the dices").append("\n");
        }
        switch (playerState) {
            case ACTIVATE_CANNONS:
                List<Coordinates> lasersCoordinates = p.getShip().getTilesMap().get("LaserTile");
                int totDoubleLasers = (int)lasersCoordinates.stream()
                        .map(coord -> p.getShip().getTile(coord.getX(), coord.getY()))
                        .filter(t -> t.isDoubleLaser())
                        .count();
                stringBuilder.append("Pirates are here! Activate your double cannons and try to defeat them!").append("\n");
                stringBuilder.append("Base fire power of your ship: " + p.getShip().getBaseFirePower()).append("\n");
                stringBuilder.append("Number of double cannons: " + totDoubleLasers).append("\n");
                break;
            case WAIT:
                stringBuilder.append("Pirates are coming! Wait for " + sortedPlayers.get(currPlayerIdx).getName() + " to combat them.").append("\n");
                break;
            case DECIDE_REWARD:
                stringBuilder.append("You won! Decide if you want to earn " + card.getEarnedCredits() + " credits").append("\n");
                stringBuilder.append("Please note that you will lose " + card.getDaysLost() + " days of flight.").append("\n");
                break;
            case WAIT_FOR_SHOT:
                stringBuilder.append("You lost! Wait for the other players to combat the pirates. You will need to defend your ship from the attacks.").append("\n");
                break;
            case PROVIDE_BATTERY:
                stringBuilder.append("You can defend your ship using a battery. Send the battery to neutralize the attack.").append("\n");
                break;
            case CORRECT_SHIP:
                stringBuilder.append("You've been hit! Fix the ship by removing tiles until it becomes legal.").append("\n");
                break;
            case SHOT_DONE:
                stringBuilder.append("You're finished for this ").append(currMeteorIdx == numMeteors ? "card! Wait for the next adventure." : "attack. Wait for the next one.").append("\n");
                break;
            case DONE:
                stringBuilder.append("You're done for this card! Wait for the other players to start the next adventure.").append("\n");
                break;
        }
        return stringBuilder.toString();
    }
}
