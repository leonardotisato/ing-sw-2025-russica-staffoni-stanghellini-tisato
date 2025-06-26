package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.ViewController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.cg04.model.enumerations.CrewType.PINK_ALIEN;

public class SmugglersState extends AdventureCardState {
    private final int DONE = 2;
    private final int HANDLE_BOXES = 1;
    private final int ACTIVATE_CANNONS = 0;

    public SmugglersState(Game game) {
        super(game);
    }

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

        if (played.get(playerIdx) == ACTIVATE_CANNONS && currPlayerIdx == playerIdx) {
            double firePower = ship.getBaseFirePower();

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
                        firePower += 2;
                    } else {
                        //else bonus is 1
                        firePower += 1;
                    }
                }

                this.addLog("Player " + player.getName() + " increased his fire power to " + firePower + " by using " + batteries.size() + "batteries");
            }

            if (firePower > 0) {
                firePower += 2 * player.getShip().getNumCrewByType(PINK_ALIEN);
                this.addLog("Player " + player.getName() + " has a pink alien and gets bonus firepower!");
            }

            // now check if player defeated the opponent

            // player has lost
            if (firePower < card.getFirePower()) {
                player.getShip().removeBestBoxes(card.getLostGoods());
                played.set(playerIdx, DONE);
                currPlayerIdx++;
                this.appendLog("Player " + player.getName() + " lost against the smugglers!");
            } else if (firePower == card.getFirePower()) {
                played.set(playerIdx, DONE);
                currPlayerIdx++;
                this.appendLog("Player " + player.getName() + " drew against the smugglers!");
            } else {
                played.replaceAll(i -> DONE);
                played.set(playerIdx, HANDLE_BOXES);
                this.appendLog("Player " + player.getName() + " won against the smugglers!");
            }

            // check if every one is done
            if (isAllDone(played)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Player " + player.getName() + " performed wrong action or not his turn");
        }
    }

    @Override
    public void handleBoxes(Player player, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);
        if (played.get(playerIdx) != HANDLE_BOXES)
            throw new InvalidStateException("Player " + player.getName() + " can't handle boxes");
        checkRightBoxesAfterReward(player, coordinates, boxes);
        if (coordinates == null && boxes == null) {
            this.addLog("Player " + player.getName() + " decided not to lose flight days!");
            played.set(playerIdx, DONE);
        } else {
            for (int i = 0; i < coordinates.size(); i++) {
                player.getShip().setBoxes(boxes.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
            }
            played.set(playerIdx, DONE);
            player.move(-card.getDaysLost());
            this.addLog(player.getName() + " decided to handle boxes and lost " + card.getDaysLost() + " flight days!");
        }
        if (isAllDone(played)) {
            triggerNextState();
        }
        ;
    }

    public boolean checkRightBoxesAfterReward(Player player, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) throws InvalidStateException {
        //the player doesn't want to play this card
        if (coordinates == null && boxes == null) return true;
        Map<BoxType, Integer> newTotBoxes = new HashMap<>(Map.of(BoxType.RED, 0, BoxType.BLUE, 0, BoxType.YELLOW, 0, BoxType.GREEN, 0));
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinates coord = coordinates.get(i);
            Map<BoxType, Integer> boxesAtCoord = boxes.get(i);
            for (Map.Entry<BoxType, Integer> entry : boxesAtCoord.entrySet()) {
                BoxType type = entry.getKey();
                Integer count = entry.getValue();
                newTotBoxes.put(type, newTotBoxes.getOrDefault(type, 0) + count);
            }
        }
        //newTotBoxes contains the updated amount of boxes type-wise
        for (Map.Entry<BoxType, Integer> entry : newTotBoxes.entrySet()) {
            BoxType type = entry.getKey();
            Integer count = entry.getValue();
            if (card.getBoxes().containsKey(type)) {
                //number of boxes of a specific type should be < old number (type) + reward(type)
                if (count > player.getShip().getBoxes(type) + card.getObtainedResourcesByType(type)) {
                    throw new InvalidStateException("The new map of boxes for " + player.getName() + " is incorrect, there are too many " + type + " boxes");
                }
            } else {
                //if there is no box in the reward of this type, the new number of box(type) should be <= the old number of box(type)
                if (count > player.getShip().getBoxes(type)) {
                    throw new InvalidStateException("The new map of boxes for " + player.getName() + " is incorrect, there are too many " + type + " boxes");
                }
            }
        }
        return true;
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

    @Override
    public void disconnect(Player player) {
        played.remove(context.getSortedPlayers().indexOf(player));
        if (isAllDone(played)) {
            triggerNextState();
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
    public void updateView(View view, Game toDisplay) throws IOException {
        view.renderSmugglersState(toDisplay);
    }

    @Override
    public void updateStateController(ViewController controller, Game game) {
        controller.updateSmugglersController(game);
    }
}
