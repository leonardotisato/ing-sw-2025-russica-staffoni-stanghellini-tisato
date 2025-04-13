package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;

public class JoinGameAction implements InitAction {

    private final int gameId;
    private final String playerName;
    private final PlayerColor playerColor;

    public JoinGameAction(int gameId, String playerName, PlayerColor playerColor) {
        this.gameId = gameId;
        this.playerName = playerName;
        this.playerColor = playerColor;
    }

    @Override
    public void execute(GamesController controller) {
        Game g = controller.getGames().get(gameId);
        Player newPlayer = g.addPlayer(playerName, playerColor);

        controller.addConnectedPlayer(g, newPlayer);
        controller.addNicktoGame(playerName, g);
    }

    @Override
    public boolean checkAction(GamesController controller) {

        // check that game exists
        if (gameId < 0 || gameId >= controller.getGames().size() || controller.getGames().get(gameId) == null || controller.getGames().get(gameId).getId() != gameId) {
            return false;
        }

        // check nick availability
        if (playerName == null || playerName.isEmpty() || controller.isNickNameTaken(playerName)) {
            return false;
        }

        // check that the color is available
        Game g = controller.getGames().get(gameId);
        for (Player p : g.getPlayers()) {
            if (p.getColor() == playerColor) {
                return false;
            }
        }

        // check that game is not full
        if (g.getNumPlayers() >= g.getMaxPlayers()) {
            return false;
        }

        return true;
    }

    @Override
    public void execute(Player player) {
        throw new RuntimeException("Player does not exist");
    }

    @Override
    public boolean checkAction(Player player) {
        throw new RuntimeException("Player does not exist");
    }

    @Override
    public String getPlayerNickname() {
        return "";
    }
}
