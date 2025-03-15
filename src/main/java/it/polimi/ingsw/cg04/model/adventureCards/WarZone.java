package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.Shot;

import java.util.List;

public class WarZone extends AdventureCard {

    @Expose
    private int[][] critPenalty;

    public WarZone() {
        super();
    }

    public int[][] getCritPenalty(){
        return critPenalty;
    }


    public int getParam(int i) {
        return critPenalty[i][0];
    }

    public int getPenalty(int i) {
        return critPenalty[i][1];
    }

    public int getPenaltyAmount(int i) {
        return critPenalty[i][2];
    }
    // matrice 3x3
    // prima riga -> tipo di parametro
    // seconda riga -> tipo di penalità
    // terza riga -> quantità della penalità
    // partendo dalla prima colonna ricavo il worstPlayer e gli assegno la relativa penalità poi passo alla colonna successiva
    // prima di continuare con il resto bisogna capire come gestire la scelta del giocatore di quali membri dell equipaggio rimuovere,
    // quali box rimuovere, decidere se usare le batterie per aumentare propulsione e potenza di fuoco
    // il punto critico è come gestire le cannonate, siccome dalle immagini ci sono solo 2 tipi di attacchi io li gestirei individualmente,
    // e le distinguerei da PenaltyAmount (es: getPenaltyAmount == 1 --> è il primo attacco, getPenaltyAmount == 2 --> è il secondo attacco)
    // todo testing
    public void solveEffect(Game game) {
        for(int i = 0; i<3; i++){
            List<Player> players = game.getPlayers();
            Player worstPlayer = players.get(0);

            switch (getParam(i)) {
                case 0:
                    // calcola worstPlayer in base alla dimensione dell'equipaggio
                    for (Player player : players) {
                        if (player.getShip().getNumCrew() < worstPlayer.getShip().getNumCrew()) {
                            worstPlayer = player;
                        }
                    }
                    break;
                case 1:
                    // todo: propulsione, capire come implementare scelta uso batterie

                    break;
                case 2:
                    // todo: potenza di fuoco, capire come implementare scelta uso batterie
                    break;
                default:
                    throw new IllegalArgumentException("Illegal parameter");

            }

            switch (getPenalty(i)) {
                case 0:
                    worstPlayer.move(-(getPenaltyAmount(i)));
                    break;
                case 1:
                    // todo casse perse
                    break;
                case 2:
                    // todo equipaggio perso
                    break;
                case 3:
                    // todo cannonata
                    break;
                default:
                    throw new IllegalArgumentException("Illegal parameter");
            }
        }
    }
}
