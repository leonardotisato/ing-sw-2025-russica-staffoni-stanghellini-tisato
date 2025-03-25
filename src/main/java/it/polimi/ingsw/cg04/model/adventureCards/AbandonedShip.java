package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.tiles.AlienSupportTile;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbandonedShip extends AdventureCard {
    @Expose
    private int lostMembers;
    @Expose
    private int earnedCredits;

    public AbandonedShip() {
        super();
    }

    public int getLostMembers() { return lostMembers; }
    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    public int getEarnedCredits() { return earnedCredits; }
    public void setEarnedCredits(int earnedCredits) {
        this.earnedCredits = earnedCredits;
    }

    public void solveEffect(Player player, Map<Integer, List<Integer>> crewLost) {
        List<Integer> coordinates = new ArrayList<>();
        Tile currTile;
        for (int numCrew : crewLost.keySet()) {
            coordinates.addAll(crewLost.get(numCrew));
            currTile = player.getShip().getTile(coordinates.get(0), coordinates.get(1));
            if (currTile instanceof HousingTile || currTile instanceof AlienSupportTile) {
                currTile.removeCrewMember(numCrew);
            }
        }
        player.updateCredits(this.getEarnedCredits());
        player.move(-this.getDaysLost());
        


    }
}
