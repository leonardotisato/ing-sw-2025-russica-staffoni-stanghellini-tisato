package it.polimi.ingsw.cg04.view.tui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TUITest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void TUITest() {
        System.out.println("Commands list:\n");

        System.out.println("LOBBY COMMANDS:");
        System.out.println("\tSet_Nickname [String] nickname");
        System.out.println("\tCreate_Game [int] game_level, [int] max_players, [String] player_color");
        System.out.println("\tJoin_Game [int] game_id, [String] player_color\n");

        System.out.println("BUILDING COMMANDS:");
        System.out.println("\tChoose_Tile [int] tileID");
        System.out.println("\tChoose_Tile_From_Buffer [int] buffer_index");
        System.out.println("\tShow_FaceUp_Tiles");
        System.out.println("\tCloseFAceUp_Tiles");
        System.out.println("\tShow_Buffer_Tiles");
        System.out.println("\tDraw_Face_Down_Tile");
        System.out.println("\tPlace_Tile");
        System.out.println("\tEndBuilding [int] position");
        System.out.println("\tPick_Pile [int] pile_index");
        System.out.println("\tPlace_In_Buffer");
        System.out.println("\tReturn_Pile");
        System.out.println("\tReturn_Tile");
        System.out.println("\tStart_Timer\n");

        System.out.println("ADVENTURE CARDS COMMANDS:");
        System.out.println();
    }
}