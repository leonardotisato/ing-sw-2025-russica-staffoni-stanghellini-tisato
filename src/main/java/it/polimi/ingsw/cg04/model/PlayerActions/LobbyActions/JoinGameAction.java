package it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

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
    public boolean checkAction(GamesController controller) throws InvalidActionException {

        // check that game exists
        if (gameId < 0 || gameId >= controller.getGames().size() || controller.getGames().get(gameId) == null || controller.getGames().get(gameId).getId() != gameId) {
            throw new InvalidActionException("Game id " + gameId + " is not valid");
        }

        // check nick availability
        if (playerName == null || playerName.isEmpty() || controller.isNickNameTaken(playerName)) {
            throw new InvalidActionException("Player name " + playerName + " is not already taken");
        }

        // check that the color is available
        Game g = controller.getGames().get(gameId);
        for (Player p : g.getPlayers()) {
            if (p.getColor() == playerColor) {
                throw new InvalidActionException("Color " + playerColor + " is already taken");
            }
        }

        // check that game is not full
        if (g.getNumPlayers() >= g.getMaxPlayers()) {
            throw new InvalidActionException("Game chosen is full");
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
