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
    opens it.polimi.ingsw.cg04.model to javafx.fxml;
}