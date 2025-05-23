package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.RemoveCrewAction;

import java.util.List;

public class AbandonedShipState extends AdventureCardState {

    public AbandonedShipState(Game game) {
        super(game);
    }


    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) throws InvalidStateException {
        if (!player.equals(sortedPlayers.get(this.currPlayerIdx)))
            throw new InvalidStateException("Player" + player.getName() + " can't play, it's not his turn, player " + sortedPlayers.get(this.currPlayerIdx) + " should play");
        if (numCrewMembersLost != null && numCrewMembersLost.stream().mapToInt(Integer::intValue).sum() != card.getLostMembers())
            throw new InvalidStateException("The number of crew sent by Player " + player.getName() + " is incorrect for this card, he should send " + card.getLostMembers() + " crew members");
        if (numCrewMembersLost == null) {
            this.addLog("Player " + player.getName() + " decided not to play this card.");
            played.set(currPlayerIdx, 1);
            currPlayerIdx++;
        } else {
            for (int i = 0; i < numCrewMembersLost.size(); i++) {
                Tile currTile = player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY());
                player.getShip().removeCrew(currTile.getHostedCrewType(), coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
            }
            played.replaceAll(ignored -> 1);
            player.updateCredits(card.getEarnedCredits());
            player.move(-card.getDaysLost());
            this.addLog("Player " + player.getName() + " decided to play this card. He earned " + card.getEarnedCredits() + " credits. He lost " + card.getLostMembers() + " crew members and " + card.getDaysLost() + " flight days.");
        }
        if (!played.contains(0)) {
            triggerNextState();
        }
    }

    public String render(String playerName) {
        StringBuilder stringBuilder = new StringBuilder(super.render(playerName));
        stringBuilder.append("\n".repeat(3));
        Player p = context.getPlayer(playerName);
        stringBuilder.append("It's ").append(currPlayerIdx == (p.getRanking() - 1) ? "your " : context.getPlayer(currPlayerIdx).getName()).append(" turn").append("\n");
        if (currPlayerIdx == (p.getRanking() - 1)) {
            stringBuilder.append("You have ").append(context.getPlayer(playerName).getShip().getNumCrew()).append(" crew members.").append("\n");
            stringBuilder.append("Type 'removeCrew' to trade crew members for credits.").append("\n");
            stringBuilder.append("Note that you will lose " + card.getDaysLost() + " days of flight if you remove any crew members.").append("\n");
        }
        return stringBuilder.toString();
    }
}

