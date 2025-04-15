package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

public class StardustState extends AdventureCardState {

    public StardustState(Game game) {
        super(game);
    }


    public void starDust(Player player) {
        if(player.getName().equals(sortedPlayers.getFirst().getName())) {
            for(int i = sortedPlayers.size() - 1; i >= 0; i--) {
                sortedPlayers.get(i).move(-sortedPlayers.get(i).getShip().getNumExposedConnectors());
                System.out.println("Player " + sortedPlayers.get(i).getName() + " perde " + sortedPlayers.get(i).getShip().getNumExposedConnectors() + " giorni di volo.");
            }
            triggerNextState();
        }
        else {
            System.out.println("It's not " + player.getName() + " turn.");
        }
    }


}
