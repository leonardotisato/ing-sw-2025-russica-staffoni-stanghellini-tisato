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

    public Integer getFirePower() { return firePower; }
    public void setFirePower(int firePower) { this.firePower = firePower; }

    public int getLostGoods() { return lostGoods; }
    public void setLostGoods(int lostGoods) {
        this.lostGoods = lostGoods;
    }

    public Map<BoxType, Integer> getBoxes() { return boxes; }
    public void setReward(Map<BoxType, Integer> boxes) {
        this.boxes = boxes;
    }

    public void addReward(BoxType boxType, int quantity) {
        boxes.put(boxType, quantity);
    }

    public int getObtainedResourcesByType(BoxType boxType) {
        return boxes.get(boxType);
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new SmugglersState(game);
    }

    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("Required fire power: " + this.firePower, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Lost best boxes: " + this.lostGoods, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Earned boxes:", WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText(renderBoxes(), WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }

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
