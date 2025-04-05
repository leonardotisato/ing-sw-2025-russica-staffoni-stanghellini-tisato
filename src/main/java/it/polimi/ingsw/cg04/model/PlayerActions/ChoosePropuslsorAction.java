package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class ChoosePropuslsorAction implements PlayerAction {
    List<Coordinates> coordinates;
    List<Integer> usedBatteries;

    public ChoosePropuslsorAction(List<Coordinates> coordinates, List<Integer> usedBatteries) {
        this.coordinates = new ArrayList<>(coordinates);
        this.usedBatteries = new ArrayList<>(usedBatteries);
    }

    public void execute(Player player) {
            for (int i = 0; i < usedBatteries.size(); i++) {
                player.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
            }
        player.move(player.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
    }

    public void endAction(Player player) {
        player.setActivity(0);
        int position = player.getRanking();

        if (player.getGame().getPlayers().size() == position){
            // porto tutti a flight
            player.getGame().getPlayers().stream().
                    forEach(p -> p.setState(ExPlayerState.FLIGHT));

            return;
        }
        player.getGame().getSortedPlayers().get(position +1).setActivity(1);
    }


}
