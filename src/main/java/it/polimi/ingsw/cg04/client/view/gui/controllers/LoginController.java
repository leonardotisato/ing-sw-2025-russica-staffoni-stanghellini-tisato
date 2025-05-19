package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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

    private GUIRoot gui;


    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

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


    }

    @FXML
    private void handleLogin() throws IOException {
        String nick = nicknameField.getText();
        if (!nick.isEmpty()) {
            gui.setNickname(nick);

            // used for testing, will be removed
            // gui.goToPrelobbyScene();
            // gui.goToEndScene();
        }
    }
}


