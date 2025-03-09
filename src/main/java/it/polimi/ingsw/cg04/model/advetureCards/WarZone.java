package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.Game;

public class WarZone extends AdventureCard {

    // come gestire subisci attacco? enum per i tipi di attacco siccome sono solo 2?
    // se ho modo di capire quale dei due attacchi arriva tramite un intero allora posso gestire easy
    // come funzionano le cannonate? in che ordine arrivano? (sembra prima quelle leggere poi quelle grosse)
    // lancio dei dadi e controllo matrice
    private final int[][] critPenalty;

    public WarZone(int cardLevel, int daysLost, int[][] critPenalty) {
        super(cardLevel, daysLost);
        this.critPenalty = critPenalty;
    }

    public int[][] getCritPenalty(){
        return critPenalty;
    }

    public void solveEffect(Game game) {
        // add effect
    }
}
