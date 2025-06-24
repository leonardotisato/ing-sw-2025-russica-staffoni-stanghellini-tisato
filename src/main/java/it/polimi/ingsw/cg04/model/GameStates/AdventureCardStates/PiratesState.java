package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.ViewController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static it.polimi.ingsw.cg04.model.enumerations.CrewType.PINK_ALIEN;

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

    /**
     * Retrieves the index of the current meteor.
     *
     * @return the index of the current meteor as an integer
     */
    public int getCurrMeteorIdx() {
        return currMeteorIdx;
    }

    /**
     * Retrieves the result of the dice roll.
     *
     * @return the result of the dice roll as an integer
     */
    public int getDiceResult() {
        return dice;
    }

    /**
     * Determines whether the dice has been rolled in the current state.
     *
     * @return true if the dice has been rolled, false otherwise
     */
    public boolean isRolled() {
        return rolled;
    }

    /**
     * Retrieves the list of player states.
     *
     * @return a list of integers representing the current states of the players
     */
    public List<Integer> getPlayerStates() {
        return playerStates;
    }

    // pre-attack phase

    /**
     * Compares the firepower of the given player with the opponent's firepower and updates
     * the game state accordingly based on the comparison result.
     *
     * @param player        the player whose firepower is being compared
     * @param batteries     a list of battery coordinates to be used by the player; can be null
     * @param doubleCannons a list of double cannon coordinates to be used by the player; can be null
     * @throws InvalidStateException if the action is not allowed for the given player in their current state
     */
    @Override
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

            // apply conditional alien bonus
            if (firePower > 0 && ship.getNumCrewByType(CrewType.PINK_ALIEN) == 1) {
                firePower += 2;
                this.addLog("Player " + player.getName() + " has a pink alien and gets bonus firepower!");
            }

            this.addLog("Player " + player.getName() + " total firepower is: " + firePower);

            // now check if player defeated the opponent

            // player has lost
            if (firePower < opponentFirePower) {
                playerStates.set(playerIdx, WAIT_FOR_SHOT);
                this.appendLog("Player " + player.getName() + " lost against the pirates!");
                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            // tie, player does not get reward and simply end his turn
            if (firePower == opponentFirePower) {
                playerStates.set(playerIdx, DONE);
                this.appendLog("Player " + player.getName() + " drew against the pirates!");
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
                    this.appendLog("Player " + player.getName() + " won against the pirates!");
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
                this.appendLog("Pirates are not a problem anymore!");
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Compare fire power are not allowed for player: " + player.getName());
        }
    }

    // attack phase

    /**
     * Executes the roll dice action during the attack phase for a specified player.
     * It determines the outcome of the dice roll and checks whether the meteor attack hits any ships, updating player
     * states accordingly.
     *
     * @param player the player attempting to roll the dice during the attack phase
     * @throws InvalidStateException if the dice roll is attempted in an invalid state
     *                               or by an unauthorized player
     */
    @Override
    public void rollDice(Player player) throws InvalidStateException {

        // roll dice must be performed only when everybody left the wait stage
        // roll dice must be performed by the leader of the losers subset
        if (!rolled && isFirstWaitingForShot(player) && !playerStates.contains(ACTIVATE_CANNONS)) {
            dice = context.getBoard().rollDices();
            rolled = true;

            // check if meteor misses completely
            direction = context.getCurrentAdventureCard().getDirection(currMeteorIdx);
            attack = context.getCurrentAdventureCard().getAttack(currMeteorIdx);

            System.out.println("Attack type is: " + attack + " shot, direction is: " + direction + " dice result is: " + dice);
            this.addLog("Attack type is: " + attack + " shot, direction is: " + direction + " dice result is: " + dice);

            // check whether the meteor will hit the ships
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (dice < leftBoundary || dice > rightBoundary) {
                    this.appendLog("No ship was hit");
                    triggerNextRound();
                    return;
                } else {
                    // subtract offset to find zero-based index to handle hit
                    dice = dice - leftBoundary;
                }
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                if (dice < upBoundary || dice > downBoundary) {
                    this.appendLog("No ship was hit");
                    triggerNextRound();
                    return;
                } else {
                    // subtract offset to find zero-based index to handle hit
                    dice = dice - upBoundary;
                }
            }

            // create list that maps player with what he needs to do
            for (int i = 0; i < sortedPlayers.size(); i++) {
                if (playerStates.get(i) == WAIT_FOR_SHOT) {
                    Player p = sortedPlayers.get(i);

                    int hitState = p.getShip().checkHit(direction, attack, dice, "shot");

                    // if player is safe set its state to done for this attack
                    if (hitState == -1) {
                        this.appendLog("Player: " + p.getName() + " was not hit");
                        playerStates.set(i, SHOT_DONE);
                    }
                    // deliver guaranteed hit and check if ship is still legal if not put in correction state "2"
                    else if (hitState == 2) {
                        this.appendLog("Player " + p.getName() + " was hit");
                        p.getShip().handleHit(direction, dice);

                        // if ship is legal, player is done for this attack
                        if (p.getShip().isShipLegal()) {
                            playerStates.set(i, SHOT_DONE);
                            triggerNextRound();
                        } else {
                            // player needs to correct his ship
                            this.appendLog("Player " + p.getName() + " must fix his ship.");
                            playerStates.set(i, CORRECT_SHIP);
                        }
                    }
                    // player can decide to use batteries to defend his ship
                    else if (hitState == 0) {
                        this.appendLog("Player: " + p.getName() + " can use a battery to save his ship!");
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

    /**
     * Allows the player to choose whether to neutralize an attack using a battery or to take the hit.
     *
     * @param player the player making the choice
     * @param x      the x-coordinate of the battery to use, or -1 if the player decides to take the hit
     * @param y      the y-coordinate of the battery to use, or -1 if the player decides to take the hit
     * @throws InvalidStateException if the player is not in a state where they can choose a battery
     */
    @Override
    public void chooseBattery(Player player, int x, int y) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        // check if player actually needs to fix his ship
        if (rolled && playerStates.get(playerIdx) == PROVIDE_BATTERY) {

            // handle case where player decide to take the hit
            if (x == -1 && y == -1) {
                this.addLog("Player " + player.getName() + " decided to take the hit.");
                player.getShip().handleHit(direction, dice);
                if (!player.getShip().isShipLegal()) {
                    this.appendLog("Player " + player.getName() + " was hit and his ship is now illegal. He must fix it.");
                    playerStates.set(playerIdx, CORRECT_SHIP);
                } else {
                    this.appendLog("Player " + player.getName() + " was hit but his ship is still legal.");
                    playerStates.set(playerIdx, SHOT_DONE);
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

    /**
     * Fixes the ship of the given player by repairing the specified tiles.
     * This method checks if the player is allowed to perform the fix operation
     * and updates the game state based on the legality of the ship after the fix.
     *
     * @param player          the player who is attempting to fix their ship
     * @param coordinatesList a list of coordinates representing the ship tiles to be repaired
     * @throws InvalidStateException if the player is not allowed to fix their ship at this time
     */
    @Override
    public void fixShip(Player player, List<Coordinates> coordinatesList) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        // check if player actually needs to fix his ship
        if (rolled && playerStates.get(playerIdx) == CORRECT_SHIP) {
            for (Coordinates coordinates : coordinatesList) {
                player.getShip().breakTile(coordinates.getX(), coordinates.getY());
            }

            // if fixes make the ship legal player is done for this round
            if (player.getShip().isShipLegal()) {
                this.addLog("Player " + player.getName() + " fixed his ship.");
                playerStates.set(playerIdx, SHOT_DONE);
            } else {
                this.addLog("Player: " + player.getName() + " ship is still illegal and he must fix it.");
            }

            if (!playerStates.contains(WAIT_FOR_SHOT) && !playerStates.contains(CORRECT_SHIP) && !playerStates.contains(PROVIDE_BATTERY)) {
                triggerNextRound();
            }

        } else {
            throw new InvalidStateException("Fix ship is not allowed for player: " + player.getName());
        }
    }

    /**
     * Allows the specified player to decide whether to accept or decline a reward
     * after defeating an opponent, provided the game state allows this action.
     * If the player accepts the reward, their in-game credits and position are
     * updated.
     *
     * @param player       the player who is deciding whether to accept the reward
     * @param acceptReward true if the player accepts the reward, false if they decline
     * @throws InvalidStateException if the player is not in a valid state to receive the reward,
     *                               or if the action is not allowed
     */
    @Override
    public void getReward(Player player, boolean acceptReward) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        if (playerStates.get(playerIdx) == DECIDE_REWARD &&
                isOpponentDead &&
                IntStream.range(0, playerStates.size())
                        .filter(i -> i != playerIdx)
                        .allMatch(i -> playerStates.get(i) == DONE)) {
            if (acceptReward) {
                // give reward to the winner
                this.addLog("Player " + player.getName() + " got his rewards after defeating pirates.");
                this.appendLog("Nobody else can receive the reward now. But you can still lose ;).");
                player.updateCredits(reward);
                player.move(-delta);
                playerStates.set(playerIdx, DONE);
            } else {
                // player won but declined reward
                playerStates.set(playerIdx, DONE);
            }

            // check if everyone is done
            if (isAll(DONE, playerStates)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Get reward not allowed for player: " + player.getName());
        }
    }

    /**
     * Advances the game to the next round by updating the current meteor index and player states.
     * If all meteors have been addressed, transitions the game to the next state.
     */
    private void triggerNextRound() {
        rolled = false;
        currMeteorIdx++;

        if (currMeteorIdx >= numMeteors) {
            triggerNextState();
        } else {
            playerStates.replaceAll(state -> state == SHOT_DONE ? WAIT_FOR_SHOT : state);
            this.appendLog("New meteor incoming! Current meteor: " + (currMeteorIdx + 1) + " out of " + numMeteors);
        }
    }

    /**
     * Determines whether all elements in the given list are equal to the provided state.
     *
     * @param state the integer value to compare against each element in the list
     * @param list  the list of integers to check
     * @return true if all elements in the list are equal to the specified state; false otherwise
     */
    private boolean isAll(int state, List<Integer> list) {
        for (Integer integer : list) {
            if (integer != state) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the specified player is the first player in the list of players
     * whose state is currently set to WAIT_FOR_SHOT.
     *
     * @param player the player to check
     * @return true if the specified player is the first player in the WAIT_FOR_SHOT state, false otherwise
     */
    public boolean isFirstWaitingForShot(Player player) {
        for (int i = 0; i < playerStates.size(); i++) {
            if (playerStates.get(i) == WAIT_FOR_SHOT) {
                return sortedPlayers.get(i).equals(player);
            }
        }
        return false;
    }

    // todo: remove me. for testing purposes only!
    public void FORCE_OPPONENT_FIREPOWER(int val) {
        this.opponentFirePower = val;
    }

    /**
     * Updates the given view with the current state of the provided game object.
     *
     * @param view      the view instance that should be updated
     * @param toDisplay the game object containing the state to render on the view
     * @throws IOException if an input or output exception occurs during the view update
     */
    @Override
    public void updateView(View view, Game toDisplay) throws IOException {
        view.renderPiratesState(toDisplay);
    }

    @Override
    public void updateStateController(ViewController controller, Game game) {
        controller.updatePiratesController(game);
    }
}
