package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.buildRightPanel;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public class OpenSpaceState extends AdventureCardState {
    private final Map<Player, Integer> playerDelta = new HashMap<>();
    ;

    public OpenSpaceState(Game game) {
        super(game);
    }

    @Override
    public void usePropulsors(Player p, List<Coordinates> coordinates, List<Integer> usedBatteries) throws InvalidStateException {
        if (!p.equals(sortedPlayers.get(currPlayerIdx)))
            throw new InvalidStateException("Player" + p.getName() + " can't play, it's not his turn, player " + sortedPlayers.get(this.currPlayerIdx) + " should play");
        for (int i = 0; i < usedBatteries.size(); i++) {
            p.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
        }
        p.move(p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
        this.addLog("Player " + p.getName() + " moved forward by " + (p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2) + " positions!");
        playerDelta.put(p, p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
        this.played.set(currPlayerIdx, 1);
        this.currPlayerIdx++;
        if (currPlayerIdx == sortedPlayers.size()) {
            flagNoPowerOpenSpace();
            triggerNextState();
        }
    }

    private void flagNoPowerOpenSpace() {
        for (Player p : playerDelta.keySet()) {
            if (playerDelta.get(p) == 0) {
                context.getRetiredPlayers().add(p);
                context.getPlayers().remove(p);
                p.setRetired(true);
                this.appendLog("Player " + p.getName() + " is eliminated, he has propulsion power of 0 in open space!");
            }
        }
        playerDelta.clear();
    }

    @Override
    public void updateView (View view, Game toDisplay) throws IOException {
        view.renderOpenSpaceState(toDisplay);
    }
}
