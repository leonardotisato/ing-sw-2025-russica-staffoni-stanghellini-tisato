package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandleBoxesAction implements PlayerAction{
    List<java.util.List<Integer>> coordinates;
    List<Map<BoxType,Integer>> boxes;
    public HandleBoxesAction(List<List<Integer>> coordinates, List<Map<BoxType,Integer>> boxes) {
        this.coordinates = new ArrayList<>(coordinates);
        this.boxes = new ArrayList<>(boxes);
    }

    public void execute(Player player) {
        if (boxes == null) {
            endAction(player);
            return;
        }
        if (player.getShip().getNumCrew() >= player.getGame().getCurrentAdventureCard().getMembersNeeded()) {
            for (int i = 0; i < coordinates.size(); i++) {
                player.getShip().setBoxes(boxes.get(i), coordinates.get(i).get(0), coordinates.get(i).get(1));
            }
        }
        else throw new RuntimeException("not enough crew");
        endAction(player);
    }

    public void endAction(Player player) {
        int position = player.getRanking();
        player.setActivity(0);
        if (boxes == null) {
            if (player.getGame().getPlayers().size() == position) {
                // porto tutti a flight
                player.getGame().getPlayers().stream().
                        forEach(p -> p.setState(ExPlayerState.FLIGHT));
            }
            else {
                player.getGame().getSortedPlayers().get(position + 1).setActivity(1);
            }
        }
        else {
            player.move(-player.getGame().getCurrentAdventureCard().getDaysLost());
            player.getGame().getPlayers().stream().
                    forEach(p -> p.setState(ExPlayerState.FLIGHT));
        }
    }
}
