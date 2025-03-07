module it.polimi.ingsw.cg04 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.ingsw.cg04 to javafx.fxml;
    exports it.polimi.ingsw.cg04;
}