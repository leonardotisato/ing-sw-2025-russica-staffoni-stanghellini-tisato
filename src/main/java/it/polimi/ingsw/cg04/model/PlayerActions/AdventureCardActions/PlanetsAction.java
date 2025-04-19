package it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanetsAction implements PlayerAction {
    private final Integer planetIdx;
    private final List<Coordinates> coordinates;
    private final List<Map<BoxType, Integer>> boxes;
    private final String nickname;

    //boxes should contain the complete storage tiles map
    public PlanetsAction(String nickname, Integer planetIdx, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) {
        this.planetIdx = planetIdx;
        this.coordinates = coordinates;
        this.boxes = boxes;
        this.nickname = nickname;
    }

    public void execute(Player player) throws InvalidStateException {
        GameState state = player.getGame().getGameState();
        state.landToPlanet(player, planetIdx, coordinates, boxes);
    }

    public boolean checkAction(Player player) throws InvalidActionException {
        if (planetIdx == null) return true;
        List<Coordinates> storageCoordinates = player.getShip().getTilesMap().get("StorageTile");
        Map<BoxType, Integer> newTotBoxes = new HashMap<>(Map.of(BoxType.RED, 0, BoxType.BLUE, 0, BoxType.YELLOW, 0, BoxType.GREEN, 0));
        if (coordinates == null && boxes == null) return true;
        //if just one of them is null, it's an error
        if (coordinates == null || boxes == null) throw new InvalidActionException("Wrong inputs format!");
        //mismatch error
        if (boxes.size() != coordinates.size())
            throw new InvalidActionException("boxes size does not match coordinates size!");
        if (storageCoordinates.size() != boxes.size())
            throw new InvalidActionException("the new boxes map is needed for each StorageTile in your ship!");
        for (int i = 0; i < coordinates.size(); i++) {
            if (!coordinates.get(i).isIn(storageCoordinates)) {
                throw new InvalidActionException("Tile in " + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + " is not a StorageTile!");
            }
            Coordinates coord = coordinates.get(i);
            Map<BoxType, Integer> boxesAtCoord = boxes.get(i);
            if (boxesAtCoord.values().stream().mapToInt(Integer::intValue).sum() > player.getShip().getTile(coord.getX(), coord.getY()).getMaxBoxes()) {
                throw new InvalidActionException("StorageTile in " + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + " contains more boxes than the number allowed!");
            }
            if (boxesAtCoord.get(BoxType.RED) > 0 && !player.getShip().getTile(coord.getX(), coord.getY()).isSpecialStorageTile()) {
                throw new InvalidActionException("StorageTile in " + coordinates.get(i).getX() + ", " + coordinates.get(i).getY() + " is not a Special StorageTile, it can't contains RED boxes!");
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

