module it.polimi.ingsw.cg04 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.google.gson;

    opens it.polimi.ingsw.cg04 to javafx.fxml;
    exports it.polimi.ingsw.cg04;
    exports it.polimi.ingsw.cg04.model;
    exports it.polimi.ingsw.cg04.model.enumerations;
    exports it.polimi.ingsw.cg04.model.tiles;
    exports it.polimi.ingsw.cg04.model.ships;
    exports it.polimi.ingsw.cg04.model.adventureCards;
    opens it.polimi.ingsw.cg04.model to javafx.fxml;
    opens it.polimi.ingsw.cg04.model.adventureCards to com.google.gson;
}