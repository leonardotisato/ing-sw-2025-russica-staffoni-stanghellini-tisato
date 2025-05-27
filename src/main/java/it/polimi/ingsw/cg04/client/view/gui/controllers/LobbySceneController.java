package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LobbySceneController extends ViewController {

    private static final double DOT_RADIUS = 30;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label levelLabel;
    @FXML
    private Label maxPlayersLabel;
    @FXML
    private ScrollPane logsScrollPane;
    @FXML
    private GridPane infoGrid;
    @FXML
    private ImageView backgroundImage;

    private VBox logsContainer;
    private GUIRoot gui;

    /**
     * Initializes the controller after the root element has been completely processed.
     * Configures the layout of the logs container and info grid components and sets initial
     * constraints and padding for the user interface.
     *
     * @param url the location used to resolve relative paths for the root object, or null if not known
     * @param rb  the resource bundle used to localize the root object, or null if not provided
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        backgroundImage.setImage(
                new Image(Objects.requireNonNull(getClass().getResource("/images/background.png")).toExternalForm())
        );
        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());

        logsContainer = new VBox(6);
        logsContainer.setPadding(new Insets(8));
        logsScrollPane.setContent(logsContainer);
        logsScrollPane.setFitToWidth(true);

        infoGrid.getRowConstraints().clear();

        infoGrid.setHgap(12);
        infoGrid.setVgap(24);
        infoGrid.setPadding(new Insets(12));

        ColumnConstraints c0 = new ColumnConstraints(1.8 * DOT_RADIUS, 1.8 * DOT_RADIUS, 1.8 * DOT_RADIUS);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.ALWAYS);
        infoGrid.getColumnConstraints().setAll(c0, c1);
    }

    // todo prolly dont need this, maybe if we want to add a DISCONNECT button (?)
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Refreshes the lobby view with updated game information, including the game level,
     * maximum number of players, the current list of players, and placeholders for open spots.
     **/
    @Override
    public void update(Game game) {
        Platform.runLater(() -> {
            // GameLevel and MaxPlayers indicators
            levelLabel.setText(String.valueOf(game.getLevel()));
            maxPlayersLabel.setText(String.valueOf(game.getMaxPlayers()));

            infoGrid.getChildren().clear();

            // build specific player
            int row = 0;
            for (Player p : game.getPlayers()) {
                Circle dot = new Circle(DOT_RADIUS, setColor(p.getColor()));
                dot.setStroke(Color.BLACK);
                infoGrid.add(dot, 0, row);

                Label name = new Label(p.getName());
                name.setMaxWidth(Double.MAX_VALUE);
                name.setWrapText(true);
                GridPane.setHgrow(name, Priority.ALWAYS);
                infoGrid.add(name, 1, row);

                row++;
            }

            // free spaces placeholders, as many as MaxPlayers for the specific game
            int free = game.getMaxPlayers() - game.getPlayers().size();
            for (int i = 0; i < free; i++) {
                Circle empty = new Circle(DOT_RADIUS, Color.TRANSPARENT);
                empty.setStroke(Color.GREY);
                infoGrid.add(empty, 0, row++);
            }
        });
    }

    /**
     * utility to set the color
     *
     * @param pc the PlayerColor value to be converted
     * @return the JavaFX Color that corresponds to the given PlayerColor
     */
    private Color setColor(PlayerColor pc) {
        return switch (pc) {
            case BLUE -> Color.DEEPSKYBLUE;
            case YELLOW -> Color.GOLD;
            case GREEN -> Color.LIMEGREEN;
            case RED -> Color.CRIMSON;
        };
    }

    @Override
    public void goToLobbyScene(GUIRoot gui){
        return;
    }
}
