package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleBoxesAction implements PlayerAction{
    private Game game;
    private final List<Coordinates> coordinates;
    private final List<Map<BoxType,Integer>> boxes;
    public HandleBoxesAction(List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes, Game game) {
        this.coordinates = coordinates;
        this.boxes = boxes;
        this.game = game;
    }

    public void execute(Player player) {
        AdventureCardState gameState = (AdventureCardState)game.getGameState();
        if (boxes.isEmpty()) {
            gameState.getPlayed().set(gameState.getCurrPlayerIdx(), 1);
            gameState.setCurrPlayerIdx(gameState.getCurrPlayerIdx() + 1);
            return;
        }
        else {
            //TODO sposta check crew member nella handleAction
            if (player.getShip().getNumCrew() >= game.getCurrentAdventureCard().getMembersNeeded()) {
                for (int i = 0; i < coordinates.size(); i++) {
                    player.getShip().setBoxes(boxes.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
                }
            }
            else throw new RuntimeException("not enough crew");
            gameState.getPlayed().replaceAll(ignored -> 1);
            player.move(-game.getCurrentAdventureCard().getDaysLost());
            return;
        }
    }

    public boolean checkAction(Player player) {
        AdventureCardState gameState = (AdventureCardState) game.getGameState();
        if (!gameState.checkAction(player)) return false;
        if(!player.equals(gameState.getSortedPlayers().get(gameState.getCurrPlayerIdx()))) return false;
        List<Coordinates> storageCoordinates = player.getShip().getTilesMap().get("StorageTile");
        Map<BoxType,Integer> newTotBoxes = new HashMap<>(Map.of(BoxType.RED, 0, BoxType.BLUE, 0, BoxType.YELLOW, 0, BoxType.GREEN, 0));
        for (int i = 0; i < coordinates.size(); i++) {
            if(!coordinates.get(i).isIn(storageCoordinates)){
                return false;
            }
            Coordinates coord = coordinates.get(i);
            Map<BoxType, Integer> boxesAtCoord = boxes.get(i);
            if(boxesAtCoord.values().stream().mapToInt(Integer::intValue).sum() > player.getShip().getTile(coord.getX(), coord.getY()).getMaxBoxes()){
                return false;
            }
            if(boxesAtCoord.get(BoxType.RED) > 0 && !player.getShip().getTile(coord.getX(), coord.getY()).isSpecialStorageTile()){
                return false;
            }
            for (Map.Entry<BoxType, Integer> entry : boxesAtCoord.entrySet()) {
                BoxType type = entry.getKey();
                Integer count = entry.getValue();

                newTotBoxes.put(type, newTotBoxes.getOrDefault(type, 0) + count);
            }
        }

        for (Map.Entry<BoxType, Integer> entry : newTotBoxes.entrySet()) {
            BoxType type = entry.getKey();
            Integer count = entry.getValue();
            if (count > player.getShip().getBoxes(type) + gameState.getCard().getObtainedResourcesByType(type)) {
                return false;
            }
        }
         return true;
    }
}
