package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.ViewController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.cg04.model.enumerations.CrewType.BROWN_ALIEN;
import static it.polimi.ingsw.cg04.model.enumerations.CrewType.PINK_ALIEN;

public class WarZoneState extends AdventureCardState {
    private final List<Double> firePower;
    private List<Integer> propulsionPower;
    private List<Integer> crewSize;
    private Integer penaltyIdx;

    private boolean rolled = false;
    private int currShotIdx;
    private int dice;
    private Direction direction;
    private Attack attack;

    private int worstPlayerState;
    private Integer worstPlayerIdx;

    private final int F_INIT = 0;
    private final int F_PROVIDE_BATTERY = 1;
    private final int F_CORRECT_SHIP = 2;
    private final int F_DONE = 3;

    private final int INIT = 0;
    private final int DONE = 1;
    private final int WORST = 2;
    private final int WILL_FIGHT = 3;

    private final int leftBoundary, rightBoundary;
    private final int upBoundary, downBoundary;


    /**
     * Constructs a new WarZoneState instance.
     *
     * @param game the current game instance used to initialize the state.
     */
    public WarZoneState(Game game) {
        super(game);
        firePower = new ArrayList<Double>();
        propulsionPower = new ArrayList<Integer>();
        crewSize = new ArrayList<Integer>();
        penaltyIdx = 0;
        currPlayerIdx = 0;
        worstPlayerState = F_INIT;
        worstPlayerIdx = 0;
        currShotIdx = -1;

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

    public Integer getPenaltyIdx() {
        return penaltyIdx;
    }

    public Integer getWorstPlayerState() {
        return worstPlayerState;
    }

    public Integer getCurrShotIdx() {
        return currShotIdx;
    }

    /**
     * Checks if all the players are DONE for this penalty.
     *
     * @param played a list of integers representing the current state of elements to check.
     * @return true if all elements in the list are equal to the constant `DONE`, false otherwise.
     */
    private boolean isAllDone(List<Integer> played) {
        for (Integer integer : played) {
            if (integer != DONE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Triggers the transition to the next shot within the current war zone state
     * if specific conditions related to the worst player's state are met.
     */
    private void triggerNextRound() {
        if (played.get(worstPlayerIdx) == WILL_FIGHT && worstPlayerState == F_DONE) {
            rolled = false;
            currShotIdx++;

            if (currShotIdx >= card.getAttacks().size()) {
                played.set(worstPlayerIdx, DONE);
                triggerNextPenalty();
            } else {
                worstPlayerState = F_INIT;
                this.appendLog("Shot incoming!");
            }
        }
    }

    /**
     * Advances to the next penalty round within the current war zone state if all players
     * have completed their actions for the current penalty.
     */
    private void triggerNextPenalty() {
        if (isAllDone(played)) {
            penaltyIdx++;
            worstPlayerIdx = 0;
            currPlayerIdx = 0;
            if (penaltyIdx >= 3) {
                triggerNextState();
            } else {
                played.replaceAll(ignored -> INIT);
                this.appendLog("New WarZone round! Round number: " + (penaltyIdx + 1));
            }
        }
    }

    /**
     * Counts the crew members for each player in the current game state and determines
     * the player with the fewest crew members to apply a penalty based on the current
     * card's penalty type.
     *
     * @param player the player initiating the crew member counting action.
     * @throws InvalidStateException if it's not the player's turn or the action is invalid in the current state.
     */
    @Override
    public void countCrewMembers(Player player) throws InvalidStateException {
        if (card.getParameterCheck().get(penaltyIdx).equals("CREW") && player.getName().equals(sortedPlayers.getFirst().getName())) {
            if (player.getGame().getSortedPlayers().size() == 1) {
                this.addLog("Only 1 player is currently in the game. WarZone will be skipped.");
                triggerNextState();
            } else {
                for (int i = 0; i < sortedPlayers.size(); i++) {
                    crewSize.add(sortedPlayers.get(i).getShip().getNumCrew());
                    if (i > 0) {
                        this.appendLog("Player " + sortedPlayers.get(i).getName() + " ha " + crewSize.get(i) + " membri dell'equipaggio.");
                    } else {
                        this.addLog("Player " + sortedPlayers.get(i).getName() + " ha " + crewSize.get(i) + " membri dell'equipaggio.");
                    }
                    played.set(i, DONE);
                }
                for (int i = 1; i < crewSize.size(); i++) {
                    if (crewSize.get(i) < crewSize.get(worstPlayerIdx)) {
                        worstPlayerIdx = i;
                    }
                }
                this.appendLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + "ha meno membri dell'equipaggio di tutti.");
                played.set(worstPlayerIdx, WORST);

                if (card.getPenaltyType().get(penaltyIdx).equals("GOBACK")) {
                    this.appendLog("Player: " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getDaysLost() + " giorni di volo.");
                    sortedPlayers.get(worstPlayerIdx).move(-card.getDaysLost());
                    played.set(worstPlayerIdx, DONE);

                    sortedPlayers = context.getSortedPlayers();
                    triggerNextPenalty();
                } else if (card.getPenaltyType().get(penaltyIdx).equals("HANDLESHOTS")) {
                    this.appendLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " subirà delle cannonate. Preparati a combattere!");
                    played.set(worstPlayerIdx, WILL_FIGHT);
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                }
            }
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    /**
     * Compares the firepower of the current player against others in the game and determines
     * outcomes based on penalties described in the current game state.
     *
     * @param player        the current player performing the firepower comparison.
     * @param batteries     a list of battery coordinates provided by the player; used to activate double cannons.
     * @param doubleCannons a list of double cannon coordinates provided by the player; used to calculate additional firepower.
     * @throws InvalidStateException if it is not the player's turn or the player performs an invalid action in the current state.
     */
    @Override
    public void compareFirePower(Player player, List<Coordinates> batteries, List<Coordinates> doubleCannons) throws InvalidStateException {
        Ship ship = player.getShip();

        if (card.getParameterCheck().get(penaltyIdx).equals("CANNONS") && player.getName().equals(sortedPlayers.get(currPlayerIdx).getName())) {
            if (player.getGame().getSortedPlayers().size() == 1) {
                this.addLog("Only 1 player is currently in the game. WarZone will be skipped.");
                triggerNextState();
            } else {
                double fire = ship.getBaseFirePower();

                if (fire > 0) {
                    fire += 2 * player.getShip().getNumCrewByType(PINK_ALIEN);
                }

                // activate double cannons if played provided them
                if (batteries != null && doubleCannons != null) {
                    // remove batteries used
                    for (Coordinates c : batteries) {
                        ship.removeBatteries(1, c.getX(), c.getY());
                    }

                    // increase firePower adding new double cannons based on orientation
                    for (Coordinates c : doubleCannons) {
                        // if laser is up bonus is 2
                        if (ship.getTile(c.getX(), c.getY()).getConnection(Direction.UP) == Connection.GUN) {
                            fire += 2;
                        } else {
                            //else bonus is 1
                            fire += 1;
                        }
                    }
                }
                firePower.add(fire);
                played.set(currPlayerIdx, DONE);
                this.addLog("Player " + player.getName() + " fire power is " + firePower.get(currPlayerIdx) + "!");
                currPlayerIdx++;
                if (isAllDone(played)) {
                    for (int i = 1; i < firePower.size(); i++) {
                        if (firePower.get(i) < firePower.get(worstPlayerIdx)) {
                            worstPlayerIdx = i;
                        }
                    }
                    this.appendLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " ha meno potenza di fuoco di tutti.");
                    played.set(worstPlayerIdx, WORST);
                    if (card.getPenaltyType().get(penaltyIdx).equals("GOBACK")) {
                        this.appendLog("Player: " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getDaysLost() + " giorni di volo.");
                        sortedPlayers.get(worstPlayerIdx).move(-card.getDaysLost());
                        played.set(worstPlayerIdx, DONE);

                        sortedPlayers = context.getSortedPlayers();
                        triggerNextPenalty();
                    } else if (card.getPenaltyType().get(penaltyIdx).equals("HANDLESHOTS")) {
                        this.appendLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " subirà delle cannonate. Preparati a combattere!");
                        played.set(worstPlayerIdx, WILL_FIGHT);
                        worstPlayerState = F_DONE;
                        triggerNextRound();
                    }
                }
            }
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    /**
     * Compares the propulsion power of the current player against others in the game and determines
     * outcomes based on penalties described in the current game state.
     *
     * @param player        the player performing the action.
     * @param coordinates   a list of coordinates indicating the positions of the batteries used by the player.
     * @param usedBatteries a list of integers representing the number of batteries utilized for each battery storage.
     * @throws InvalidStateException if it is not the player's turn or the player performs an invalid action in the current game state.
     */
    @Override
    public void usePropulsors(Player player, List<Coordinates> coordinates, List<Integer> usedBatteries) throws InvalidStateException {
        if (card.getParameterCheck().get(penaltyIdx).equals("PROPULSORS") && player.getName().equals(sortedPlayers.get(currPlayerIdx).getName())) {
            if (player.getGame().getSortedPlayers().size() == 1) {
                this.addLog("Only 1 player is currently in the game. WarZone will be skipped.");
                triggerNextState();
            } else {
                int deltaPropPower = 0;
                for (int i = 0; i < usedBatteries.size(); i++) {
                    player.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
                    deltaPropPower += usedBatteries.get(i);
                }
                if (player.getShip().getBasePropulsionPower() > 0) {
                    deltaPropPower += 2 * player.getShip().getNumCrewByType(BROWN_ALIEN);
                }
                propulsionPower.add(player.getShip().getBasePropulsionPower() + deltaPropPower * 2);
                played.set(currPlayerIdx, DONE);
                this.addLog("Player " + player.getName() + " ha " + propulsionPower.get(currPlayerIdx) + " di potenza dei propulsori.");
                currPlayerIdx++;
                if (isAllDone(played)) {
                    for (int i = 1; i < propulsionPower.size(); i++) {
                        if (propulsionPower.get(i) < propulsionPower.get(worstPlayerIdx)) {
                            worstPlayerIdx = i;
                        }
                    }
                    this.appendLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " ha meno potenza dei propulsori di tutti.");
                    played.set(worstPlayerIdx, WORST);
                    if (card.getPenaltyType().get(penaltyIdx).equals("LOSEBOX")) {
                        player.getShip().removeBestBoxes(card.getLostGoods());
                        played.set(worstPlayerIdx, DONE);
                        this.appendLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getLostGoods() + " risorse.");
                        triggerNextPenalty();
                    } else if (card.getPenaltyType().get(penaltyIdx).equals("LOSECREW")) {
                        this.appendLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " deve rimuovere " + card.getLostMembers() + " membri dell'equipaggio.");
                    }
                }
            }
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    /**
     * Removes a specified number of crew members from the player's ship based on the provided coordinates.
     *
     * @param player             the player performing the action to remove crew members.
     * @param coordinates        a list of coordinates indicating the positions on the ship where crew members are removed.
     * @param numCrewMembersLost a list of integers representing the number of crew members being removed at each coordinate.
     * @throws InvalidStateException if the action is invalid for the current game state, if the player is not the worst player, or if the number of crew members removed does not match the penalty requirements.
     */
    @Override
    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) throws InvalidStateException {
        if (coordinates == null && numCrewMembersLost == null) {
            throw new InvalidStateException(player.getName() + " removed uncorrectly the crew members. Try again.");
        }
        if (played.get(worstPlayerIdx).equals(WORST) && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName())) {
            int crewremoved = 0;
            for (Integer integer : numCrewMembersLost) {
                crewremoved += integer;
            }
            if (crewremoved != card.getLostMembers()) {
                throw new InvalidStateException(player.getName() + " removed uncorrectly the crew members. Try again.");
            }

            for (int i = 0; i < numCrewMembersLost.size(); i++) {
                Tile currTile = player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY());
                player.getShip().removeCrew(currTile.getHostedCrewType(), coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
            }
            this.addLog("Player " + player.getName() + " ha rimosso i membri dall'equipaggio.");
            played.set(worstPlayerIdx, DONE);
            triggerNextPenalty();
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    /**
     * Handles the selection of a battery by the worst player during their turn
     * to neutralize an attack with a shield or take a hit if no battery is used.
     *
     * @param player the player attempting the battery selection.
     * @param x      the x-coordinate of a battery on the ship or -1 if no battery is used.
     * @param y      the y-coordinate of a battery on the ship or -1 if no battery is used.
     * @throws InvalidStateException if it is not the turn of the specified player or the action is invalid in the current game state.
     */
    @Override
    public void chooseBattery(Player player, int x, int y) throws InvalidStateException {
        if (rolled && worstPlayerState == F_PROVIDE_BATTERY && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName())) {

            // handle case where player decide to take the hit
            if (x == -1 && y == -1) {
                player.getShip().handleHit(direction, dice);
                if (!player.getShip().isShipLegal()) {
                    this.addLog("Player " + player.getName() + " has been hit and he should fix his ship!");
                    worstPlayerState = F_CORRECT_SHIP;
                } else {
                    this.addLog("Player " + player.getName() + " has been hit but his ship is still legal!");
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                }
            } else { // player used battery and he is done for the round
                this.addLog("Player " + player.getName() + " used a battery and neutralized the attack!");
                player.getShip().removeBatteries(1, x, y);
                worstPlayerState = F_DONE;
                triggerNextRound();
            }
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    /**
     * Removes the tiles of the player's ship based on the provided coordinates,
     * ensuring the ship becomes legal. If the ship is successfully fixed, the
     * player's turn for this round is marked as done. .
     *
     * @param player          the player attempting to fix their ship.
     * @param coordinatesList a list of coordinates representing the tiles on the ship to be removed.
     * @throws InvalidStateException if it's not the player's turn, the action is not valid in the current state, or the
     *                               ship remains in an illegal state after removing tiles.
     */
    @Override
    public void fixShip(Player player, List<Coordinates> coordinatesList) throws InvalidStateException {
        // check if player actually needs to fix his ship
        if (rolled && worstPlayerState == F_CORRECT_SHIP && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName())) {
            for (Coordinates coordinates : coordinatesList) {
                player.getShip().breakTile(coordinates.getX(), coordinates.getY());
            }
            // if fixes make the ship legal player is done for this round
            if (player.getShip().isShipLegal()) {
                this.addLog("Player: " + player.getName() + "'s ship was fixed!");
                worstPlayerState = F_DONE;
                triggerNextRound();
            } else {
                this.addLog("Player: " + player.getName() + "'s ship is still illegal! Fix it with fixShip!");
            }
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    /**
     * Rolls dices to determine the row or the column where the shot will hit. The result of the dice
     * roll is used to determine whether the player's ship is hit, missed, or requires additional actions.
     *
     * @param player the player performing the dice roll action.
     * @throws InvalidStateException if it is not the player's turn, or the action is invalid in the current game state.
     */
    @Override
    public void rollDice(Player player) throws InvalidStateException {

        if (!rolled && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName()) && worstPlayerState == F_INIT) {
            dice = context.getBoard().rollDices();
            this.addLog("Dice result is: " + dice);
            rolled = true;


            // check if meteor misses completely
            direction = context.getCurrentAdventureCard().getDirection(currShotIdx);
            attack = context.getCurrentAdventureCard().getAttack(currShotIdx);


            // check whether the meteor will hit the ships
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (dice < leftBoundary || dice > rightBoundary) {
                    this.appendLog("Ship wasn't hit.");
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                    return;
                } else {
                    // substract offset to find zero-based index to handle hit
                    dice = dice - leftBoundary;
                }
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                if (dice < upBoundary || dice > downBoundary) {
                    this.appendLog("Ship wasn't hit");
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                    return;
                } else {
                    // substract offset to find zero-based index to handle hit
                    dice = dice - upBoundary;
                }
            }

            // create list that maps player with what he needs to do

            int hitState = player.getShip().checkHit(direction, attack, dice, "shot");

            // if player is safe set its state to done for this attack
            if (hitState == -1) {
                this.appendLog("Player: " + player.getName() + " was not hit");
                worstPlayerState = F_DONE;
                triggerNextRound();
            }
            // deliver guaranteed hit and check if ship is still legal if not put in correction state "2"
            else if (hitState == 2) {
                this.appendLog("Player " + player.getName() + " was hit");
                player.getShip().handleHit(direction, dice);

                // if ship is legal, player is done for this attack
                if (player.getShip().isShipLegal()) {
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                } else {
                    // player needs to correct his ship
                    this.appendLog("Player " + player.getName() + " deve correggere la nave.");
                    worstPlayerState = F_CORRECT_SHIP;
                }
            }
            // player can decide to use batteries to defend his ship
            else if (hitState == 0) {
                this.appendLog("Player: " + player.getName() + " can use a battery to save his ship!");
                worstPlayerState = F_PROVIDE_BATTERY;
            }
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    /**
     * Determines if any player in the current game state holds the "worst" status.
     *
     * @return true if at least one player's state matches the "WORST" or "WILL_FIGHT" conditions.
     */
    public boolean anyWorstPlayer() {
        for (Integer integer : played) {
            if (integer == WORST || integer == WILL_FIGHT) {
                return true;
            }
        }
        return false;
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
        view.renderWarZoneState(toDisplay);
    }

    @Override
    public void updateStateController(ViewController controller, Game game) {
        controller.updateWarZoneController(game);
    }
}
