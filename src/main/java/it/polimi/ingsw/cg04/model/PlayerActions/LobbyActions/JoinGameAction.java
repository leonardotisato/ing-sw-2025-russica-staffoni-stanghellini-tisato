package it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

public class JoinGameAction extends InitAction {

    private final int gameId;
    private final PlayerColor playerColor;

    public JoinGameAction(int gameId, String playerName, PlayerColor playerColor) {
        super(playerName);
        this.gameId = gameId;
        this.playerColor = playerColor;
    }

    @Override
    public void execute(GamesController controller) {
        Game g = controller.getGames().get(gameId);
        Player newPlayer = g.addPlayer(nickname, playerColor);

        controller.addConnectedPlayer(g, newPlayer);
        controller.addNicktoGame(nickname, g);
        this.addLog(nickname + " joined this game!");

        if (g.getPlayers().size() == g.getMaxPlayers()) {
            g.setGameState(new BuildState(g));
            g.setCurrentAdventureCard(null);
            this.addLog("Number of players required has been reached. It's time to start!");
        }
    }

    @Override
    public boolean checkAction(GamesController controller) throws InvalidActionException {

        // check that game exists
        if (gameId < 0 || gameId >= controller.getGames().size() || controller.getGames().get(gameId) == null || controller.getGames().get(gameId).getId() != gameId) {
            throw new InvalidActionException("Game id " + gameId + " is not valid");
        }

        // check nickname was set correctly
        if (nickname == null) {
            throw new InvalidActionException("Set your nickname first!");
        }

        if (nickname.isEmpty()) {
            throw new InvalidActionException("Invalid nickname");
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

        // check that the player is still not associated with any game
        if (controller.getGameFromNickname(nickname) != null) {
            throw new InvalidActionException("You are already in a game!");
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
}
