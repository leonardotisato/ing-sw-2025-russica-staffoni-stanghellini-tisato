package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

public class LobbyState extends GameState {
    Game game;

    public LobbyState(Game game) {
        this.game = game;
    }

    @Override
    public String render(String nickname) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        for (Player p : game.getPlayers()) {
            stringBuilder.append(p.getName()).append("\n");
        }
        stringBuilder.append("waiting for ").append(game.getMaxPlayers() - game.getPlayers().size()).append(" players to start the game\n");
        return stringBuilder.toString();
    }

}
