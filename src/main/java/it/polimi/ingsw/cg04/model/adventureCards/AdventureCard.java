package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;
import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.toLines;

public abstract class AdventureCard implements Serializable {

    @Expose
    private String type;
    @Expose
    private int cardLevel;
    @Expose
    private int daysLost;
    // @Expose private boolean solved = false;

    protected static final int WIDTH = 29;
    protected static final int HEIGHT = 11;

    public AdventureCard() {
    }

    //public abstract void solveEffect(Game game);

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public void setDaysLost(int daysLost) {
        this.daysLost = daysLost;
    }

    // public boolean getSolved() { return solved; }

    // public void setSolved(boolean solved) { this.solved = solved; }

    public Integer getFirePower() {
        return null;
    }

    // public int getLostGoods() { return 0; }

    public Integer getLostMembers() {
        return null;
    }

    public List<Boolean> getIsOccupied() {
        return null;
    }


    public Integer getEarnedCredits() {
        return null;
    }

    public int getMembersNeeded() {
        return 0;
    }

    public void setMembersNeeded(int i) {
        return;
    }

    public abstract AdventureCardState createState(Game game);

    public int getObtainedResourcesByType(BoxType type) {
        return 0;
    }

    public List<Map<BoxType, Integer>> getPlanetReward() {
        return null;
    }

    public Map<BoxType, Integer> getObtainedResources() {
        return null;
    }

    public Direction getDirection(int i) {
        return null;
    }

    public List<Attack> getAttacks() {
        return null;
    }

    public Attack getAttack(int i) {
        return null;
    }

    public int getLostGoods() { return 0; }

    public Map<BoxType, Integer> getBoxes() { return null; }

    public List<String> getParameterCheck() {
        return null;
    }

    public List<String> getPenaltyType() {
        return null;
    }

    public AdventureCardState createGameState(Game game) {
        return null;
    }

    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("", WIDTH - 4)).append(" │").append("\n");
    }

    public final String draw() {
        StringBuilder sb = new StringBuilder();
        sb.append(TuiDrawer.drawTopBoundary(WIDTH)).append('\n');
        sb.append(TuiDrawer.drawCenteredRow(getType(), WIDTH)).append('\n');
        sb.append(TuiDrawer.drawInnerSeparator(WIDTH)).append('\n');
        StringBuilder contentBuilder = new StringBuilder();
        drawContent(contentBuilder);  // disegna righe nel builder temporaneo
        List<String> contentLines = toLines(contentBuilder.toString());

        int bodyHeight = HEIGHT - 4;  // top + title + separator + bottom
        int contentHeight = contentLines.size();
        int padTop = (bodyHeight - contentHeight) / 2;
        int padBottom = bodyHeight - contentHeight - padTop;

        // padding sopra
        for (int i = 0; i < padTop; i++) {
            sb.append(TuiDrawer.drawEmptyRow(WIDTH)).append('\n');
        }

        // contenuto effettivo
        for (String line : contentLines) {
            sb.append(line).append('\n');  // linea già formattata
        }

        // padding sotto
        for (int i = 0; i < padBottom; i++) {
            sb.append(TuiDrawer.drawEmptyRow(WIDTH)).append('\n');
        }

        // chiusura
        sb.append(TuiDrawer.drawBottomBoundary(WIDTH));
        return sb.toString();
    }

}
