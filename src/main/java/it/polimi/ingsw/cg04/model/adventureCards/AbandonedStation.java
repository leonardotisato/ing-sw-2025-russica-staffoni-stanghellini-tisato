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
    @Expose
    private int membersNeeded;
    @Expose
    private Map<BoxType, Integer> obtainedResources;


    public AbandonedStation() {
        super();
        this.obtainedResources = new HashMap<>();
    }

    /**
     *
     * @return the required number of members as an integer.
     */
    @Override
    public int getMembersNeeded() {
        return membersNeeded;
    }

    /**
     *
     * @param membersNeeded the number of members required, represented as an integer
     */
    @Override
    public void setMembersNeeded(int membersNeeded) {
        this.membersNeeded = membersNeeded;
    }

    /**
     * Retrieves the resources obtained from the abandoned station.
     *
     * @return a map where the keys represent the types of resources (BoxType)
     *         and the values represent the quantity obtained for each type.
     */
    @Override
    public Map<BoxType, Integer> getObtainedResources() {
        return obtainedResources;
    }

    public void setObtainedResources(Map<BoxType, Integer> obtainedResources) {
        this.obtainedResources = obtainedResources;
    }

    /**
     * Retrieves the quantity of resources obtained for a specific boxType.
     *
     * @param boxType the type of resource for which the obtained quantity should be retrieved
     * @return the quantity of resources obtained for the specified boxType
     */
    @Override
    public int getObtainedResourcesByType(BoxType boxType) {
        return obtainedResources.get(boxType);
    }

    /**
     * Creates the state associated with the adventure card.
     *
     * @param game the game instance that provides the context for the adventure card state
     * @return the newly created instance of {@code AbandonedShipState} which represents the state of the current adventure card
     */
    @Override
    public AdventureCardState createState(Game game) {
        return new AbandonedStationState(game);
    }

    // toString methods

    /**
     * Draws the content of the {@code AbandonedShip} card
     *
     * @param sb the {@code StringBuilder} to which the content will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("Required crew: " + this.membersNeeded, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Earned boxes:", WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText(renderBoxes(), WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }

    /**
     * Renders the obtained resources as a formatted string, where each entry consists of the
     * first character of the resource type name, followed by the associated quantity.
     *
     * @return a string representation of the obtained resources
     */
    public String renderBoxes() {
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