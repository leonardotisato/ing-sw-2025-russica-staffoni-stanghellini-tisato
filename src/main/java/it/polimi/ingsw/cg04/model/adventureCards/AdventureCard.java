package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.HashMap;
import java.util.Map;

public abstract class AdventureCard {

    @Expose private String type;
    @Expose private int cardLevel;
    @Expose private int daysLost;

    public AdventureCard(){
    }

    public abstract void solveEffect(Game game);

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getCardLevel() {
        return cardLevel;
    }
    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getDaysLost() {
        return daysLost;
    }
    public void setDaysLost(int daysLost) {
        this.daysLost = daysLost;
    }


}
