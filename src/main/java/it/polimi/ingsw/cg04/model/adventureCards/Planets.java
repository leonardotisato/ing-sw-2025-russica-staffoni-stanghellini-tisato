package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.PlanetsState;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;

public class Planets extends AdventureCard {
    @Expose
    private List<Map<BoxType, Integer>> planetReward;
    private List<Boolean> isOccupied;


    public Planets() {
        super();
        planetReward = new ArrayList<>();
    }

    /**
     * Retrieves the list of planet rewards, where each planet reward is
     * represented as a map associating BoxType with its corresponding integer value.
     *
     * @return a list of maps, where each map contains BoxType keys and their associated integer values
     *         representing the reward for a specific planet.
     */
    public List<Map<BoxType, Integer>> getPlanetReward() {
        return planetReward;
    }

    public void setPlanetReward(List<Map<BoxType, Integer>> planetReward) {
        this.planetReward = planetReward;
    }

    public Map<BoxType, Integer> getSinglePlanetReward(int i) {
        return planetReward.get(i);
    }

    public int getSinglePlanetRewardByType(int i, BoxType boxType) {
        return planetReward.get(i).get(boxType);
    }

    public List<Boolean> getIsOccupied() {
        return isOccupied;
    }

    public void createListIsOccupied() {
        isOccupied = new ArrayList<>();
        int size = planetReward.size();
        for (int i = 0; i < size; i++) {
            isOccupied.add(false);
        }
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new PlanetsState(game);
    }

    /**
     * Draws the formatted content of the Planets adventure card into the provided StringBuilder.
     *
     * @param sb the StringBuilder instance where the formatted card content will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        for (int i = 0; i < this.planetReward.size(); i++) {
            sb.append("│ ").append(centerText("Planet " + i + " → " + renderPlanetBoxes(i), WIDTH - 4)).append(" │").append("\n");
        }
        sb.append("│ ").append(centerText("Lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }

    /**
     * Renders a formatted string representing the box types and their associated values
     * for the specified planet index.
     *
     * @param planetIdx the index of the planet whose box types and values are to be rendered
     * @return a string representing contained boxTypes
     */
    public String renderPlanetBoxes(Integer planetIdx) {
        StringBuilder sb = new StringBuilder();
        Iterator<BoxType> it = planetReward.get(planetIdx).keySet().iterator();
        while (it.hasNext()) {
            BoxType boxType = it.next();
            sb.append(boxType.toString().charAt(0))
                    .append(": ")
                    .append(planetReward.get(planetIdx).get(boxType));

            sb.append(it.hasNext() ? ", " : "");  // virgola o punto
        }
        return sb.toString();
    }
}