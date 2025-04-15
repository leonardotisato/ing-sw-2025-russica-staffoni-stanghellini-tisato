package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleBoxesAction implements PlayerAction{
    private Game game;
    private final List<Coordinates> coordinates;
    private final List<Map<BoxType,Integer>> boxes;
    private final String nickname;
    public HandleBoxesAction(String nickname, List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes) {
        this.coordinates = coordinates;
        this.boxes = boxes;
        this.nickname = nickname;
    }

    public void execute(Player player) {
        GameState state = player.getGame().getGameState();
        state.handleBoxes(player, coordinates, boxes);
        }

    public boolean checkAction(Player player) throws InvalidActionException {
        GameState gameState = player.getGame().getGameState();
        // if both null, the player doesn't want to play
        if(coordinates == null && boxes == null) return true;
        //if just one of them is null, it's an error
        if (coordinates == null || boxes == null) throw new InvalidActionException("Wrong inputs format!");
        List<Coordinates> storageCoordinates = player.getShip().getTilesMap().get("StorageTile");
        Map<BoxType,Integer> newTotBoxes = new HashMap<>(Map.of(BoxType.RED, 0, BoxType.BLUE, 0, BoxType.YELLOW, 0, BoxType.GREEN, 0));
        //mismatch error
        if(boxes.size() != coordinates.size()) throw new InvalidActionException("boxes size does not match coordinates size!");
        //the player should send the new boxes map for each storage tile in his ship
        if(storageCoordinates.size() != boxes.size()) throw new InvalidActionException("the new boxes map is needed for each StorageTile in your ship!");
        for (int i = 0; i < coordinates.size(); i++) {
            //the tile in these coordinates is not a storageTile
            if(!coordinates.get(i).isIn(storageCoordinates)){
                throw new InvalidActionException("Tile in (" + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + ") is not a StorageTile!");
            }
            Coordinates coord = coordinates.get(i);
            Map<BoxType, Integer> boxesAtCoord = boxes.get(i);
            //more boxes than the max number allowed in this storage tile
            if(boxesAtCoord.values().stream().mapToInt(Integer::intValue).sum() > player.getShip().getTile(coord.getX(), coord.getY()).getMaxBoxes()){
                throw new InvalidActionException("StorageTile in " + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + " contains more boxes than the number allowed!");
            }
            //special box in a no-special tile
            if(boxesAtCoord.get(BoxType.RED) > 0 && !player.getShip().getTile(coord.getX(), coord.getY()).isSpecialStorageTile()){
                throw new InvalidActionException("StorageTile in " + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + " is not a Special StorageTile, it can't contains RED boxes!");
            }
            for (Map.Entry<BoxType, Integer> entry : boxesAtCoord.entrySet()) {
                BoxType type = entry.getKey();
                Integer count = entry.getValue();

                newTotBoxes.put(type, newTotBoxes.getOrDefault(type, 0) + count);
            }
        }
        //the action is legal!
         return true;
    }

    @Override
    public String getPlayerNickname() {
        return this.nickname;
    }
}
