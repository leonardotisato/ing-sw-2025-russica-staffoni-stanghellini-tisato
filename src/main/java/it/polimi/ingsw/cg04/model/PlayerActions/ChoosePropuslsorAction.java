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
    String nickname;

    public ChoosePropuslsorAction(String nickname, List<Coordinates> coordinates, List<Integer> usedBatteries) {
        this.coordinates = new ArrayList<>(coordinates);
        this.usedBatteries = new ArrayList<>(usedBatteries);
        this.nickname = nickname;
    }

    public void execute(Player player) {
        AdventureCardState state = (AdventureCardState) player.getGame().getGameState();
        state.usePropulsors(player, coordinates, usedBatteries);
    }

    @Override
    public boolean checkAction(Player player) {
        AdventureCardState gameState = (AdventureCardState) player.getGame().getGameState();
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

    @Override
    public String getPlayerNickname() {
        return this.nickname;
    }
}
