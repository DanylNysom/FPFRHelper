package info.dylansymons.fpfrhelper.firefighter;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by dylan on 1/26/17.
 */

public abstract class Firefighter implements Serializable {

    public abstract String getTitle();
    public abstract HashSet<Action> getActions();

    public int getAp() {
        return 4;
    }

    public int getMaxSavedAp() {
        return 4;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) ||
                (other instanceof Firefighter) &&
                        ((Firefighter)other).getTitle().equals(getTitle());
    }

    HashSet<Action> getDefaultMoveActions() {
        HashSet<Action> actions = new HashSet<>(4);
        actions.add(Action.MOVE);
        actions.add(Action.MOVE_FIRE);
        actions.add(Action.CARRY);
        actions.add(Action.OPEN_CLOSE_DOOR);
        return actions;
    }

    HashSet<Action> getDefaultVehicleActions() {
        HashSet<Action> actions = new HashSet<>(2);
        actions.add(Action.DRIVE);
        actions.add(Action.FIRE_DECK_GUN);
        return actions;
    }

    public static class Action {
        public static final Action MOVE = new Action(1, "Move", "Move to an adjacent space without Fire");
        public static final Action MOVE_FIRE = new Action(2, "Move through fire", "Move to an adjacent space with Fire");
        public static final Action CARRY = new Action(2, "Carry", "Carry a Victim or Hazmat to an adjacent space without Fire");
        public static final Action CHOP = new Action(2, "Chop", "Place a Damage marker on a Wall segment in your space");
        public static final Action OPEN_CLOSE_DOOR = new Action(1, "Open/Close Door", "Flip a Door marker in your space");
        public static final Action EXTINGUISH = new Action(1, "Extinguish", "Flip a Fire marker to Smoke, or remove a Smoke marker from the Board");
        public static final Action EXTINGUISH_DOUBLE = new Action(2, "Extinguish", "Flip a Fire marker to Smoke, or remove a Smoke marker from the Board");
        public static final Action DRIVE = new Action(2, "Drive", "Drive a Vehicle");
        public static final Action FIRE_DECK_GUN= new Action(4, "Fire the Deck Gun", "Use the Engine's powerful hose to Extinguish Fires quickly");
        public static final Action CREW_CHANGE = new Action(2, "Crew Change", "Change Specialists while in the heat of the action");

        public int getCost() {
            return mCost;
        }

        public void setCost(int cost) {
            this.mCost = cost;
        }

        public String getShortDescription() {
            return mShortDescription;
        }

        public String getLongDescription() {
            return mLongDescription;
        }

        private int mCost;
        private final String mShortDescription;
        private final String mLongDescription;

        public Action(int cost, String shortDescription, String longDescription) {
            mCost = cost;
            mShortDescription = shortDescription;
            mLongDescription = longDescription;
        }

        public Action(Action other) {
            mCost = other.getCost();
            mShortDescription = other.getShortDescription();
            mLongDescription = other.getLongDescription();
        }
    }

    public class SpecialAction {
        private final String mShortDescription;
        private final String mDescription;
        private final int mCost;

        public SpecialAction(String shortDescription, String description, int cost) {
            mShortDescription = shortDescription;
            mDescription = description;
            mCost = cost;
        }

        public String getShortDescription() {return mShortDescription;}
        public String getDescription() {return mDescription;}
        public int getCost() {return mCost;}
    }
}
