package info.dylansymons.fpfrhelper.game;

import android.content.Context;

import java.util.ArrayList;

import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;
import info.dylansymons.fpfrhelper.player.Player;

public class Game {
    public static final int BASE = 0;
    public static final int PREVENTION = 1;
    public static final int URBAN = 2;
    public static final int VETERAN_DOG = 3;
    public static final String DEFAULT_NAME = "game";
    private boolean[] expansions = {false, false, false, false};
    private String mName;
    private ArrayList<Player> mPlayerList;
    private FirefighterList mFirefighterList;
    private long mId = -1;
    private int mCurrentPlayerIndex = -1;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<Player> getPlayerList() {
        return mPlayerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        mPlayerList = playerList;
    }

    public FirefighterList getFirefighterList() {
        return mFirefighterList;
    }

    public void setFirefighterList(ArrayList<Firefighter> firefighterList) {
        mFirefighterList = new FirefighterList(firefighterList);
        System.err.println("game has " + mFirefighterList.size() + " firefighters");
    }

    public int getCurrentPlayerIndex() {
        return mCurrentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int index) {
        mCurrentPlayerIndex = index;
    }

    private int getNextIndex() {
        return (mCurrentPlayerIndex + 1) % mPlayerList.size();
    }

    Player peekNext() {
        int nextIndex = getNextIndex();
        return mPlayerList.get(nextIndex);
    }

    Player getNext() {
        if (mCurrentPlayerIndex >= 0) {
            mPlayerList.get(mCurrentPlayerIndex).endTurn();
        }
        mCurrentPlayerIndex = getNextIndex();
        Player player = mPlayerList.get(mCurrentPlayerIndex);
        player.startTurn();
        return player;
    }

    Player getCurrent() {
        if (mCurrentPlayerIndex >= 0) {
            return mPlayerList.get(mCurrentPlayerIndex);
        } else {
            mCurrentPlayerIndex = -1;
            return getNext();
        }
    }

    public void checkExpansions(Context context) {
        mFirefighterList.checkExpansions(context, expansions);
    }

    public boolean[] getExpansions() {
        return expansions;
    }

    public void setExpansions(boolean[] expansions) {
        this.expansions = expansions;
    }
}
