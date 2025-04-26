package it.polimi.ingsw.cg04.network.Client;

public abstract class ServerHandler implements VirtualServer {
    protected String nickname;
    // private final ClientModel clientModel;
    // private View view;

    protected final String serverIP;
    protected final String serverPort;

    protected ServerHandler(String serverIP, String serverPort, String viewType) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        // clientModel = new ClientModel();

//        if(viewType.equals("GUI"))
//            view = new GUIRoot(clientModel, this);
//        else
//            view = new TUI(clientModel, this);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

}
