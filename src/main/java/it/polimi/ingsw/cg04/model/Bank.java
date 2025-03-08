package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    public int numCredits;
    public int numBatteries;
    public Map<BoxType, Integer> boxes;
    public int numHumans;
    public int numAliens;

    private Bank(int numCredits, int numBatteries, int numHumans, int numAliens, Map<BoxType, Integer> boxes) {
        this.numCredits = numCredits;
        this.numBatteries = numBatteries;
        this.numHumans = numHumans;
        this.numAliens = numAliens;
        this.boxes = new HashMap<>(boxes);
    }

    private void addCredits(int credits){
        this.numCredits += credits;
    }

    private void addBatteries(int batteries){
        this.numBatteries += batteries;
    }

    private void addHumans(int humans){
        this.numHumans += humans;
    }

    private void addAliens(int aliens){
        this.numAliens += aliens;
    }

    private int getNumCredits(){
        return this.numCredits;
    }

    private int getNumBatteries(){
        return this.numBatteries;
    }

    private int getNumHumans(){
        return this.numHumans;
    }

    private int getNumAliens(){
        return this.numAliens;
    }

    private Map<BoxType,Integer> getBoxes(){
        return this.boxes;
    }

    private void addBoxes(BoxType type, int num){
        boxes.put(type, boxes.get(type) + num);
    }

}
