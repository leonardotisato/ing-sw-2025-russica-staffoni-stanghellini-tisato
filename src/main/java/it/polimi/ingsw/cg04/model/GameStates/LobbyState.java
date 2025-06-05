package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.model.Game;
import java.io.IOException;

public class LobbyState extends GameState {
    Game game;

    public LobbyState(Game game) {
        this.game = game;
    }

    @Override
    public void updateView (View view, Game toDisplay) throws IOException {
        view.renderLobbyState(toDisplay);
    }
}
