package it.polimi.ingsw.cg04;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.network.Client.RMI.ServerHandlerRMI;
import it.polimi.ingsw.cg04.network.Client.Socket.SocketServerHandler;

import java.io.IOException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        try {
            String type = args[0];

            if (type.equals("client")) {
                String connection = args[1];
                String ip = args[2];
                String port = args[3];
                String view = args[4];

                if (connection.equals("rmi")) {
                    new ServerHandlerRMI(ip, port, view);
                } else {
                    new SocketServerHandler(ip, port, view);
                }

            } else {
                System.out.println("Starting a new game");
                new GamesController();
            }


        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Unexpected parameters");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
