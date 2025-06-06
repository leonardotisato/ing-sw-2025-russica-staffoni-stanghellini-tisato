package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbandonedStationState extends AdventureCardState {
    private Integer playerToBeMovedIdx;

    public AbandonedStationState(Game game) {
        super(game);
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
        //newTotBoxes contains the updated number of boxes type-wise
        for (Map.Entry<BoxType, Integer> entry : newTotBoxes.entrySet()) {
            BoxType type = entry.getKey();
            Integer count = entry.getValue();
            if (card.getObtainedResources().containsKey(type)) {
                // the number of boxes of a specific type should be < old number (type) + reward(type)
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


    public void handleBoxes(Player player, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) throws InvalidStateException {
        if (!player.equals(sortedPlayers.get(this.currPlayerIdx)))
            throw new InvalidStateException("Player" + player.getName() + " can't play, it's not his turn, player " + sortedPlayers.get(this.currPlayerIdx) + " should play");
        checkRightBoxesAfterReward(player, coordinates, boxes);
        if (coordinates == null && boxes == null) {
            this.addLog("Player " + player.getName() + " decided not to play this card.");
            played.set(currPlayerIdx, 1);
            currPlayerIdx++;
        } else {
            if (player.getShip().getNumCrew() <= getCard().getMembersNeeded())
                throw new InvalidStateException("Player " + player.getName() + " has not enough crew members");
            for (int i = 0; i < coordinates.size(); i++) {
                player.getShip().setBoxes(boxes.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
            }
            played.replaceAll(ignored -> 1);
            player.move(-card.getDaysLost());
            this.addLog("Player " + player.getName() + " decided to play this card. He earned the boxes, but he lost " + card.getDaysLost() + " flight days.");
        }
        if (!played.contains(0)) triggerNextState();
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
        view.renderAbandonedStationState(toDisplay);
    }

}
