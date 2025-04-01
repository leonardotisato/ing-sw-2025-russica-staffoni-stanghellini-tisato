package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ChoosePropuslsorAction implements PlayerAction {
    List<List<Integer>> coordinates;
    List<Integer> usedBatteries;

    public ChoosePropuslsorAction(List<List<Integer>> coordinates, List<Integer> usedBatteries) {
        this.coordinates = new ArrayList<>(coordinates);
        this.usedBatteries = new ArrayList<>(usedBatteries);
    }

    public void execute(Player player) {
        for(int i = 0; i < usedBatteries.size(); i++){
            player.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).get(0), coordinates.get(i).get(1));
        }
        player.move(player.getShip().getBasePropulsionPower() + usedBatteries.size() * 2);

        //position = player.getRanking();
        endAction(player);
    }

    public void endAction(Player player) {
        player.setActivity(0);
        int position = player.getRanking();
        if (player.getGame().getPlayers().size() == position){
            // porto tutti a flight
        }

        player.getGame().getSortedPlayers().get(position +1).setActivity(1);
    }


}
