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
    exports it.polimi.ingsw.cg04.model.adventureCards;
    opens it.polimi.ingsw.cg04.model to javafx.fxml;
    opens it.polimi.ingsw.cg04.model.adventureCards to com.google.gson;
    opens it.polimi.ingsw.cg04.model.tiles to com.google.gson;
    exports it.polimi.ingsw.cg04.controller;
    exports it.polimi.ingsw.cg04.model.GameStates;
    exports it.polimi.ingsw.cg04.model.PlayerActions;
    exports it.polimi.ingsw.cg04.model.utils;
    opens it.polimi.ingsw.cg04.controller to javafx.fxml;
    exports it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;
    exports it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;
    exports it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;
    exports it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;
}