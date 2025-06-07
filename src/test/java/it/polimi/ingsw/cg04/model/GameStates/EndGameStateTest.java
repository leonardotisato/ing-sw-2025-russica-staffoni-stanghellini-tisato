package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EndGameStateTest {
    @Test
    public void testGetLeaderboard_WithActiveAndRetiredPlayers() {
        Game mockGame = mock(Game.class);

        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);

        when(player1.getNumCredits()).thenReturn(150.0);
        when(player2.getNumCredits()).thenReturn(200.0);
        when(player3.getNumCredits()).thenReturn(100.0);

        List<Player> activePlayers = new ArrayList<>();
        activePlayers.add(player1);
        activePlayers.add(player3);

        List<Player> retiredPlayers = new ArrayList<>();
        retiredPlayers.add(player2);

        when(mockGame.getPlayers()).thenReturn(activePlayers);
        when(mockGame.getRetiredPlayers()).thenReturn(retiredPlayers);

        EndGameState endGameState = new EndGameState(mockGame);
        List<Player> leaderboard = endGameState.getLeaderboard();

        Assertions.assertEquals(3, leaderboard.size());
        Assertions.assertEquals(player2, leaderboard.get(0));
        Assertions.assertEquals(player1, leaderboard.get(1));
        Assertions.assertEquals(player3, leaderboard.get(2));
    }

    @Test
    public void testGetLeaderboard_OnlyActivePlayers() {
        Game mockGame = mock(Game.class);

        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);

        when(player1.getNumCredits()).thenReturn(300.0);
        when(player2.getNumCredits()).thenReturn(150.0);

        List<Player> activePlayers = new ArrayList<>();
        activePlayers.add(player1);
        activePlayers.add(player2);

        when(mockGame.getPlayers()).thenReturn(activePlayers);
        when(mockGame.getRetiredPlayers()).thenReturn(new ArrayList<>());

        EndGameState endGameState = new EndGameState(mockGame);
        List<Player> leaderboard = endGameState.getLeaderboard();

        Assertions.assertEquals(2, leaderboard.size());
        Assertions.assertEquals(player1, leaderboard.get(0));
        Assertions.assertEquals(player2, leaderboard.get(1));
    }

    @Test
    public void testGetLeaderboard_OnlyRetiredPlayers() {
        Game mockGame = mock(Game.class);

        Player player1 = mock(Player.class);

        when(player1.getNumCredits()).thenReturn(500.0);

        List<Player> retiredPlayers = new ArrayList<>();
        retiredPlayers.add(player1);

        when(mockGame.getPlayers()).thenReturn(new ArrayList<>());
        when(mockGame.getRetiredPlayers()).thenReturn(retiredPlayers);

        EndGameState endGameState = new EndGameState(mockGame);
        List<Player> leaderboard = endGameState.getLeaderboard();

        Assertions.assertEquals(1, leaderboard.size());
        Assertions.assertEquals(player1, leaderboard.get(0));
    }

    @Test
    public void testGetLeaderboard_NoPlayers() {
        Game mockGame = mock(Game.class);

        when(mockGame.getPlayers()).thenReturn(new ArrayList<>());
        when(mockGame.getRetiredPlayers()).thenReturn(new ArrayList<>());

        EndGameState endGameState = new EndGameState(mockGame);
        List<Player> leaderboard = endGameState.getLeaderboard();

        Assertions.assertEquals(0, leaderboard.size());
    }
}