package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.SmugglersState;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;

public class Smugglers extends AdventureCard {
    @Expose
    private int firePower;
    @Expose
    private int lostGoods;
    @Expose
    private Map<BoxType, Integer> boxes;

    public Smugglers() {
        super();
        this.boxes = new HashMap<BoxType, Integer>();
    }

    /**
     * Retrieves the firepower value associated with the smuggler or pirate card.
     *
     * @return the firepower as an Integer
     */
    @Override
    public Integer getFirePower() {
        return firePower;
    }

    /**
     * Sets the firepower level for the adventure card.
     *
     * @param firePower the firepower value to be assigned
     */
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    /**
     * Retrieves the number of lost goods associated with the smuggler or pirate card.
     *
     * @return the number of lost goods as an integer
     */
    @Override
    public int getLostGoods() {
        return lostGoods;
    }

    public void setLostGoods(int lostGoods) {
        this.lostGoods = lostGoods;
    }

    /**
     * Retrieves the collection of boxes associated with their respective types and quantities.
     *
     * @return a map where the keys are instances of BoxType and the values are integers representing the quantities of the respective box types
     */
    @Override
    public Map<BoxType, Integer> getBoxes() {
        return boxes;
    }

    public void setReward(Map<BoxType, Integer> boxes) {
        this.boxes = boxes;
    }

    public void addReward(BoxType boxType, int quantity) {
        boxes.put(boxType, quantity);
    }

    /**
     * Retrieves the quantity of resources obtained for a specific type of box.
     *
     * @param boxType the type of box resource to retrieve the quantity for
     * @return the quantity of resources associated with the specified box type as an integer
     */
    @Override
    public int getObtainedResourcesByType(BoxType boxType) {
        return boxes.get(boxType);
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new SmugglersState(game);
    }

    /**
     * Draws the content of the Smugglers adventure card into the provided StringBuilder.
     * The method appends various card details such as required firepower, lost goods,
     * earned boxes, and lost flight days.
     *
     * @param sb the StringBuilder instance where the formatted card content will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("Required fire power: " + this.firePower, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Lost best boxes: " + this.lostGoods, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Earned boxes:", WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText(renderBoxes(), WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }

    /**
     * Renders the obtained resources as a formatted string
     *
     * @return a string representation of the obtained resources
     */
    public String renderBoxes() {
        StringBuilder sb = new StringBuilder();
        Iterator<BoxType> it = boxes.keySet().iterator();
        while (it.hasNext()) {
            BoxType boxType = it.next();
            sb.append(boxType.toString().charAt(0))
                    .append(": ")
                    .append(boxes.get(boxType));

            sb.append(it.hasNext() ? ", " : "");  // virgola o punto
        }
        return sb.toString();
    }
}
