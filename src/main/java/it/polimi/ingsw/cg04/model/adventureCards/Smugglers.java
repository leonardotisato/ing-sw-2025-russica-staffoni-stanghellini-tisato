package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.SmugglersState;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.HashMap;
import java.util.Map;

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

    public void solveEffect(Game game) {

        /*
        *
        * for each player
        *   player.calc_fire_power
        *   if(fire_power > smuggler_fire_power)
        *       if(player.wants_to_redeem)
        *           player.lose_days
        *           player.load_resources
        *       break
        *   if(fire_power == smuggler_fire_power)
        *       continue
        *   if(fire_power < smuggler_fire_power)
        *       player.lose_resources
        *
        */

    }

    @Override
    public AdventureCardState createState(Game game) {
        return new SmugglersState(game);
    }
}
