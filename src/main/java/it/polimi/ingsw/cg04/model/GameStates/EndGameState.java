package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;

public class EndGameState extends GameState{
    private Game game;

    public EndGameState(Game game) {
        this.game = game;
    }
}
