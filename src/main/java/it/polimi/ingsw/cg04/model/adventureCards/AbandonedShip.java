package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
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

    public void solveEffect(Player player, List<Integer> numCrewMembersLost, List<List<Integer>> coordinates) {
        Tile currTile;
        for (int i = 0; i < numCrewMembersLost.size(); i++) {
            currTile = player.getShip().getTile(coordinates.get(i).get(0), coordinates.get(i).get(1));
            if (currTile instanceof HousingTile) {
                player.getShip().removeCrew(CrewType.HUMAN, coordinates.get(i).get(0), coordinates.get(i).get(1), numCrewMembersLost.get(i));
            }
        }
        player.updateCredits(this.getEarnedCredits());
        player.move(-this.getDaysLost());
    }
}
