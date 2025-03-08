package it.polimi.ingsw.cg04.model.ships;
import it.polimi.ingsw.cg04.model.enumerations.AlienColor;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.components.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;

public abstract class Ship {

    private int numHumans = 0;
    private int numAliens = 0;
    private int numBatteries = 0;
    private int numBrokenComponents = 0;
    private Map<AlienColor, Integer> aliens;
    private Map<BoxType, Integer> boxes;
    private int numExposedConnectors;
    private List<Direction> protectedDirections;
    private Component[] componentsBuffer;
    private int baseFirePower = 0;
    private int basePropulsionPower = 0;
    private Component[][] componentsMatrix;

    public int getNumBrokenComponents() {
        return numBrokenComponents;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public int getNumAliens() {
        return numAliens;
    }

    public int getNumAliens(AlienColor color) {
        return aliens.get(color);
    }

    public int getNumHumans() {
        return numHumans;
    }

    public  int getNumCrew() {
        return this.getNumHumans() + this.getNumAliens();
    }

    public int getNumExposedConnectors() {
        return numExposedConnectors;
    }

    public List<Direction> getProtectedDirections() {
        return protectedDirections;
    }

    public Component[] getComponentsBuffer() {
        return componentsBuffer;
    }

    public Map<AlienColor, Integer> getAliens() {
        return aliens;
    }

    // do we need this?
    public Map<BoxType, Integer> getBoxes() {
        return boxes;
    }

    public int getBoxes(BoxType type) {
        return boxes.get(type);
    }

    public int getBaseFirePower() {
        return baseFirePower;
    }

    public int getBasePropulsionPower() {
        return basePropulsionPower;
    }

    public void removeHumans(int lostHumans) {

        // todo: handle this gracefully
        assert this.getNumHumans() - lostHumans >= 0;

        numHumans -= lostHumans;
    }

    public void removeBatteries(int lostBatteries) {
        assert this.getNumBatteries() - lostBatteries >= 0;
        numBatteries -= lostBatteries;
    }

    public void removeAliens(AlienColor color, int lostAliens) {
        numAliens -= lostAliens;

        // todo: handle this gracefully
        assert aliens.get(color) != null;
        assert this.getNumAliens(color) - lostAliens >= 0;

        aliens.put(color, aliens.get(color) - lostAliens);
    }

    // this method remove 'lostBoxes' boxes, prioritizing high value boxes, then removes battery. As stated in the game rules
    public void removeBoxes(int lostBoxes) {
        int yetToRemove = lostBoxes;
        List<BoxType> sortedTypes = this.boxes.keySet().stream().sorted(Comparator.comparing(BoxType::getPriority).reversed()).toList();

        for (BoxType type : sortedTypes) {
            if (yetToRemove > 0) {
                yetToRemove -= this.removeBoxes(type, yetToRemove);
            }
        }

        if (yetToRemove > 0) {
            this.removeBatteries(yetToRemove);
        }
    }

    // returns the number of boxes that were removed
    public int removeBoxes(BoxType type, int lostBoxes) {
        Integer currentCount = this.boxes.get(type);

        if (currentCount == null) {
            return 0;
        }

        if (currentCount >= lostBoxes) {
            this.boxes.put(type, currentCount - lostBoxes);
            return lostBoxes;
        }

        this.boxes.put(type, 0);
        return currentCount;
    }

    public boolean isShipLegal(){
        return false;
    }

    public int calcTotalPropulsionPower(int usedBatteries) {
        return -1;
    }

    public int calcTotalFirePower(int usedBatteries) {
        return -1;
    }

    public boolean placeComponent(Component component, int x, int y) {
        return false;
    }

    // todo: change Meteor and Shot enums into Attacks enum which includes both...
    // the method should return true if component was broken, so state of the ship can be updated
    public boolean handleAttack(Direction dir, AttackType attack){
        return true;
    }
}
