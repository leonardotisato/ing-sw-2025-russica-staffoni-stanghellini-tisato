package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.buildRightPanel;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public class StardustState extends AdventureCardState {

    public StardustState(Game game) {
        super(game);
    }


    public void starDust(Player player) throws InvalidStateException {
        if(player.getName().equals(sortedPlayers.getFirst().getName())) {
            for(int i = sortedPlayers.size() - 1; i >= 0; i--) {
                sortedPlayers.get(i).move(-sortedPlayers.get(i).getShip().getNumExposedConnectors());
                if (i == sortedPlayers.size() - 1) {
                    this.addLog("Player " + sortedPlayers.get(i).getName() + " perde " + sortedPlayers.get(i).getShip().getNumExposedConnectors() + " giorni di volo.");
                } else {
                    this.appendLog("Player " + sortedPlayers.get(i).getName() + " perde " + sortedPlayers.get(i).getShip().getNumExposedConnectors() + " giorni di volo.");
                }
            }
            triggerNextState();
        }
        else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    public String render(String playerName){
        StringBuilder stringBuilder = new StringBuilder(super.render(playerName));
        stringBuilder.append("\n".repeat(3));
        stringBuilder.append("Send 'stardust' to solve stardust effect and continue the game.");
        stringBuilder.append("\n");
        stringBuilder.append("You may lose some days of flight!").append("\n");
        return stringBuilder.toString();
    }


}
