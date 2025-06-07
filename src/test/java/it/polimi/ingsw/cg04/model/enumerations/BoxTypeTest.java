package it.polimi.ingsw.cg04.model.enumerations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the BoxType enum.
 * Verifies the correct functionality of the getValue method.
 */
public class BoxTypeTest {

    @Test
    public void testGetValueForRedBox() {
        BoxType boxType = BoxType.RED;

        int value = boxType.getValue();

        assertEquals(4, value, "The value for RED box should be 4");
    }

    @Test
    public void testGetValueForYellowBox() {
        BoxType boxType = BoxType.YELLOW;
        
        int value = boxType.getValue();
        
        assertEquals(3, value, "The value for YELLOW box should be 3");
    }

    @Test
    public void testGetValueForGreenBox() {
        BoxType boxType = BoxType.GREEN;
        
        int value = boxType.getValue();
        
        assertEquals(2, value, "The value for GREEN box should be 2");
    }

    @Test
    public void testGetValueForBlueBox() {
        BoxType boxType = BoxType.BLUE;
        
        int value = boxType.getValue();
        
        assertEquals(1, value, "The value for BLUE box should be 1");
    }
}