package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;

public class GetNextAdventureCardAction implements PlayerAction {
    private Game game;
    public GetNextAdventureCardAction (Game game) {
        this.game = game;
    }
    public void execute(Player player) {
        game.getNextAdventureCard();
        game.setGameState(game.getCurrentAdventureCard().createState(game));
    }

    @Override
    public boolean checkAction(Player player) {
        return false;
    }
}
