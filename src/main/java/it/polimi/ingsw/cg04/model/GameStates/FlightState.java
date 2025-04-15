package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

import java.util.List;

public class FlightState extends GameState {
    public void getNextAdventureCard(Player player){
        Game game = player.getGame();
        game.getNextAdventureCard();
        game.getCurrentAdventureCard().createState(game);
    }
}
