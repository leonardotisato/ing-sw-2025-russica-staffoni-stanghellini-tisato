package it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

public class SetNicknameAction extends InitAction {

    public SetNicknameAction(String nickname) {
        super(nickname);
    }

    @Override
    public void execute(GamesController controller) {
        controller.addNicktoGame(nickname, null);
    }

    @Override
    public boolean checkAction(GamesController controller) throws InvalidActionException {
        if (nickname == null || nickname.isEmpty() || controller.isNickNameTaken(nickname)) {
            throw new InvalidActionException("Name already taken or empty or null");
        }

        return true;
    }

    @Override
    public void execute(Player player) {
        throw new RuntimeException("Player does not exist");
    }

    @Override
    public boolean checkAction(Player player) {
        throw new RuntimeException("Player does not exist");
    }

    @Override
    public String getPlayerNickname() {
        return nickname;
    }
}
