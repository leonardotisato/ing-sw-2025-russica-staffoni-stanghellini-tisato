package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;

public class OpenSpaceState extends AdventureCardState {

    public OpenSpaceState(Game game) {
        super(game);
    }

    @Override
    public void usePropulsors(Player p, List<Coordinates> coordinates, List<Integer> usedBatteries) throws InvalidStateException {
        if (!p.equals(sortedPlayers.get(currPlayerIdx))) throw new InvalidStateException("Player" + p.getName() + " can't play, it's not his turn, player " + sortedPlayers.get(this.currPlayerIdx) + " should play");
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

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append(super.render(playerName));
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
        return stringBuilder.toString();
    }
}
