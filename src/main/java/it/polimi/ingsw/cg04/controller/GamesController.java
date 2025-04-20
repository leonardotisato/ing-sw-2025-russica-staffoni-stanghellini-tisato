package it.polimi.ingsw.cg04.controller;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesController {

    private final List<Game> games;
    private final Map<Game, List<Player>> connectedPlayers;
    private final Map<Game, List<Player>> disconnectedPlayers;
    private final Map<String, Game> nickToGame;
    private final List<Player> connectedPlayersList;

    public GamesController() {
        games = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        disconnectedPlayers = new HashMap<>();
        nickToGame = new HashMap<>();
        connectedPlayersList = new ArrayList<>();
    }

    public List<Game> getGames() {
        return games;
    }

    public void onActionReceived(PlayerAction action) {

        // System.out.println("Handling action");

        Game g = nickToGame.get(action.getPlayerNickname());

        // handle unknown players
//        if (g == null) {
//            try {
//                ((InitAction) action).checkAction(this);
//                ((InitAction) action).execute(this);
//                return;
//            } catch (InvalidActionException e) {
//                System.out.println(e.getReason());
//            }
//        }

        // handle known players
        Player p = g.getPlayer(action.getPlayerNickname());
        try {
            action.checkAction(p);
            action.execute(p);
        } catch (InvalidActionException e) {
            System.out.println(e.getReason());
        } catch (InvalidStateException e){
            System.out.println(e.getReason());
        }
    }

    public void onInitActionReceived(InitAction action){
        try {
            action.checkAction(this);
            action.execute(this);
            return;
        } catch (InvalidActionException e) {
            System.out.println(e.getReason());
        }
    }

    public void addGame(Game game) {
        games.add(game);
        connectedPlayers.put(game, new ArrayList<>());
        for (Player p : game.getPlayers()) {
            nickToGame.put(p.getName(), game);
            connectedPlayers.get(game).add(p);
        }
    }

    public List<Integer> getJoinableGames() {
        List<Integer> joinableGames = new ArrayList<>();

        for (Game g : games) {
            if (!g.hasStarted() && connectedPlayers.get(g).size() < g.getMaxPlayers()) {
                joinableGames.add(g.getId());
            }
        }

        return joinableGames;
    }


    public boolean isNickNameTaken(String nickname) {
        return nickToGame.containsKey(nickname);
    }

    public void addConnectedPlayer(Game game, Player player) {
        connectedPlayers.get(game).add(player);
    }

    public void addNicktoGame(String nickname, Game game) {
        nickToGame.put(nickname, game);
    }

//    public void beginGame(Game game) {
//        game.beginGame();
//    }
//
//    public void endGame(Game game) {
//        game.endGame();
//    }
//
//
//    public void createGame(int level) {
//    }
//
//    public void reconnect(Game game, Player player) {
//    }
//
//    public void disconnect(Game game, Player player) {
//    }
//
//    public void addPlayer(Game game, String playerName, Player player, PlayerColor color) {
//        game.addPlayer(playerName, color);
//    }

}
