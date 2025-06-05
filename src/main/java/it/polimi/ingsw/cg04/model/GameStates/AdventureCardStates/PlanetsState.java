package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
import java.util.*;

public class PlanetsState extends AdventureCardState {
    Map<Player, Integer> chosenPlanets;
    Boolean allPlanetsChosen;
    List<Player> currPlayers;

    public PlanetsState(Game game) {
        super(game);
        this.allPlanetsChosen = false;
        this.chosenPlanets = new HashMap<>();
    }

    public Map<Player, Integer> getChosenPlanets() {
        return chosenPlanets;
    }

    public Boolean getAllPlanetsChosen() {
        return allPlanetsChosen;
    }

    public void setAllPlanetsChosen(Boolean allPlanetsChosen) {
        this.allPlanetsChosen = allPlanetsChosen;
    }

    @Override
    public List<Player> getCurrPlayers() {
        return currPlayers;
    }

    public boolean allPlanetsChosen() {
        return allPlanetsChosen;
    }

    public void landToPlanet(Player player, Integer planetIdx, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) throws InvalidStateException {
        if (!player.equals(sortedPlayers.get(currPlayerIdx)))
            throw new InvalidStateException("Player" + player.getName() + " can't play, it's not his turn, player " + sortedPlayers.get(this.currPlayerIdx) + " should play");
        if (chosenPlanets.containsValue(planetIdx))
            throw new InvalidStateException("Planet" + planetIdx + " is already chosen");
        if (planetIdx != null && (planetIdx < 0 || planetIdx >= card.getPlanetReward().size()))
            throw new InvalidStateException("Planet index" + planetIdx + " is out of range, it should be in range [0, " + (card.getPlanetReward().size() - 1) + "]");
        if (planetIdx != null) {
            checkRightBoxesAfterReward(player, planetIdx, coordinates, boxes);
            chosenPlanets.put(player, planetIdx);
            for (int i = 0; i < coordinates.size(); i++) {
                player.getShip().setBoxes(boxes.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
            }
            this.addLog("Player " + player.getName() + " has landed on planet " + chosenPlanets.get(player));
        } else {
            this.addLog("Player " + player.getName() + " decided not to land on a planet.");
        }
        played.set(sortedPlayers.indexOf(player), 1);
        currPlayerIdx++;
        if (chosenPlanets.size() == card.getPlanetReward().size() || currPlayerIdx == sortedPlayers.size()) {
            for (int i = sortedPlayers.size() - 1; i >= 0; i--) {
                if (chosenPlanets.containsKey(sortedPlayers.get(i))) {
                    sortedPlayers.get(i).move(-card.getDaysLost());
                    this.appendLog("Player " + sortedPlayers.get(i).getName() + " lost " + card.getDaysLost() + " days of flight.");
                }
            }
            triggerNextState();
        }
    }

    public boolean checkRightBoxesAfterReward(Player player, Integer planetIdx, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes) throws InvalidStateException {
        //the player doesn't want to play this card
        if (coordinates == null && boxes == null) return true;
        Map<BoxType, Integer> newTotBoxes = new HashMap<>(Map.of(BoxType.RED, 0, BoxType.BLUE, 0, BoxType.YELLOW, 0, BoxType.GREEN, 0));
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinates coord = coordinates.get(i);
            Map<BoxType, Integer> boxesAtCoord = boxes.get(i);
            for (Map.Entry<BoxType, Integer> entry : boxesAtCoord.entrySet()) {
                BoxType type = entry.getKey();
                Integer count = entry.getValue();
                newTotBoxes.put(type, newTotBoxes.getOrDefault(type, 0) + count);
            }
        }
        //newTotBoxes contains the updated amount of boxes type-wise
        for (Map.Entry<BoxType, Integer> entry : newTotBoxes.entrySet()) {
            BoxType type = entry.getKey();
            Integer count = entry.getValue();
            if (card.getPlanetReward().get(planetIdx).containsKey(type)) {
                //number of boxes of a specific type should be < old number (type) + reward(type)
                if (count > player.getShip().getBoxes(type) + card.getPlanetReward().get(planetIdx).get(type)) {
                    throw new InvalidStateException("The new map of boxes for " + player.getName() + " is incorrect, there are too many " + type + " boxes");
                }
            } else {
                //if there is no box in the reward of this type, the new number of box(type) should be <= the old number of box(type)
                if (count > player.getShip().getBoxes(type)) {
                    throw new InvalidStateException("The new map of boxes for " + player.getName() + " is incorrect, there are too many " + type + " boxes");
                }
            }
        }
        return true;
    }

    @Override
    public void updateView (View view, Game toDisplay) throws IOException {
        view.renderPlanetsState(toDisplay);
    }
}
