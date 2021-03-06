package info.dylansymons.fpfrhelper.player;

import java.io.Serializable;
import java.util.Stack;

import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;

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
    public static final long NO_ID = -1;
    private final Stack<PerformedAction> actionHistory;
    private String mName;
    private Firefighter mFirefighter;
    private int mCurrentAp;
    private int mSavedAp;
    private int mCurrentBonusAp;
    private int mColour;
    private Firefighter.Action[] mActions;
    private boolean mHasActed;
    private Firefighter previousFirefighter;
    private long mId;

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

    public Player() {
        this(null, null, 0);
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

    public void startTurn() {
        mCurrentAp = mFirefighter.getAp() + mSavedAp;
        mSavedAp = 0;
        mCurrentBonusAp = mFirefighter.getBonusAp();
        mHasActed = false;
        actionHistory.clear();
    }

    public void endTurn() {
        mSavedAp = Math.min(mFirefighter.getMaxSavedAp(), mCurrentAp);
    }

    public int getAp() {
        return mCurrentAp;
    }

    public void setAp(int ap) {
        mCurrentAp = ap;
    }

    public int getBonusAp() {
        return mCurrentBonusAp;
    }

    public void setBonusAp(int ap) {
        mCurrentBonusAp = ap;
    }

    public Firefighter.Action[] getActions() {
        if (mActions == null) {
            mActions = mFirefighter.getActions();
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
            PerformedAction performed = new PerformedAction(cost, bonusAp - mCurrentBonusAp);
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

    public void crewChange(Firefighter newFirefighter) {
        previousFirefighter = mFirefighter;
        mCurrentAp += newFirefighter.getAp()
                - getFirefighter().getAp();
        mFirefighter = newFirefighter;
        mActions = null;
        perform(Firefighter.Action.CREW_CHANGE);
    }

    public void undoAction() {
        if (!actionHistory.isEmpty()) {
            PerformedAction performedAction = actionHistory.pop();
            mCurrentAp += performedAction.generalApUsed;
            mCurrentBonusAp += performedAction.specialApUsed;
        }
        if (actionHistory.isEmpty()) {
            mHasActed = false;
        }
    }

    public void undoCrewChange(FirefighterList firefighters) {
        mCurrentAp += previousFirefighter.getAp()
                - getFirefighter().getAp();
        firefighters.add(mFirefighter);
        firefighters.remove(previousFirefighter);
        mFirefighter = previousFirefighter;
        mActions = null;
        undoAction();
    }

    public int getSavedAp() {
        return mSavedAp;
    }

    public void setSavedAp(int ap) {
        mSavedAp = ap;
    }

    public Firefighter getPreviousFirefighter() {
        return previousFirefighter;
    }

    public void setPreviousFirefighter(Firefighter previousFirefighter) {
        this.previousFirefighter = previousFirefighter;
    }

    public void setHasActed(boolean hasActed) {
        mHasActed = hasActed;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    private class PerformedAction implements Serializable {
        final int specialApUsed;
        final int generalApUsed;

        PerformedAction(int generalCost, int specialCost) {
            this.generalApUsed = generalCost;
            this.specialApUsed = specialCost;
        }
    }
}
