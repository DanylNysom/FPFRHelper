package info.dylansymons.fpfrhelper.firefighter;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * A Firefighter role for a player of the Game.
 *
 * Each Firefighter has a set of Actions that are available to it, an amount of Action Points per
 * turn, and a maximum amount of ActionPoints that can be saved per turn.
 *
 * @author dylan
 */

public class Firefighter {
    private static final int EXPANSION_NONE = -1;
    public static final Firefighter RANDOM = new Firefighter(
            "Random", EXPANSION_NONE, 0, 0, null
    );
    private static final int EXPANSION_BASE = 0;
    private static final int EXPANSION_PREVENTION = 1;
    private static final int EXPANSION_URBAN = 2;
    private static final int EXPANSION_VETERAN_DOG = 3;
    private static final Firefighter CAFS = new Firefighter(
            "CAFS Firefighter", EXPANSION_BASE, 3, 4, "Extinguish", 3, new Action[]{Action.MOVE,
            Action.MOVE_FIRE, Action.CARRY, Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE,
            Action.FIRE_DECK_GUN, Action.CHOP, Action.CREW_CHANGE
    },
            new Action[]{Action.EXTINGUISH}
    );
    private static final Firefighter DRIVER = new Firefighter(
            "Driver/Operator", EXPANSION_BASE, 4, 4, new Action[]{Action.MOVE, Action.MOVE_FIRE, Action.CARRY,
            Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE, Action.FIRE_DECK_GUN_HALF,
            Action.CHOP, Action.CREW_CHANGE
    }
    );
    private static final Firefighter CAPTAIN = new Firefighter(
            "Fire Captain", EXPANSION_BASE, 4, 4, "Command", 2, new Action[]{Action.MOVE, Action.MOVE_FIRE,
            Action.CARRY, Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE,
            Action.FIRE_DECK_GUN, Action.CHOP, Action.COMMAND1, Action.COMMAND2, Action.COMMAND4,
            Action.CREW_CHANGE
    },
            new Action[]{Action.COMMAND1, Action.COMMAND2, Action.COMMAND4}
    );
    private static final Firefighter GENERALIST = new Firefighter(
            "Generalist", EXPANSION_BASE, 5, 4, new Action[]{Action.MOVE, Action.MOVE_FIRE, Action.CARRY,
            Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE, Action.FIRE_DECK_GUN,
            Action.CHOP, Action.CREW_CHANGE
    }
    );
    private static final Firefighter HAZMAT = new Firefighter(
            "Hazmat Technician", EXPANSION_BASE, 4, 4, new Action[]{Action.MOVE, Action.MOVE_FIRE, Action.CARRY,
            Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE, Action.FIRE_DECK_GUN,
            Action.CHOP, Action.DISPOSE, Action.CREW_CHANGE
    }
    );
    private static final Firefighter IMAGING = new Firefighter(
            "Imaging Technician", EXPANSION_BASE, 4, 4, new Action[]{Action.MOVE, Action.MOVE_FIRE, Action.CARRY,
            Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE, Action.FIRE_DECK_GUN,
            Action.CHOP, Action.IDENTIFY, Action.CREW_CHANGE
    }
    );
    private static final Firefighter PARAMEDIC = new Firefighter(
            "Paramedic", EXPANSION_BASE, 4, 4, new Action[]{Action.MOVE, Action.MOVE_FIRE, Action.CARRY,
            Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH_DOUBLE, Action.DRIVE, Action.FIRE_DECK_GUN,
            Action.CHOP, Action.TREAT, Action.CREW_CHANGE
    }
    );
    private static final Firefighter RESCUE = new Firefighter(
            "Rescue Specialist", EXPANSION_BASE, 4, 4, "Movement", 3, new Action[]{Action.MOVE,
            Action.MOVE_FIRE, Action.CARRY, Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE,
            Action.FIRE_DECK_GUN, Action.CHOP_HALF, Action.CREW_CHANGE
    },
            new Action[]{Action.MOVE, Action.MOVE_FIRE, Action.CARRY, Action.OPEN_CLOSE_DOOR}
    );
    static final Firefighter[] FIREFIGHTERS_BASE = new Firefighter[]{
            CAFS, DRIVER, CAPTAIN, GENERALIST, HAZMAT, IMAGING, PARAMEDIC, RESCUE
    };
    private static final Firefighter PREVENTION = new Firefighter(
            "Fire Prevention Specialist", EXPANSION_PREVENTION, 4, 4, new Action[]{Action.MOVE,
            Action.MOVE_FIRE, Action.CARRY, Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE,
            Action.FIRE_DECK_GUN, Action.CHOP, Action.PREVENT_SMOKE, Action.PREVENT_POI,
            Action.CREW_CHANGE
    }
    );
    static final Firefighter[] FIREFIGHTERS_PREVENTION = new Firefighter[]{PREVENTION};
    private static final Firefighter DOG = new Firefighter(
            "Rescue Dog", EXPANSION_VETERAN_DOG, 12, 6, new Action[]{Action.MOVE,
            Action.CARRY_DOUBLE, Action.SQUEEZE, Action.REVEAL, Action.CREW_CHANGE}
    );
    private static final Firefighter VETERAN = new Firefighter(
            "Fire Prevention Specialist", EXPANSION_VETERAN_DOG, 4, 4, new Action[]{Action.MOVE,
            Action.MOVE_FIRE, Action.CARRY, Action.OPEN_CLOSE_DOOR, Action.EXTINGUISH, Action.DRIVE,
            Action.FIRE_DECK_GUN, Action.CHOP, Action.DODGE, Action.CREW_CHANGE
    }
    );
    static final Firefighter[] FIREFIGHTERS_VETERAN_DOG = new Firefighter[]{VETERAN, DOG};
    private static final Firefighter STRUCTURAL = new Firefighter(
            "Structural Engineer", EXPANSION_URBAN, 4, 4, new Action[]{Action.MOVE,
            Action.MOVE_FIRE, Action.CARRY, Action.OPEN_CLOSE_DOOR, Action.DRIVE,
            Action.FIRE_DECK_GUN, Action.CHOP, Action.CLEAR, Action.REPAIR, Action.CREW_CHANGE
    }
    );
    static final Firefighter[] FIREFIGHTERS_URBAN = new Firefighter[]{STRUCTURAL};
    private final String mTitle;
    private final Action[] mActions;
    private final Action[] mBonusActions;
    private final int mExpansion;
    private final int mAp;
    private final int mBonusAp;
    private final int mApSaveLimit;
    private final String mBonusApLabel;

    private Firefighter(String title, int expansion, int ap, int apSaveLimit,
                        String bonusApLabel, int bonusAp, Action[] actions, Action[] bonusActions) {
        mTitle = title;
        mExpansion = expansion;
        mAp = ap;
        mApSaveLimit = apSaveLimit;
        mBonusApLabel = bonusApLabel;
        mBonusAp = bonusAp;
        mActions = actions;
        mBonusActions = bonusActions;
    }

    private Firefighter(String title, int expansion, int ap, int apSaveLimit, Action[] actions) {
        this(title, expansion, ap, apSaveLimit, null, 0, actions, null);
    }

    @Nullable
    public static Firefighter fromTitle(String title) {
        for (Firefighter firefighter : FIREFIGHTERS_BASE) {
            if (firefighter.getTitle().equals(title)) {
                return firefighter;
            }
        }
        for (Firefighter firefighter : FIREFIGHTERS_PREVENTION) {
            if (firefighter.getTitle().equals(title)) {
                return firefighter;
            }
        }
        for (Firefighter firefighter : FIREFIGHTERS_URBAN) {
            if (firefighter.getTitle().equals(title)) {
                return firefighter;
            }
        }
        for (Firefighter firefighter : FIREFIGHTERS_VETERAN_DOG) {
            if (firefighter.getTitle().equals(title)) {
                return firefighter;
            }
        }
        if (RANDOM.getTitle().equals(title)) {
            return Firefighter.RANDOM;
        }
        return null;
    }

    /**
     * Retrieve the title (name) of the Firefighter role - not a Player's name.
     * @return this Firefighter subclass's title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Retrieve a set of Actions available to this Firefighter, including short name,
     * long description, and cost of each.
     * @return a set of Actions that this Firefighter may enact
     */
    public Action[] getActions() {
        return mActions;
    }

    /**
     * Retrieve the number of Action Points this Firefighter receives per turn.
     * @return the number of Action Points this Firefighter receives per turn
     */
    public int getAp() {
        return mAp;
    }

    /**
     * Returns the amount of bonus Action Points available to this Firefighter per turn.
     *
     * @return the amount of bonus Action Points per turn; 0 by default
     */
    public int getBonusAp() {
        return mBonusAp;
    }

    /**
     * Retrieve the maximum number of Action Points this Firefighter can save per turn.
     * @return the number of Action Points this Firefighter can save
     */
    public int getMaxSavedAp() {
        return mApSaveLimit;
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

    public String getBonusApLabel() {
        return mBonusApLabel;
    }

    public boolean hasBonusApFor(Action action) {
        if (mBonusActions != null) {
            for (Action bonus : mBonusActions) {
                if (bonus.equals(action)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * An Action represents something a Player can do on his or her turn.
     *
     * Each action has a Name ({@link Action#getName}) and a cost ({@link Action#getCost()}).
     */
    public static class Action implements Serializable {
        /**
         * An Action representing a Player exchanging their Firefighter for a different one. This
         * can only be done at the start of the Player's Turn, and only when the Firefighter token
         * starts the turn on the same space as the Fire Engine.
         */
        public static final Action CREW_CHANGE = new Action(2, "Crew Change");
        /**
         * An Action representing a movement of one space by the Firefighter.
         */
        static final Action MOVE = new Action(1, "Move");
        /**
         * An Action representing a movement of one space into Fire by the Firefighter.
         */
        static final Action MOVE_FIRE = new Action(2, "Move through fire");
        /**
         * An Action representing a movement of one space by a Firefighter that is carrying a Victim
         * or other object.
         */
        static final Action CARRY = new Action(2, "Carry");
        static final Action CARRY_DOUBLE = new Action(4, "Carry");
        /**
         * An Action representing a Firefighter chopping into an adjacent wall by adding a damage
         * cube to it.
         */
        static final Action CHOP = new Action(2, "Chop");
        static final Action CHOP_HALF = new Action(1, "Chop");
        /**
         * An Action representing the opening or closing of an adjacent Door by a Firefighter.
         */
        static final Action OPEN_CLOSE_DOOR = new Action(1, "Open/Close Door");
        /**
         * An Action representing the extinguishing of a Fire token to a Smoke token, or a Smoke
         * token being removed from the board. This costs 1 Action Point.
         */
        static final Action EXTINGUISH = new Action(1, "Extinguish");
        /**
         * An Action representing the extinguishing of a Fire token to a Smoke token, or a Smoke
         * token being removed from the board. This costs 2 Action Points.
         */
        static final Action EXTINGUISH_DOUBLE = new Action(2, "Extinguish");
        /**
         * An Action representing the movement of a vehicle to one of the nearest Parking Spaces,
         * costing 2 Action Points.
         */
        static final Action DRIVE = new Action(2, "Drive");
        /**
         * An Action representing the firing of the Deck Gun.
         */
        static final Action FIRE_DECK_GUN = new Action(4, "Fire Deck Gun");
        static final Action FIRE_DECK_GUN_HALF = new Action(2, "Fire Deck Gun");

        private static final Action COMMAND1 = new Action(1, "Command");
        private static final Action COMMAND2 = new Action(2, "Command");
        private static final Action COMMAND4 = new Action(4, "Command");

        private static final Action DISPOSE = new Action(2, "Dispose");

        private static final Action IDENTIFY = new Action(1, "Identify");

        private static final Action TREAT = new Action(1, "Treat");

        private static final Action PREVENT_SMOKE = new Action(1, "Prevent (Smoke)");
        private static final Action PREVENT_POI = new Action(1, "Prevent (POI)");

        private static final Action SQUEEZE = new Action(2, "Squeeze");
        private static final Action REVEAL = new Action(0, "Reveal");

        private static final Action DODGE = new Action(1, "Dodge");

        private static final Action CLEAR = new Action(1, "Clear");
        private static final Action REPAIR = new Action(2, "Repair");

        private final String mName;
        private int mCost;

        /**
         * Creates an Action from scratch, using the given parameters
         *
         * @param cost             an integer number representing how many Action Points this Action costs
         */
        public Action(int cost, String name) {
            setCost(cost);
            mName = name;
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
        public String getName() {
            return mName;
        }

    }
}
