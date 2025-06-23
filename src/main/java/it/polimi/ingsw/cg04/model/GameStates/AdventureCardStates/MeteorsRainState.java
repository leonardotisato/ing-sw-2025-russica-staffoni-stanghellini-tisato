package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.ViewController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
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

    /**
     * Retrieves the index of the current meteor in the game state.
     *
     * @return the current meteor index
     */
    public int getCurrMeteorIdx() {
        return currMeteorIdx;
    }

    /**
     * Retrieves the value of the dice rolled in the current game state.
     *
     * @return the result of the dice roll
     */
    public int getDiceResult() {
        return dice;
    }


    /**
     * Checks whether the dice has been rolled in the current game state.
     *
     * @return true if the dice has been rolled, false otherwise
     */
    public boolean isRolled() {
        return rolled;
    }

    /**
     * Checks if all elements in the provided list are equal to the DONE value.
     *
     * @param list the list of integers to check
     * @return true if all elements in the list are equal to DONE, false otherwise
     */
    private boolean isAllDone(List<Integer> list) {
        for (Integer integer : list) {
            if (integer != DONE) {
                return false;
            }
        }
        return true;
    }


    /**
     * Advances the game to the next round in the MeteorsRainState.
     *
     * This method resets the rolled status for the dice, increments the index of the current meteor,
     * and determines whether it is necessary to transition to the next game state. If all meteors have
     * been processed, the `triggerNextState` method is called to handle the transition. Otherwise, the
     * state of each player is reset to the INIT state in preparation for the next round.
     */
    private void triggerNextRound() {
        rolled = false;
        currMeteorIdx++;

        if (currMeteorIdx >= numMeteors) {
            triggerNextState();
        } else {
            played.replaceAll(ignored -> INIT);
        }
    }

    /**
     * Handles the roll dice action in the context of the MeteorsRainState.
     * This method allows a player to roll the dice, determines the effect of the roll based on
     * the current meteor's direction and attack specifications, and updates the game state accordingly.
     *
     * @param player the player who initiates the roll dice action
     * @throws InvalidStateException if the action is not allowed for the player or the game state
     */
    @Override
    public void rollDice(Player player) throws InvalidStateException {
        if (!rolled && player.equals(sortedPlayers.getFirst())) {
            dice = context.getBoard().rollDices();
            rolled = true;


            // check if the meteor misses completely
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

            System.out.println("0-based dice result is: " + dice);

            // create a list that maps player with what he needs to do
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

    /**
     * Fixes the ship of the given player by repairing the specified tiles.
     * This method checks if the player is allowed to perform the fix operation
     * and updates the game state based on the legality of the ship after the fix.
     *
     * @param player the player who is attempting to fix their ship
     * @param coordinatesList a list of coordinates representing the ship tiles to be repaired
     * @throws InvalidStateException if the player is not allowed to fix their ship at this time
     */
    @Override
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

    /**
     * Allows a player to choose how to respond when their ship is under attack by a meteor.
     * The player can either handle the hit by accepting the damage or use a battery to prevent damage.
     *
     * @param player the player making the decision to handle the attack
     * @param x the x-coordinate of the battery to be used; or -1 if the player chooses to take the hit
     * @param y the y-coordinate of the battery to be used; or -1 if the player chooses to take the hit
     * @throws InvalidStateException if the player is not allowed to perform the choose battery action at this time
     */
    @Override
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

    /**
     * Updates the given view with the current state of the provided game object.
     *
     * @param view      the view instance that should be updated
     * @param toDisplay the game object containing the state to render on the view
     * @throws IOException if an input or output exception occurs during the view update
     */
    @Override
    public void updateView (View view, Game toDisplay) throws IOException {
        view.renderMeteorsRainState(toDisplay);
    }

    @Override
    public void updateStateController(ViewController controller, Game game) {
        controller.updateMeteorsRainController(game);
    }

}
