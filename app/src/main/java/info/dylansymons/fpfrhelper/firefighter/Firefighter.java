package info.dylansymons.fpfrhelper.firefighter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Firefighter role for a player of the Game.
 *
 * Each Firefighter has a set of Actions that are available to it, an amount of Action Points per
 * turn, and a maximum amount of ActionPoints that can be saved per turn.
 *
 * @author dylan
 */

public abstract class Firefighter implements Serializable {

    /**
     * The default number of Action Points that a Firefighter receives per turn.
     */
    private static final int DEFAULT_AP = 4;

    /**
     * The default number of Action Points that a Firefighter can save per turn.
     */
    private static final int DEFAULT_SAVED_AP = 4;

    /**
     * Retrieve the title (name) of the Firefighter role - not a Player's name.
     * @return this Firefighter subclass's title
     */
    public abstract String getTitle();

    /**
     * Retrieve a set of Actions available to this Firefighter, including short name,
     * long description, and cost of each.
     * @return a set of Actions that this Firefighter may enact
     */
    public abstract ArrayList<Action> getActions();

    /**
     * Retrieve the number of Action Points this Firefighter receives per turn.
     * @return the number of Action Points this Firefighter receives per turn
     */
    public int getAp() {
        return DEFAULT_AP;
    }

    /**
     * Returns the amount of bonus Action Points available to this Firefighter per turn.
     *
     * @see Firefighter#hasBonusApFor(Action) ()
     * @return the amount of bonus Action Points per turn; 0 by default
     */
    public int getBonusAp() {
        return 0;
    }

    /**
     * Retrieve the maximum number of Action Points this Firefighter can save per turn.
     * @return the number of Action Points this Firefighter can save
     */
    public int getMaxSavedAp() {
        return DEFAULT_SAVED_AP;
    }

    /**
     * {@inheritDoc}
     * @return the Firefighter's title
     */
    @Override
    public String toString() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     * @param other {@inheritDoc}
     * @return true if and only if this and the passed Object are both instances of
     * {@link Firefighter} and have the same Title
     */
    @Override
    public boolean equals(Object other) {
        return super.equals(other) ||
                ((other instanceof Firefighter) &&
                        ((Firefighter) other).getTitle().equals(getTitle())) ||
                (other instanceof String) &&
                        getTitle().equals(other);
    }

    /**
     * Returns a default set of Actions relating to movement.
     * @return a set containing {@link Action#MOVE}, {@link Action#MOVE_FIRE}, {@link Action#CARRY},
     * and {@link Action#OPEN_CLOSE_DOOR}
     */
    ArrayList<Action> getDefaultMoveActions() {
        ArrayList<Action> actions = new ArrayList<>(4);
        actions.add(Action.MOVE);
        actions.add(Action.MOVE_FIRE);
        actions.add(Action.CARRY);
        actions.add(Action.OPEN_CLOSE_DOOR);
        return actions;
    }

    /**
     * Returns a default set of Actions relating to Vehicles.
     * @return a set containing {@link Action#DRIVE} and {@link Action#FIRE_DECK_GUN}
     */
    ArrayList<Action> getDefaultVehicleActions() {
        ArrayList<Action> actions = new ArrayList<>(2);
        actions.add(Action.DRIVE);
        actions.add(Action.FIRE_DECK_GUN);
        return actions;
    }

    /**
     * Determine if this Firefighter receives bonus Action Points for an Action
     *
     * @param action the Action to check if this Firefighter has bonus AP for
     * @return true if this Firefighter receives bonus AP for the passed Action
     */
    public boolean hasBonusApFor(Action action) {
        return false;
    }

    public String getBonusApLabel() {
        return null;
    }

    /**
     * An Action represents something a Player can do on his or her turn.
     *
     * Each action has a Name ({@link Action#getShortDescription}), a description
     * ({@link Action#getLongDescription()}), and a cost ({@link Action#getCost()}).
     */
    public static class Action implements Serializable {
        /**
         * An Action representing a Player exchanging their Firefighter for a different one. This
         * can only be done at the start of the Player's Turn, and only when the Firefighter token
         * starts the turn on the same space as the Fire Engine.
         */
        public static final Action CREW_CHANGE = new Action(2, "Crew Change",
                "Change Specialists while in the heat of the action");
        /**
         * An Action representing a movement of one space by the Firefighter.
         */
        static final Action MOVE = new Action(1, "Move",
                "Move to an adjacent space without Fire");
        /**
         * An Action representing a movement of one space into Fire by the Firefighter.
         */
        static final Action MOVE_FIRE = new Action(2, "Move through fire",
                "Move to an adjacent space with Fire");
        /**
         * An Action representing a movement of one space by a Firefighter that is carrying a Victim
         * or other object.
         */
        static final Action CARRY = new Action(2, "Carry",
                "Carry a Victim or Hazmat to an adjacent space without Fire");
        /**
         * An Action representing a Firefighter chopping into an adjacent wall by adding a damage
         * cube to it.
         */
        static final Action CHOP = new Action(2, "Chop",
                "Place a Damage marker on a Wall segment in your space");
        /**
         * An Action representing the opening or closing of an adjacent Door by a Firefighter.
         */
        static final Action OPEN_CLOSE_DOOR = new Action(1, "Open/Close Door",
                "Flip a Door marker in your space");
        /**
         * An Action representing the extinguishing of a Fire token to a Smoke token, or a Smoke
         * token being removed from the board. This costs 1 Action Point.
         */
        static final Action EXTINGUISH = new Action(1, "Extinguish",
                "Flip a Fire marker to Smoke, or remove a Smoke marker from the Board");
        /**
         * An Action representing the extinguishing of a Fire token to a Smoke token, or a Smoke
         * token being removed from the board. This costs 2 Action Points.
         */
        static final Action EXTINGUISH_DOUBLE = new Action(2, "Extinguish",
                "Flip a Fire marker to Smoke, or remove a Smoke marker from the Board");
        /**
         * An Action representing the movement of a vehicle to one of the nearest Parking Spaces,
         * costing 2 Action Points.
         */
        static final Action DRIVE = new Action(2, "Drive",
                "Drive a Vehicle");
        /**
         * An Action representing the firing of the Deck Gun.
         */
        static final Action FIRE_DECK_GUN = new Action(4, "Fire Deck Gun",
                "Use the Engine's powerful hose to Extinguish Fires quickly");
        private final String mShortDescription;
        private final String mLongDescription;
        private int mCost;

        /**
         * Creates an Action from scratch, using the given parameters
         *
         * @param cost             an integer number representing how many Action Points this Action costs
         * @param shortDescription a brief (one- to five-word) name representing this Action. Not
         *                         necessarily unique
         * @param longDescription  a longer description giving more information about the Action. Not
         *                         necessarily unique
         */
        public Action(int cost, String shortDescription, String longDescription) {
            setCost(cost);
            mShortDescription = shortDescription;
            mLongDescription = longDescription;
        }

        /**
         * Creates a new Action where the fields are all copies of another Action object.
         * <p>
         * This may be useful when creating an Action with a different cost from the default.
         *
         * @param other an Action to create a copy of
         */
        public Action(Action other) {
            mCost = other.getCost();
            mShortDescription = other.getShortDescription();
            mLongDescription = other.getLongDescription();
        }

        /**
         * Returns the number of Action Points that it costs to perform this Action.
         * @return an integer representing how many Action Points this Action costs
         */
        public int getCost() {
            return mCost;
        }

        /**
         * Sets the number of Action Points that it costs to perform this Action.
         * @param cost an integer number representing how many Action Points this Action costs.
         */
        void setCost(int cost) {
            this.mCost = cost;
        }

        /**
         * Returns a brief description (name) for this Action.
         * @return a short name, unique for the Action being performed. Different Action instances
         * may have the same short and long description, but different costs
         */
        public String getShortDescription() {
            return mShortDescription;
        }

        /**
         * Returns a longer description for this Action.
         * @return a description, unique for the Action being performed. Different Action instances
         * may have the same short and long description, but different costs
         */
        String getLongDescription() {
            return mLongDescription;
        }
    }
}
