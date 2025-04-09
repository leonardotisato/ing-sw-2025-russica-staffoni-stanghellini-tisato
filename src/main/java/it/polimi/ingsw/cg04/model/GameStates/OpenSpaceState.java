package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class OpenSpaceState extends AdventureCardState {

    public OpenSpaceState(Game game) {
        super(game);
    }
    public void handleAction(Player player, PlayerAction action) {
        return;
        //delete
    }

    @Override
    public void usePropulsors(Player p, List<Coordinates> coordinates, List<Integer> usedBatteries){
        if (!p.equals(sortedPlayers.get(currPlayerIdx))) throw new RuntimeException("Not curr player");
        if(usedBatteries == null) usedBatteries = new ArrayList<>();
        for (int i = 0; i < usedBatteries.size(); i++) {
            p.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
        }
        p.move(p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
        this.played.set(currPlayerIdx, 1);
        this.currPlayerIdx++;
        if (currPlayerIdx == sortedPlayers.size()) {
            triggerNextState();
        }
    }
}
