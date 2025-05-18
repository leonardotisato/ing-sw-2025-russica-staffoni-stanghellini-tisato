module it.polimi.ingsw.cg04 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.google.gson;
    requires java.rmi;
    requires org.jline;

    opens it.polimi.ingsw.cg04 to javafx.fxml;
    exports it.polimi.ingsw.cg04;
    exports it.polimi.ingsw.cg04.model;
    exports it.polimi.ingsw.cg04.model.enumerations;
    exports it.polimi.ingsw.cg04.model.tiles;
    exports it.polimi.ingsw.cg04.model.adventureCards;
    opens it.polimi.ingsw.cg04.model.adventureCards to com.google.gson;
    opens it.polimi.ingsw.cg04.model.tiles to com.google.gson;
    exports it.polimi.ingsw.cg04.controller;
    exports it.polimi.ingsw.cg04.model.GameStates;
    exports it.polimi.ingsw.cg04.model.PlayerActions;
    exports it.polimi.ingsw.cg04.model.utils;
    exports it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;
    exports it.polimi.ingsw.cg04.model.PlayerActions.BuildActions;
    exports it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions;
    exports it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;
    exports it.polimi.ingsw.cg04.model.exceptions;
    exports it.polimi.ingsw.cg04.network.Server.RMI to java.rmi;
    exports it.polimi.ingsw.cg04.network.Client.RMI to java.rmi;

    exports it.polimi.ingsw.cg04.client.model;

    exports it.polimi.ingsw.cg04.client.view.gui;
    opens it.polimi.ingsw.cg04.client.view.gui to javafx.fxml;
    exports it.polimi.ingsw.cg04.client.view.gui.controllers;
    opens it.polimi.ingsw.cg04.client.view.gui.controllers to javafx.fxml;


    exports it.polimi.ingsw.cg04.network.Server;
    exports it.polimi.ingsw.cg04.network.Client;
}