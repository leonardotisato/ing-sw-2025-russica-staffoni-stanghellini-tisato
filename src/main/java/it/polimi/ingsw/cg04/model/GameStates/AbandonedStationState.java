package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleBoxesAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbandonedStationState extends AdventureCardState {
    private Integer playerToBeMovedIdx;
    public AbandonedStationState(Game game) {
        super(game);
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
            if (card.getObtainedResources().containsKey(type)) {
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


    public void handleBoxes(Player player, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes){
        if (!player.equals(sortedPlayers.get(this.currPlayerIdx))) throw new RuntimeException("Not curr player");
        if(player.getShip().getNumCrew() <= getCard().getMembersNeeded()) throw new RuntimeException("Not enough crew members");
        if (!checkRightBoxesAfterReward(player, coordinates, boxes)) throw new RuntimeException("wrong amount of boxes after reward");
        if (coordinates == null && boxes == null){
            played.set(currPlayerIdx, 1);
            currPlayerIdx++;
        }
        else {
            for (int i = 0; i < coordinates.size(); i++) {
                player.getShip().setBoxes(boxes.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
            }
            played.replaceAll(ignored -> 1);
            player.move(-card.getDaysLost());
        }
        if (!played.contains(0)) triggerNextState();
    }
}
