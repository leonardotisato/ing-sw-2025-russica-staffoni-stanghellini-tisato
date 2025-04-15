package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AbandonedShipState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.util.List;

public class AbandonedShip extends AdventureCard {
    @Expose
    private int lostMembers;
    @Expose
    private int earnedCredits;

    public AbandonedShip() {
        super();
    }

    public Integer getLostMembers() { return lostMembers; }
    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    public Integer getEarnedCredits() { return earnedCredits; }
    public void setEarnedCredits(int earnedCredits) {
        this.earnedCredits = earnedCredits;
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new AbandonedShipState(game);
    }
}
