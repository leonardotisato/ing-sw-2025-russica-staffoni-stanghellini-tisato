package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ErrorSceneController extends ViewController {
    private final double BASE_WIDTH = 960;
    private final double BASE_HEIGHT = 540;

    @FXML
    private StackPane root;
    @FXML
    private Group scalableGroup;


    @FXML
    public AnchorPane anchorPane;

    private GUIRoot gui;

    /**
     * Sets the GUI root reference for this build scene controller.
     * This method establishes the connection between the controller and the main GUI class,
     * enabling communication with the game client and other GUI components.
     *
     * @param gui the GUIRoot instance that manages the overall GUI state and client communication
     */
    public void setGui(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Initializes the error scene controller after the FXML file has been loaded.
     * This method sets up responsive scaling behavior by adding listeners to the root container's
     * width and height properties. When the window is resized, the scaleUI method is automatically
     * called to maintain proper aspect ratio and centering of the UI elements.
     *
     * @param location the location used to resolve relative paths for the root object, or null if unknown
     * @param resources the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        root.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());

    }

    /**
     * Scales the UI elements to maintain proper aspect ratio and centering during window resizing.
     * This method calculates the appropriate scale factor based on the current window dimensions
     * compared to the base dimensions (960x540), applies uniform scaling to the scalable group,
     * and centers the scaled content within the available space. The scaling preserves the original
     * aspect ratio by using the smaller of the horizontal or vertical scale factors.
     */

    private void scaleUI() {
        double scaleX = root.getWidth() / BASE_WIDTH;
        double scaleY = root.getHeight() / BASE_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        scalableGroup.setScaleX(scale);
        scalableGroup.setScaleY(scale);
        double offsetX = (root.getWidth() - BASE_WIDTH * scale) / 2;
        double offsetY = (root.getHeight() - BASE_HEIGHT * scale) / 2;

        scalableGroup.setLayoutX(offsetX);
        scalableGroup.setLayoutY(offsetY);
    }

    /**
     * Placeholder method for navigation to the pre-lobby scene.
     * Currently returns immediately without performing any navigation action.
     * This method appears to be intended for scene transition logic when returning from
     * an error state to the pre-lobby, but the implementation is not completed.
     *
     * @param gui the GUIRoot instance for potential scene management operations
     * @throws IOException if an I/O error occurs during scene transition (though not currently thrown)
     */
    @Override
    public void goToPrelobbyScene(GUIRoot gui) throws IOException {
        return;
    }
}
