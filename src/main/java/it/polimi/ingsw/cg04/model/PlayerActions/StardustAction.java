package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;

public class StardustAction implements PlayerAction {

    public void execute(Player player) {
        player.move(-player.getShip().getNumExposedConnectors());

        endAction(player);
    }

    public void endAction(Player player) {
        player.setActivity(0);

        // if(player.getName() == ){}

        int position = player.getRanking();


        if (player.getGame().getPlayers().size() == position){
            // porto tutti a flight
            player.getGame().getPlayers().
                    forEach(p -> p.setState(ExPlayerState.FLIGHT));

            return;
        }
        player.getGame().getSortedPlayers().get(position +1).setActivity(1);
    }
}
