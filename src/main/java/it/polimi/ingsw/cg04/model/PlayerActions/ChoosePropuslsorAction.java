package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class ChoosePropuslsorAction implements PlayerAction {
    List<Coordinates> coordinates;
    List<Integer> usedBatteries;
    Game game;

    public ChoosePropuslsorAction(List<Coordinates> coordinates, List<Integer> usedBatteries, Game game) {
        this.coordinates = new ArrayList<>(coordinates);
        this.usedBatteries = new ArrayList<>(usedBatteries);
        this.game = game;
    }

    public void execute(Player player) {
            for (int i = 0; i < usedBatteries.size(); i++) {
                player.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
            }
        player.move(player.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
    }

    @Override
    public boolean checkAction(Player player) {
        AdventureCardState gameState = (AdventureCardState) game.getGameState();
        List<Coordinates> propulsorCoordinates = player.getShip().getTilesMap().get("PropulsorTile");
        int numberOfBatteries = usedBatteries.stream().mapToInt( b -> b.intValue()).sum();
        int totDoublePropulsor = (int)propulsorCoordinates.stream()
                .map(coord -> player.getShip().getTile(coord.getX(), coord.getY()))
                .filter(t -> t.isDoublePropulsor())
                .count();
        if(numberOfBatteries > totDoublePropulsor) return false;
        for (int i = 0; i < usedBatteries.size(); i++) {
            if(!coordinates.get(i).isIn(player.getShip().getTilesMap().get("BatteryTile"))){
                return false;
            }
            if(usedBatteries.get(i) > player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY()).getNumBatteries()){
                return false;
            }
        }
        return true;
    }
}
