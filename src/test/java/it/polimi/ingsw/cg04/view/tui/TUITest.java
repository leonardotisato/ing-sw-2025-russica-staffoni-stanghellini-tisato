package it.polimi.ingsw.cg04.view.tui;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.client.view.tui.TUI;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TUITest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void TUITest() {
    }

    @Test
    void testRenderLobbyState() {
        // Arrange
        Game mockGame = mock(Game.class);
        Player mockPlayerAlice = mock(Player.class);
        Player mockPlayerBob = mock(Player.class);
        ClientModel mockClientModel = mock(ClientModel.class);
        
        when(mockPlayerAlice.getName()).thenReturn("Alice");
        when(mockPlayerAlice.getColor()).thenReturn(PlayerColor.RED);
        when(mockPlayerBob.getName()).thenReturn("Bob");
        when(mockPlayerBob.getColor()).thenReturn(PlayerColor.BLUE);
        
        when(mockGame.getPlayers()).thenReturn(List.of(mockPlayerAlice, mockPlayerBob));
        when(mockGame.getMaxPlayers()).thenReturn(4);

        TUI tui = new TUI(null, mockClientModel);

        // Act
        tui.renderLobbyState(mockGame);
        System.out.println(tui.getRendered());

    }

}