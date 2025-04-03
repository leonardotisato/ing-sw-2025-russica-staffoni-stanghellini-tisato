package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.PlanetsState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Planets extends AdventureCard {
    @Expose
    private List<Map<BoxType, Integer>> planetReward;
    private List<Boolean> isOccupied;


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

    public void solveEffect(Player player, List<List<Integer>> coordinates, List<Map<BoxType, Integer>> boxes) {
        for (int i = 0; i < coordinates.size(); i++) {
            player.getShip().setBoxes(boxes.get(i), coordinates.get(i).get(0), coordinates.get(i).get(1));
        }
        // in the controller we must call movePlayer for each player who landed in reverse order
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new PlanetsState(game.getSortedPlayers(), this);
    }
}