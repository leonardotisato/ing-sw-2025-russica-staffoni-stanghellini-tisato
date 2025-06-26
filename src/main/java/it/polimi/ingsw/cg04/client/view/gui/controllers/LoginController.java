package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController extends ViewController {

    @FXML
    private StackPane rootPane;
    @FXML
    private Label titleText;
    @FXML
    private TextField nicknameField;
    @FXML
    private Button okButton;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private TextField logs;

    private GUIRoot gui;


    /**
     * Sets the GUI root reference for this login controller.
     * This method establishes the connection between the controller and the main GUI application,
     * enabling the controller to communicate with other GUI components and initiate scene transitions.
     *
     * @param gui the GUIRoot instance that manages the overall GUI application
     */
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Initializes the login controller after the FXML file has been loaded.
     * This method sets up responsive font sizing for UI elements by binding their font sizes
     * to the root pane's width, configures the background image to fill the container,
     * and enables login functionality through the Enter key in addition to the button click.
     * Font sizes scale dynamically based on window width for better user experience.
     *
     * @param location the location used to resolve relative paths for the root object, or null if unknown
     * @param resources the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleText.styleProperty().bind(
                Bindings.concat("-fx-font-size: ", rootPane.widthProperty().divide(15).asString(), "px;")
        );

        nicknameField.styleProperty().bind(
                Bindings.concat("-fx-font-size: ", rootPane.widthProperty().divide(50).asString(), "px;")
        );

        okButton.styleProperty().bind(
                Bindings.concat("-fx-font-size: ", rootPane.widthProperty().divide(50).asString(), "px;")
        );
        backgroundImage.setImage(
                new Image(Objects.requireNonNull(getClass().getResource("/images/background.png")).toExternalForm())
        );
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        // trigger login with enter
        nicknameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    handleLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Displays log messages with automatic timeout functionality.
     * This method shows the most recent log message in the logs text field and automatically
     * clears it after 5 seconds using a Timeline animation. Only the last message from the
     * provided log list is displayed to avoid UI clutter.
     *
     * @param log the list of log messages, where the last message will be displayed
     */
    @Override
    public void showLogs(List<String> log) {
        logs.setText(log.getLast());
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                event -> logs.setText("")
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Handles the user login process when the login button is clicked or Enter is pressed.
     * This method validates that the nickname field is not empty, sets the nickname in the
     * GUI root for use throughout the application, and prepares for potential scene transitions.
     * The method includes commented code for testing various scene transitions.
     *
     * @throws IOException if an I/O error occurs during the login process or scene transition
     */
    @FXML
    private void handleLogin() throws IOException {
        String nick = nicknameField.getText();
        if (!nick.isEmpty()) {
            gui.setNickname(nick);

            // used for testing, will be removed
            // gui.goToPrelobbyScene();
            // gui.goToEndScene();
        }
        // gui.goToFaceUpScene();
    }
}


