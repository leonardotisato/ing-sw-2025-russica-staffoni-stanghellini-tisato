package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
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
            throw new InvalidStateException("Player " + player.getName() + " performed wrong action or not his turn");
        }
    }

    public void handleBoxes(Player player, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);
        if (played.get(playerIdx) != HANDLE_BOXES) throw new InvalidStateException("Player " + player.getName() + " can't handle boxes");
        checkRightBoxesAfterReward(player, coordinates, boxes);
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

    public boolean checkRightBoxesAfterReward(Player player, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes) throws InvalidStateException {
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
                    throw new InvalidStateException("The new map of boxes for " + player.getName() + " is incorrect, there are too many " + type + " boxes");
                }
            }
            else {
                //if there is no box in the reward of this type, the new number of box(type) should be <= the old number of box(type)
                if (count > player.getShip().getBoxes(type)) {
                    throw new InvalidStateException("The new map of boxes for " + player.getName() + " is incorrect, there are too many " + type + " boxes");
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

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder(super.render(playerName));
        stringBuilder.append("\n".repeat(3));
        Player p = context.getPlayer(playerName);
        int playerIdx = p.getRanking() - 1;
        if (played.get(playerIdx) == ACTIVATE_CANNONS && currPlayerIdx == playerIdx){
            List<Coordinates> lasersCoordinates = p.getShip().getTilesMap().get("LaserTile");
            int totDoubleLasers = (int)lasersCoordinates.stream()
                    .map(coord -> p.getShip().getTile(coord.getX(), coord.getY()))
                    .filter(t -> t.isDoubleLaser())
                    .count();
            stringBuilder.append("Smugglers are here! Activate your double cannons and try to defeat them!").append("\n");
            stringBuilder.append("Base fire power of your ship: " + p.getShip().getBaseFirePower()).append("\n");
            stringBuilder.append("Number of double cannons: " + totDoubleLasers).append("\n");
        }
        else if(played.get(playerIdx) == ACTIVATE_CANNONS && currPlayerIdx != playerIdx){
            stringBuilder.append("Smugglers are coming! Wait for " + sortedPlayers.get(currPlayerIdx).getName() + " to combat them.").append("\n");
        }
        else if (played.get(playerIdx) == HANDLE_BOXES) {
            stringBuilder.append("You won! Handle your new boxes now if you want.").append("\n");
            stringBuilder.append("Please note that you will lose " + card.getDaysLost() + " days of flight.").append("\n");
        }
        else if (played.get(playerIdx) == DONE){
            stringBuilder.append("You're done for this card! Wait for the other players to start the next adventure.").append("\n");
        }
        return stringBuilder.toString();
    }
}
