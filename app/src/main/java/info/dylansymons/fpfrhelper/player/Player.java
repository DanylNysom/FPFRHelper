package info.dylansymons.fpfrhelper.player;

import java.io.Serializable;
import java.util.Stack;

import info.dylansymons.fpfrhelper.firefighter.Firefighter;

/**
 * A representation of a Player of the Game.
 *
 * A Player has:
 * <ul>
 *     <li>a name of the person playing the game</li>
 *     <li>a Firefighter role being played</li>
 *     <li>the current state, e.g. number of action points, of the player/firefighter</li>
 * </ul>
 *
 * @author dylan
 */
public class Player implements Serializable {
    private final Stack<PerformedAction> actionHistory;
    private String mName;
    private Firefighter mFirefighter;
    private int mCurrentAp;
    private int mSavedAp;
    private int mCurrentBonusAp;
    private int mColour;
    private Firefighter.Action[] mActions;
    private boolean mHasActed;

    /**
     * Creates a new Player given a name, Firefighter, and colour
     *
     * @param name        the name of the person playing the game
     * @param firefighter the Firefighter that this person is playing as
     * @param colour      the colour of this player's badge
     */
    public Player(String name, Firefighter firefighter, int colour) {
        mName = name;
        mFirefighter = firefighter;
        mColour = colour;
        mCurrentAp = 0;
        mSavedAp = 0;
        mCurrentBonusAp = 0;
        actionHistory = new Stack<>();
    }

    /**
     * Returns the name of the person playing as this Player
     *
     * @return the name of the person (not the Firefighter)
     */
    public String getName() {
        if (mName != null) {
            return mName;
        } else {
            return "";
        }
    }

    public void setName(String name) {
        mName = name;
    }

    /**
     * Returns the Firefighter role this Player is playing as
     *
     * @return the Firefighter role
     */
    public Firefighter getFirefighter() {
        return mFirefighter;
    }

    public void setFirefighter(Firefighter firefighter) {
        mFirefighter = firefighter;
    }

    /**
     * Returns the name of the Firefighter role this Player is playing as
     * @return the name of the Firefighter role (not the person playing)
     */
    public String getFirefighterTitle() {
        return mFirefighter.getTitle();
    }

    public int getColour() {
        return mColour;
    }

    public void setColour(int colour) {
        mColour = colour;
    }

    void startTurn() {
        mCurrentAp = mFirefighter.getAp() + mSavedAp;
        mSavedAp = 0;
        mCurrentBonusAp = mFirefighter.getBonusAp();
        mHasActed = false;
        actionHistory.empty();
    }

    void endTurn() {
        mSavedAp = Math.min(mFirefighter.getMaxSavedAp(), mCurrentAp);
    }

    public int getAp() {
        return mCurrentAp;
    }

    public int getBonusAp() {
        return mCurrentBonusAp;
    }

    public Firefighter.Action[] getActions() {
        if (mActions == null) {
            mActions = mFirefighter.getActions().toArray(new Firefighter.Action[0]);
        }
        return mActions;
    }

    /**
     * Performs an Action.
     * <p>
     * If the Action is successfully performed, its cost will be deducted from the Player's current
     * Action Points. If the Action is one for which the Firefighter receives bonus Action Points,
     * the cost will be deducted from the bonus points first, and any leftover cost will be deducted
     * from the current general Action Points.
     * <p>
     * If the Player does not have enough Action Points, no points will be deducted and false will
     * be returned.
     *
     * @param action the Action to perform. No checks are made to ensure that the Firefighter is
     *               allowed to perform the Action
     */
    public void perform(Firefighter.Action action) {
        int cost = action.getCost();
        int bonusAp = mCurrentBonusAp;
        if (mFirefighter.hasBonusApFor(action)) {
            if (cost > mCurrentBonusAp) {
                cost -= mCurrentBonusAp;
                mCurrentBonusAp = 0;
            } else {
                mCurrentBonusAp -= cost;
                cost = 0;
            }
        }
        if (cost <= mCurrentAp) {
            mCurrentAp -= cost;
            mHasActed = true;
            PerformedAction performed = new PerformedAction(action, cost, bonusAp - mCurrentBonusAp);
            actionHistory.push(performed);
        } else {
            mCurrentBonusAp = bonusAp;
        }
    }

    /**
     * Determines if the Player has enough Action Points to perform a given Action.
     * <p>
     * This takes into account bonus Action Points if the Action given is one for which the
     * Firefighter has bonus points.
     *
     * @param action an Action that this Player's Firefighter is capable of enacting, assuming he or
     *               she has the necessary Action Points
     * @return true if the Player currently has enough Action Points to perform the Action
     */
    public boolean hasPointsFor(Firefighter.Action action) {
        int cost = action.getCost();
        int availableAp = (mFirefighter.hasBonusApFor(action)) ?
                mCurrentAp + mCurrentBonusAp :
                mCurrentAp;
        return cost <= availableAp;
    }

    @Override
    public String toString() {
        if (mName != null && !mName.isEmpty()) {
            return getFirefighter() + " (" + getName() + ")";
        } else if (mFirefighter != null) {
            return getFirefighter().toString();
        } else {
            return "";
        }
    }

    public boolean hasActed() {
        return mHasActed;
    }

    public void crewChange(Firefighter newFirefighter, Firefighter.Action action) {
        mCurrentAp += newFirefighter.getAp()
                - getFirefighter().getAp();
        mFirefighter = newFirefighter;
        mActions = null;
        perform(action);
    }

    public void undoAction() {
        if (!actionHistory.empty()) {
            PerformedAction performedAction = actionHistory.pop();
            System.err.println("undoing " + performedAction.action.getShortDescription());
            mCurrentAp += performedAction.generalApUsed;
            mCurrentBonusAp += performedAction.specialApUsed;
        }
    }

    private class PerformedAction implements Serializable {
        final Firefighter.Action action;
        final int specialApUsed;
        final int generalApUsed;

        PerformedAction(Firefighter.Action action, int generalCost, int specialCost) {
            this.action = action;
            this.generalApUsed = generalCost;
            this.specialApUsed = specialCost;
        }
    }
}
