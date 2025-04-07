package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanetsAction implements PlayerAction {
    private Integer planetIdx;
    private final List<Coordinates> coordinates;
    private final List<Map<BoxType, Integer>> boxes;
    private final Game game;
     //boxes should contain the complete storage tiles map
    public PlanetsAction(Integer planetIdx, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes, Game game) {
        this.planetIdx = planetIdx;
        this.coordinates = coordinates;
        this.boxes = boxes;
        this.game = game;
    }

    public void execute(Player player) {
        AdventureCardState gameState = (AdventureCardState) game.getGameState();
        if (planetIdx == null) {
            return;
        } else {
            gameState.getChosenPlanets().put(player, planetIdx);
            for (int i = 0; i < coordinates.size(); i++) {
                player.getShip().setBoxes(boxes.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
            }
        }
    }

    public boolean checkAction(Player player) {
        AdventureCardState gameState = (AdventureCardState) game.getGameState();
        if(!player.equals(gameState.getSortedPlayers().get(gameState.getCurrPlayerIdx()))) return false;
        if(planetIdx != null && (planetIdx < 0 || planetIdx > game.getCurrentAdventureCard().getPlanetReward().size() - 1)) return false;
        if(gameState.getChosenPlanets().containsValue(planetIdx)) return false;
        if(planetIdx == null) return true;
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
            if(gameState.getCard().getPlanetReward().get(planetIdx).containsKey(type)){
                if (count > player.getShip().getBoxes(type) + gameState.getCard().getPlanetReward().get(planetIdx).get(type)) {
                    return false;
                }
            }
            else {
                if (count > player.getShip().getBoxes(type)) return false;
            }
        }
        return true;
    }

    @Override
    public String getPlayerNickname() {
        return "";
    }

    @Override
    public String getType() {
        return "PLANETS_ACTION";
    }
}

