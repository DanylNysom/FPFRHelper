package info.dylansymons.fpfrhelper.player;

import java.io.Serializable;

import info.dylansymons.fpfrhelper.firefighter.Firefighter;

/**
 * @author dylan
 */
public class Player implements Serializable {
    private String name;
    private Firefighter firefighter;
    private int currentAp;
    private int savedAp;
    private int colour;

    public Player(String name, Firefighter firefighter, int colour) {
        setName(name);
        setFirefighter(firefighter);
        setColour(colour);
        currentAp = 0;
        savedAp = 0;
    }

    public String getName() {
        if(name != null) {
            return name;
        } else {
            return "";
        }
    }

    private void setName(String name) {
        this.name = name;
    }

    public Firefighter getFirefighter() {
        return firefighter;
    }

    public String getFirefighterTitle() {
        return firefighter.getTitle();
    }

    private void setColour(int colour) {
        this.colour = colour;
    }

    public int getColour() {
        return colour;
    }

    void startTurn() {
        currentAp = firefighter.getAp() + savedAp;
        savedAp = 0;
    }

    void endTurn() {
        savedAp = Math.min(firefighter.getMaxSavedAp(), currentAp);
    }

    private void setFirefighter(Firefighter firefighter) {
        this.firefighter = firefighter;
    }

    public int getAp() {
        return currentAp;
    }

    @Override
    public String toString() {
        if (name != null && !name.isEmpty()) {
            return getFirefighter() + " (" + getName() + ")";
        } else if (firefighter != null) {
            return getFirefighter().toString();
        } else {
            return "";
        }
    }
}
