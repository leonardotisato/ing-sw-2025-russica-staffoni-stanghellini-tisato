package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmugglersState extends AdventureCardState {
    private final int DONE = 2;
    private final int HANDLE_BOXES = 1;
    private final int ACTIVATE_CANNONS = 0;

    public SmugglersState(Game game) {
        super(game);
    }

    public void handleAction(Player player, PlayerAction action) {
        return;
    }

    public void compareFirePower(Player player, List<Coordinates> batteries, List<Coordinates> doubleCannons) {
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
            }

            // now check if player defeated the opponent

            // player has lost
            if (firePower < card.getFirePower()) {
                player.getShip().removeBestBoxes(card.getLostGoods());
                played.set(playerIdx, DONE);
                currPlayerIdx++;
            } else if (firePower == card.getFirePower()) {
                played.set(playerIdx, DONE);
                currPlayerIdx++;
            } else {
                played.replaceAll(i -> DONE);
                played.set(playerIdx, HANDLE_BOXES);
            }

            // check if every one is done
            if (isAllDone(played)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            System.out.println("Player " + player.getName() + " performed wrong action or not his turn");
        }
    }

    public void handleBoxes(Player player, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes){
        int playerIdx = sortedPlayers.indexOf(player);
        if (played.get(playerIdx) != HANDLE_BOXES) throw new RuntimeException("you can't handle boxes now!");
        if (!checkRightBoxesAfterReward(player, coordinates, boxes)) throw new RuntimeException("wrong amount of boxes after reward");
        if (coordinates == null && boxes == null){
            played.set(playerIdx, DONE);
        }
        else {
            for (int i = 0; i < coordinates.size(); i++) {
                player.getShip().setBoxes(boxes.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
            }
            played.set(playerIdx, DONE);
            player.move(-card.getDaysLost());
        }
        if (isAllDone(played)) {
            triggerNextState();
        };
    }

    public boolean checkRightBoxesAfterReward(Player player, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes) {
        //the player doesn't want to play this card
        if(coordinates == null && boxes == null) return true;
        Map<BoxType,Integer> newTotBoxes = new HashMap<>(Map.of(BoxType.RED, 0, BoxType.BLUE, 0, BoxType.YELLOW, 0, BoxType.GREEN, 0));
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
                    return false;
                }
            }
            else {
                //if there is no box in the reward of this type, the new number of box(type) should be <= the old number of box(type)
                if (count > player.getShip().getBoxes(type)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isAllDone(List<Integer> list) {
        for (Integer integer : list) {
            if (integer != DONE) {
                return false;
            }
        }
        return true;
    }
}
