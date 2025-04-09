package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
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
    private String nickname;
    public HandleBoxesAction(String nickname, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes) {
        this.coordinates = coordinates;
        this.boxes = boxes;
        this.nickname = nickname;
    }

    public void execute(Player player) {
        GameState state = player.getGame().getGameState();
        state.handleBoxes(player, coordinates, boxes);
        }

    public boolean checkAction(Player player) {
        GameState gameState = player.getGame().getGameState();
        if(coordinates == null && boxes == null) return true;
        if (coordinates == null || boxes == null) return false;
        List<Coordinates> storageCoordinates = player.getShip().getTilesMap().get("StorageTile");
        Map<BoxType,Integer> newTotBoxes = new HashMap<>(Map.of(BoxType.RED, 0, BoxType.BLUE, 0, BoxType.YELLOW, 0, BoxType.GREEN, 0));
        if(boxes.size() != coordinates.size()) return false;
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
         return true;
    }

    @Override
    public String getPlayerNickname() {
        return this.nickname;
    }
}
