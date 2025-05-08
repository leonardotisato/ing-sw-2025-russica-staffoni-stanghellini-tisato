package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.buildRightPanel;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public class OpenSpaceState extends AdventureCardState {
    private final Map<Player, Integer> playerDelta = new HashMap<>();;

    public OpenSpaceState(Game game) {  super(game);  }

    @Override
    public void usePropulsors(Player p, List<Coordinates> coordinates, List<Integer> usedBatteries) throws InvalidStateException {
        if (!p.equals(sortedPlayers.get(currPlayerIdx))) throw new InvalidStateException("Player" + p.getName() + " can't play, it's not his turn, player " + sortedPlayers.get(this.currPlayerIdx) + " should play");
        for (int i = 0; i < usedBatteries.size(); i++) {
            p.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
        }
        p.move(p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
        playerDelta.put(p, p.getShip().getBasePropulsionPower() + usedBatteries.stream().mapToInt(Integer::intValue).sum() * 2);
        this.played.set(currPlayerIdx, 1);
        this.currPlayerIdx++;
        if (currPlayerIdx == sortedPlayers.size()) {
            flagNoPowerOpenSpace();
            triggerNextState();
        }
    }

    private void flagNoPowerOpenSpace() {
        for(Player p : playerDelta.keySet()) {
            if(playerDelta.get(p) == 0) {
                context.getRetiredPlayers().add(p);
                context.getPlayers().remove(p);
            }
        }
        playerDelta.clear();
    }
    public String renderOld(String playerName) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append(super.render(playerName));
        List<String> shipPanel = new ArrayList<>(Arrays.asList(stringBuilder.toString().split("\n")));
        List<String> flightBoardPanel = new ArrayList<>(Arrays.asList(context.getBoard().draw().split("\n")));
        stringBuilder = new StringBuilder("\n");
        Player p = context.getPlayer(playerName);
        stringBuilder.append("It's ").append(currPlayerIdx == (p.getRanking() - 1) ? "your " : context.getPlayer(currPlayerIdx).getName()).append("turn").append("\n").append("\n");
        if (currPlayerIdx == (p.getRanking() - 1)) {
            List<Coordinates> propulsorCoordinates = p.getShip().getTilesMap().get("PropulsorTile");
            int totDoublePropulsor = (int)propulsorCoordinates.stream()
                    .map(coord -> p.getShip().getTile(coord.getX(), coord.getY()))
                    .filter(t -> t.isDoublePropulsor())
                    .count();
            stringBuilder.append("You have ").append(p.getShip().getNumBatteries()).append(" batteries").append("\n");
            stringBuilder.append("Your base propulsion power is ").append(p.getShip().getBasePropulsionPower()).append(" and you have ").append(totDoublePropulsor).append(" double propulsors.\n");
            stringBuilder.append("Send the batteries you want to use to increase your propulsion power").append("\n");
        }
        List<String> logsPanel = new ArrayList<>(Arrays.asList(stringBuilder.toString().split("\n")));
        List<String> rightPanel = new ArrayList<>();
        rightPanel.addAll(flightBoardPanel);
        rightPanel.addAll(logsPanel);
        return TuiDrawer.renderTwoColumnLayout(shipPanel, rightPanel, 40);
    }

    public String render(String playerName) {
        List<String> shipPanel = toLines(super.render(playerName));
        List<String> flightBoardPanel = new ArrayList<>(Arrays.asList(context.getBoard().draw().split("\n")));

        Player p = context.getPlayer(playerName);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("It's ").append(currPlayerIdx == (p.getRanking() - 1) ? "your " : context.getPlayer(currPlayerIdx).getName()).append("turn").append("\n");
        if (currPlayerIdx == (p.getRanking() - 1)) {
            List<Coordinates> propulsorCoordinates = p.getShip().getTilesMap().get("PropulsorTile");
            int totDoublePropulsor = (int)propulsorCoordinates.stream()
                    .map(coord -> p.getShip().getTile(coord.getX(), coord.getY()))
                    .filter(t -> t.isDoublePropulsor())
                    .count();
            stringBuilder.append("Batteries: " + p.getShip().getNumBatteries()).append("\n");
            stringBuilder.append("Base propulsion power: ").append(p.getShip().getBasePropulsionPower()).append("\n");
            stringBuilder.append("Double propulsor: ").append(totDoublePropulsor).append("\n");
            stringBuilder.append("Send batteries to increase propulsion power").append("\n");
        }

        int totalH = shipPanel.size();
        List<String> logsPanel = toLines(stringBuilder.toString());
        int rightWidth = Stream.concat(flightBoardPanel.stream(), logsPanel.stream())
                .mapToInt(String::length)
                .max()
                .orElse(0);                  // quanto spazio assegni alla colonna destra
        List<String> rightPanel = buildRightPanel(flightBoardPanel, logsPanel, totalH, rightWidth);
        return TuiDrawer.renderTwoColumnLayout(shipPanel, rightPanel, 40);
    }
}
