package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Planets extends AdventureCard {
    @Expose
    private List<Map<BoxType, Integer>> planetReward;

    public Planets() {
        super();
        planetReward = new ArrayList<>();
    }

    public List<Map<BoxType, Integer>> getPlanetReward() {
        return planetReward;
    }
    public void setPlanetReward(List<Map<BoxType, Integer>> planetReward) {
        this.planetReward = planetReward;
    }

    public Map<BoxType, Integer> getSinglePlanetReward(int i) {
        return planetReward.get(i);
    }

    // potrebbe servire ad esempio per controllare quanti rossi ci sono e se il giocatore può trasportarli?
    // nel dubbio lo tengo per ora
    public int getSinglePlanetRewardByType(int i, BoxType boxType) {
        return planetReward.get(i).get(boxType);
    }

    public void solveEffect(Game game) {
        // add effect
    }
}