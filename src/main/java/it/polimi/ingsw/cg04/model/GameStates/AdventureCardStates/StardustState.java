package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

public class StardustState extends AdventureCardState {

    public StardustState(Game game) {
        super(game);
    }


    public void starDust(Player player) throws InvalidStateException {
        if(player.getName().equals(sortedPlayers.getFirst().getName())) {
            for(int i = sortedPlayers.size() - 1; i >= 0; i--) {
                sortedPlayers.get(i).move(-sortedPlayers.get(i).getShip().getNumExposedConnectors());
                System.out.println("Player " + sortedPlayers.get(i).getName() + " perde " + sortedPlayers.get(i).getShip().getNumExposedConnectors() + " giorni di volo.");
            }
            triggerNextState();
        }
        else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }


}
