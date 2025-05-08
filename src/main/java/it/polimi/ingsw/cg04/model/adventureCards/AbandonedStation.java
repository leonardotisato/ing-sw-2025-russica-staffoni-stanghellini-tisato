package it.polimi.ingsw.cg04.model.adventureCards;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AbandonedStationState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;

public class AbandonedStation extends AdventureCard {
    @Expose private int membersNeeded;
    @Expose private Map<BoxType, Integer> obtainedResources;


    public AbandonedStation() {
        super();
        this.obtainedResources = new HashMap<>();
    }

    public int getMembersNeeded() {
        return membersNeeded;
    }
    public void setMembersNeeded(int membersNeeded) {
        this.membersNeeded = membersNeeded;
    }
    public Map<BoxType, Integer> getObtainedResources() {
        return obtainedResources;
    }
    public void setObtainedResources(Map<BoxType, Integer> obtainedResources) {
        this.obtainedResources = obtainedResources;
    }

    public int getObtainedResourcesByType(BoxType boxType) {
        return obtainedResources.get(boxType);
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new AbandonedStationState(game);
    }

    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("Required crew: " + this.membersNeeded, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Earned boxes:", WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText(renderBoxes(), WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }

    public String renderBoxes(){
        StringBuilder sb = new StringBuilder();
        Iterator<BoxType> it = obtainedResources.keySet().iterator();
        while (it.hasNext()) {
            BoxType boxType = it.next();
            sb.append(boxType.toString().charAt(0))
                    .append(": ")
                    .append(obtainedResources.get(boxType));

            sb.append(it.hasNext() ? ", " : "");  // virgola o punto
        }
        return sb.toString();
    }
}